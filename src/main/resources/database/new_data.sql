-- Add some data to Grade levels
INSERT INTO GradeLevels(grade_level, grade_name)
VALUES (11, 'Grade 11'),
       (12, 'Grade 12');

-- Add subject
INSERT INTO Subjects (name, description)
VALUES ('General Mathematics', 'Hard Subject');

-- Add Teacher
INSERT INTO Teachers (first_name, middle_name, last_name)
VALUES ('Gladys', null, 'Austria');

-- Add Section
INSERT INTO sections (teacher, room, grade_level, section_name)
VALUES (1, 301, 11, 'Casimiro Del Rosario');
