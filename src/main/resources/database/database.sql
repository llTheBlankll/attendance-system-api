-- @block
CREATE TABLE GradeLevels(
    grade_level INT PRIMARY KEY,
    grade_name VARCHAR(255) NOT NULL
);

-- Create enum types for each table.
CREATE TYPE Status AS ENUM ('LATE','ONTIME');
CREATE TYPE Relationship AS ENUM (
    'FATHER',
    'MOTHER',
    'GUARDIAN',
    'SIBLING',
    'STEPFATHER',
    'STEPMOTHER',
    'GRANDPARENT'
        'OTHER'
    );

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
    first_name              VARCHAR(32),
    middle_name             VARCHAR(32),
    last_name               VARCHAR(32),
    relationship_to_student Relationship NOT NULL DEFAULT 'OTHER',
    FOREIGN KEY (student_id) REFERENCES Students (lrn)
);

-- @block
CREATE TABLE Students
(
    lrn         BIGINT PRIMARY KEY,
    rfid_credentials BIGINT,
    first_name       VARCHAR(255) NOT NULL,
    middle_name      VARCHAR(255),
    last_name        VARCHAR(255),
    grade_level INT,
    sex              VARCHAR(6),
    section_id       VARCHAR(2),
    guardian    INT,
    address          TEXT,
    UNIQUE (rfid_credentials),
    FOREIGN KEY (grade_level) REFERENCES GradeLevels (grade_level),
    FOREIGN KEY (section_id) REFERENCES Sections (section_id),
    FOREIGN KEY (guardian) REFERENCES Guardians (guardian_id)
);

-- @block
CREATE TABLE rfid_credentials
(
    lrn  BIGINT NOT NULL PRIMARY KEY,
    hashed_lrn CHAR(32),
    salt VARCHAR(16),
    FOREIGN KEY (lrn) REFERENCES students (lrn) ON DELETE CASCADE
);


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
