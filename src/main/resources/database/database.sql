CREATE TABLE Strand
(
	strand_id   SERIAL PRIMARY KEY,
	strand_name VARCHAR(255) NOT NULL
);

-- * GRADE LEVELS TABLE
CREATE TABLE GradeLevels
(
	grade_level  SERIAL PRIMARY KEY,
	grade_name   VARCHAR(255) NOT NULL,
	grade_strand INT,
	CONSTRAINT gradelevels_grade_strand_fk FOREIGN KEY (grade_strand) REFERENCES Strand (strand_id)
);

-- * Create enum types for each table.
CREATE TYPE Status AS ENUM ('LATE','ONTIME', 'OUT', 'ABSENT');

-- CREATE A CAST, The CREATE CAST solution does not seem to work when the enum
-- is used as an argument of a JPA Repository. E.g.
-- Entity findByMyEnum(MyEnum myEnum)
CREATE CAST (CHARACTER VARYING as Status) WITH INOUT AS IMPLICIT;

-- * Creates Subjects Table.
CREATE TABLE Subjects
(
	subject_id  SERIAL PRIMARY KEY,
	name        VARCHAR(128),
	description TEXT
);
-- * Creates Teachers Table.
CREATE TABLE Teachers
(
	teacher_id  SERIAL,
	first_name  VARCHAR(32),
	middle_name VARCHAR(32),
	last_name   VARCHAR(32),
--     sex               VARCHAR(8),
--     subject_expertise INT,
	PRIMARY KEY (teacher_id)
--     FOREIGN KEY (subject_expertise) REFERENCES Subjects (subject_id) ON DELETE SET NULL
);
-- CREATE INDEX teachers_subject_expertise_idx ON Teachers (subject_expertise);

-- * SECTIONS TABLE
CREATE TABLE Sections
(
	section_id   SERIAL PRIMARY KEY,
	teacher      INT          NULL,
	room         INT,
	strand       INT,
	grade_level  INT          NOT NULL,
	section_name VARCHAR(255) NOT NULL,
	FOREIGN KEY (grade_level) REFERENCES GradeLevels (grade_level) ON DELETE SET NULL,
	FOREIGN KEY (teacher) REFERENCES Teachers (teacher_id) ON DELETE SET NULL,
	FOREIGN KEY (strand) REFERENCES Strand (strand_id) ON DELETE SET NULL
);

-- * STUDENTS TABLE
CREATE TABLE Students
(
	lrn         BIGINT PRIMARY KEY,
	first_name  VARCHAR(255) NOT NULL,
	middle_name VARCHAR(255) NULL,
	last_name   VARCHAR(255),
	grade_level INT,
	sex         VARCHAR(6),
	section_id  INT,
	address     TEXT,
	birthdate   DATE         NOT NULL,
	FOREIGN KEY (grade_level) REFERENCES GradeLevels (grade_level) ON DELETE SET NULL,
	FOREIGN KEY (section_id) REFERENCES Sections (section_id) ON DELETE SET NULL
);
CREATE INDEX students_section_id_idx ON Students (section_id);
CREATE INDEX students_grade_level_idx ON Students (grade_level);

-- * RFID CREDENTIALS
CREATE TABLE rfid_credentials
(
	lrn        BIGINT NOT NULL PRIMARY KEY,
	hashed_lrn CHAR(32),
	salt       VARCHAR(16),
	enabled    BOOLEAN DEFAULT TRUE,
	FOREIGN KEY (lrn) REFERENCES students (lrn) ON DELETE CASCADE
);
CREATE INDEX rfid_credentials_lrn_idx ON rfid_credentials (lrn);

-- * GUARDIANS TABLE
CREATE TABLE Guardians
(
	guardian_id    SERIAL PRIMARY KEY,
	student_lrn    BIGINT,
	full_name      VARCHAR(255) NOT NULL,
	contact_number VARCHAR(32),
	FOREIGN KEY (student_lrn) REFERENCES Students (lrn) ON DELETE CASCADE
);
CREATE INDEX guardian_student_id_idx ON Guardians (student_lrn);
CREATE INDEX guardian_full_name_idx ON Guardians (full_name);

