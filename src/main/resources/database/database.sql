CREATE TABLE Strand
(
    strand_id   SERIAL PRIMARY KEY,
    strand_name VARCHAR(255) NOT NULL
);

-- @block
CREATE TABLE GradeLevels(
                            grade_level  SERIAL PRIMARY KEY,
                            grade_name   VARCHAR(255) NOT NULL,
                            grade_strand INT,
                            CONSTRAINT gradelevels_grade_strand_fk FOREIGN KEY (grade_strand) REFERENCES Strand (strand_id)
);

-- Create enum types for each table.
CREATE TYPE Status AS ENUM ('LATE','ONTIME');

-- CREATE A CAST, The CREATE CAST solution does not seem to work when the enum
-- is used as an argument of a JPA Repository. E.g.
-- Entity findByMyEnum(MyEnum myEnum)
CREATE CAST (CHARACTER VARYING as Status) WITH INOUT AS IMPLICIT;

-- Creates Subjects Table.
CREATE TABLE Subjects
(
    subject_id  SERIAL PRIMARY KEY,
    name        VARCHAR(128),
    description TEXT
);
-- Creates Teachers Table.
CREATE TABLE Teachers
(
    teacher_id        SERIAL,
    first_name        VARCHAR(32),
    middle_name       VARCHAR(32),
    last_name         VARCHAR(32),
    birth_date        DATE,
    subject_expertise INT,
    sex               VARCHAR(6),
    address           VARCHAR(255),
    contact_number    VARCHAR(48),
    email             VARCHAR(255),
    PRIMARY KEY (teacher_id),
    FOREIGN KEY (subject_expertise) REFERENCES Subjects (subject_id) ON DELETE SET NULL
);
CREATE INDEX teachers_subject_expertise_idx ON Teachers (subject_expertise);

-- @block
CREATE TABLE Sections
(
    section_id SERIAL PRIMARY KEY,
    teacher    INT NULL,
    room INT,
    strand INT,
    grade_level INT NOT NULL,
    section_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (grade_level) REFERENCES GradeLevels (grade_level) ON DELETE SET NULL,
    FOREIGN KEY (teacher) REFERENCES Teachers (teacher_id) ON DELETE SET NULL,
    FOREIGN KEY (strand) REFERENCES Strand (strand_id) ON DELETE SET NULL
);

-- @block
CREATE TABLE Students
(
    lrn              BIGINT PRIMARY KEY,
    first_name       VARCHAR(255) NOT NULL,
    middle_name      VARCHAR(255) NULL,
    last_name        VARCHAR(255),
    grade_level      INT,
    sex              VARCHAR(6),
    section_id INT,
    address          TEXT,
    birthdate DATE NOT NULL,
    FOREIGN KEY (grade_level) REFERENCES GradeLevels (grade_level) ON DELETE SET NULL,
    FOREIGN KEY (section_id) REFERENCES Sections (section_id) ON DELETE SET NULL
);

-- @block
CREATE TABLE rfid_credentials
(
    lrn        BIGINT NOT NULL PRIMARY KEY,
    hashed_lrn CHAR(32),
    salt       VARCHAR(16),
    FOREIGN KEY (lrn) REFERENCES students (lrn) ON DELETE CASCADE
);
CREATE INDEX rfid_credentials_lrn_idx ON rfid_credentials (lrn);

-- @block
CREATE TABLE Guardians
(
    guardian_id             SERIAL PRIMARY KEY,
    student_lrn BIGINT,
    full_name      VARCHAR(255) NOT NULL,
    contact_number VARCHAR(32),
    FOREIGN KEY (student_lrn) REFERENCES Students (lrn) ON DELETE CASCADE
);
CREATE INDEX guardian_student_id_idx ON Guardians (student_lrn);

-- @block
CREATE TABLE Attendance
(
    id SERIAL PRIMARY KEY,
    student_id        BIGINT NOT NULL,
    attendance_status Status,
    date              DATE DEFAULT CURRENT_DATE,
    time              TIME DEFAULT LOCALTIME,
    time_out          TIME DEFAULT LOCALTIME,
    CONSTRAINT fk_student_lrn FOREIGN KEY (student_id) REFERENCES students (lrn) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE Attendance
    ALTER COLUMN attendance_status TYPE CHARACTER VARYING;
CREATE INDEX attendance_student_id_idx ON Attendance (student_id);

-- CREATE TRIGGER AND NOTIFY --
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
