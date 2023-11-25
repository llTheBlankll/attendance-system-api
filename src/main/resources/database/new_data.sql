-- Add some data to Grade levels
INSERT INTO GradeLevels(grade_level, grade_name)
VALUES (11, 'Grade 11'),
       (12, 'Grade 12');

-- Add subject
INSERT INTO Subjects (name, description)
VALUES ('General Mathematics', 'Hard Subject');

-- ADD STRAND
INSERT INTO Strand (strand_id, strand_name)
VALUES (1, 'HUMSS'),
       (2, 'STEM')