-- * ATTENDANCE TABLE
CREATE TABLE Attendance
(
	id                SERIAL PRIMARY KEY,
	student_id        BIGINT NOT NULL,
	attendance_status Status,
	date              DATE DEFAULT CURRENT_DATE,
	time              TIME DEFAULT LOCALTIME,
	time_out          TIME DEFAULT LOCALTIME,
	CONSTRAINT fk_student_lrn FOREIGN KEY (student_id) REFERENCES students (lrn) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX attendance_student_id_idx ON Attendance (student_id);
CREATE INDEX attendance_date_idx on Attendance (date);

-- * MAKE ATTENDANCE ENUM TYPE CHARACTER VARYING
ALTER TABLE Attendance
	ALTER COLUMN attendance_status TYPE CHARACTER VARYING;

-- * CREATE FINGERPRINT TABLE
CREATE TABLE Fingerprint
(
	id             SERIAL PRIMARY KEY,
	fingerprint_id VARCHAR(255) NOT NULL,
	student        BIGINT,
	template_data  TEXT,
	CONSTRAINT fk_student_lrn FOREIGN KEY (student) REFERENCES students (lrn) ON DELETE SET NULL
);

-- * CREATE ROLES TABLE
CREATE TABLE Roles
(
	role_id   SERIAL PRIMARY KEY,
	role_name VARCHAR(128)
);

-- * CREATE USERS TABLE
CREATE TABLE Users
(
	user_id    SERIAL PRIMARY KEY,
	user_name  VARCHAR(255),
	password   VARCHAR(255),
	email      VARCHAR(255),
	role_id    INT,
	last_login TIMESTAMP,
	FOREIGN KEY (role_id) REFERENCES Roles (role_id) ON DELETE SET NULL
);

-- * CREATE USERS_ROLE
CREATE TABLE Users_Role
(
	users_role_id SERIAL,
	user_id       BIGINT,
	role_id       BIGINT,
	PRIMARY KEY (user_id, role_id, users_role_id),
	FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE,
	FOREIGN KEY (role_id) REFERENCES Roles (role_id) ON DELETE CASCADE
);

-- * CREATE LEAVE TABLE
CREATE TABLE Leaves
(
	leave_id   SERIAL PRIMARY KEY,
	lrn        INT  NOT NULL,
	start_date DATE NOT NULL,
	end_date   DATE NOT NULL,
	reason     TEXT NOT NULL,
	status     VARCHAR(20) DEFAULT 'PENDING',
	created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,

	CONSTRAINT fk_leave_student FOREIGN KEY (lrn) REFERENCES students (lrn)
);

-- * CREATE NOTIFICATION TABLE
CREATE TABLE GlobalNotifications (
	notification_id SERIAL,
	title TEXT NOT NULL,
	message TEXT NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (notification_id)
);

-- * CREATE NOTIFICATION GUARDIANS
CREATE TABLE GuardianNotifications (
	notification_id SERIAL,
	guardian_id INT,
	title TEXT NOT NULL,
	message TEXT NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (notification_id),
	FOREIGN KEY (guardian_id) REFERENCES Guardians (guardian_id)
);

-- * CREATE TRIGGER AND NOTIFY --
CREATE OR REPLACE FUNCTION notify_changes_attendance() RETURNS TRIGGER AS
$$
DECLARE
	payload VARCHAR;
	channel TEXT := 'attendance_channel';
BEGIN
	IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
		SELECT json_build_object(
			       'new', NEW,
			       'old', OLD
		       )::text
		INTO payload;
		PERFORM pg_notify(channel, payload);
		RETURN NEW;
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER on_event_attendance
	AFTER INSERT OR UPDATE
	ON Attendance
	FOR EACH ROW
EXECUTE FUNCTION notify_changes_attendance();

-- ==================== SELECT STATEMENTS =================== --

-- show all sections.
SELECT *
FROM sections;

-- show all grade levels.
select grade_level, grade_name
FROM GradeLevels;

-- Select all data from sections.
SELECT *
FROM sections;
