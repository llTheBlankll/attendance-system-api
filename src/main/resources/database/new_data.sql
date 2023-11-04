-- Add some data to Grade levels
INSERT INTO GradeLevels(grade_level, grade_name)
VALUES (11, 'Grade 11'),
       (12, 'Grade 12');

-- Add subject
INSERT INTO Subjects (name, description)
VALUES ('General Mathematics', 'Hard Subject');

-- Add Section


-- Add Teacher
INSERT INTO Teachers (first_name, middle_name, last_name, birth_date, subject_expertise, sex, address, contact_number,
                      email)
VALUES ('John', 'Doe', 'Doe', '1990-01-01', 1, 'Male', '1234', '1234567890', 'jdoe@u.com');