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
       (2, 'STEM');

-- ADD DUMMY ROLE
INSERT INTO Roles (role_name)
VALUES ('ADMIN'),
       ('PRINCIPAL'),
       ('TEACHER'),
       ('STUDENT'),
       ('OTHER');

-- ADD DUMMY USERS
INSERT INTO Users (user_name, password, email, role_id, last_login)
VALUES ('admin', '$2a$12$ll5AqR1tdfRBB3v1UcBEr.HAGmYxQ9lUxdFcLFZAbMJwQUczjQ9OG', 'admin', 1, '2022-01-01 00:00:00'),
       ('principal', '$2a$12$GQTtgkh7ufBcjrQqY683KOKvG/ZS796Al1siLsmlCD5PtalfPyV1S', 'principal', 2, '2022-01-01 00:00:00'),
       ('teacher', '$2a$12$9m6b4VxlAXCCGF1V7rtH2evITY0nFJ.NPneDMwD1xy9l.ueba1az2', 'teacher', 3, '2022-01-01 00:00:00'),
       ('student', '$2a$12$a3aGT3oFUy6SKd5otLJ3COKGl5cDMviWn5.w9JW6gOx6YRQpJmxaa', 'student', 4, '2022-01-01 00:00:00');

-- ADD ROLES
INSERT INTO users_role (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4);