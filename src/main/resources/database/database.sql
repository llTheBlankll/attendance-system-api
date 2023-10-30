-- @block
CREATE TABLE GradeLevels(
    grade_level INT PRIMARY KEY,
    grade_name VARCHAR(255) NOT NULL
);

-- Create enum types for each table.
CREATE TYPE Status AS ENUM ('LATE','ONTIME');

-- CREATE A CAST, The CREATE CAST solution does not seem to work when the enum
-- is used as an argument of a JPA Repository. E.g.
-- Entity findByMyEnum(MyEnum myEnum)
CREATE CAST (CHARACTER VARYING as Status) WITH INOUT AS IMPLICIT;
CREATE CAST (CHARACTER VARYING as Relationship) WITH INOUT AS IMPLICIT;

-- @block
CREATE TABLE Sections
(
    section_id VARCHAR(2) PRIMARY KEY,
    teacher    INT NULL,
    room INT,
    grade_level INT NOT NULL,
    section_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (section_id),
    FOREIGN KEY (grade_level) REFERENCES GradeLevels (grade_level),
    FOREIGN KEY (teacher) REFERENCES Teachers (teacher_id)
);

CREATE TABLE Guardians
(
    guardian_id             SERIAL PRIMARY KEY,
    student_id              BIGINT,
    full_name      VARCHAR(255) NOT NULL,
    contact_number VARCHAR(32),
    FOREIGN KEY (student_id) REFERENCES Students (lrn)
);

CREATE INDEX guardian_student_id_idx ON Guardians (student_id);

-- @block
CREATE TABLE Students
(
    lrn         BIGINT PRIMARY KEY,
    rfid_credentials BIGINT,
    first_name       VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255) NULL,
    last_name        VARCHAR(255),
    grade_level INT,
    sex              VARCHAR(6),
    section_id       VARCHAR(2),
    address          TEXT,
    UNIQUE (rfid_credentials),
    FOREIGN KEY (grade_level) REFERENCES GradeLevels (grade_level),
    FOREIGN KEY (section_id) REFERENCES Sections (section_id)
);

CREATE INDEX student_rfid_credentials_idx ON Students (rfid_credentials);

-- @block
CREATE TABLE rfid_credentials
(
    lrn  BIGINT NOT NULL PRIMARY KEY,
    hashed_lrn CHAR(32),
    salt VARCHAR(16),
    FOREIGN KEY (lrn) REFERENCES students (lrn) ON DELETE CASCADE
);

CREATE INDEX rfid_credentials_lrn_idx ON rfid_credentials (lrn);


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
    FOREIGN KEY (subject_expertise) REFERENCES Subjects (subject_id)
);
CREATE INDEX teachers_subject_expertise_idx ON Teachers (subject_expertise);

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
