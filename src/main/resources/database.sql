-- @block
CREATE TABLE GradeLevels(
    grade_level INT PRIMARY KEY,
    grade_name VARCHAR(255) NOT NULL
);

-- @block
INSERT INTO GradeLevels(grade_level,grade_name)
VALUES (11,'Grade 11'),
(12,'Grade 12');

-- @block
select grade_level, grade_name FROM GradeLevels;

-- @block
CREATE TABLE Sections(
    section_id VARCHAR(2),
    adviser VARCHAR(255),
    room INT,
    grade_level INT NOT NULL,
    section_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (section_id),
    FOREIGN KEY (grade_level) REFERENCES GradeLevels(grade_level)
);

-- @block
INSERT INTO Sections(section_id,adviser,room,grade_level,section_name)
VALUES('MC','Dumaquita',3,12,'Marie Curie'),
      ('DB','Blandeux',2,11,'Diosdado Banatao');

-- @block
SELECT * FROM sections;

-- @block
CREATE TABLE scan (
    lrn BIGINT NOT NULL,
    hashed_lrn CHAR(32),
    salt VARCHAR(32),
    PRIMARY KEY (lrn),
    FOREIGN KEY (lrn) REFERENCES students (lrn)
);

-- @block
CREATE TABLE Students(
    lrn BIGINT,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    last_name VARCHAR(255),
    grade_level int,
    sex VARCHAR(6),
    section_id VARCHAR(2),
    guardian_name VARCHAR(255),
    contact_guardian BIGINT,
    address TEXT,
    scan BIGINT,
    PRIMARY KEY(lrn, scan),
    FOREIGN KEY (grade_level) REFERENCES GradeLevels(grade_level),
    FOREIGN KEY (section_id) REFERENCES Sections(section_id),
    FOREIGN KEY (scan) REFERENCES scan (lrn)
);

-- @block
    INSERT INTO Students (last_name, first_name, middle_name, lrn, guardian_name, contact_guardian, address, sex,grade_level,section_id) VALUES
  ('Aguilon ', 'Sian Zyryll ', 'C.', '136812120445', 'Elino Aguilon ', '09197770725', 'BLK 1 LOT 11 CNAI, MAHABANG PARANG ST., BIGNAY, VALENZUELA CITY', 'Male', 11, 'DB'),
('ALCOBER ', 'Meg ryan', 'A.', '136812110015', 'Marites Alcober ', '09197409590', 'Blk 26 lot 5 northville 1 A bignay Valenzuela city ', 'Male', 11, 'DB'),
('Ambos', 'Clarrise Jolia', 'G. ', '136815120162', 'Olivia G. Ambos ', '09203688358', '#7 Malinis St. Lawang Bato Valenzuela City', 'Female', 11, 'DB'),
('Antonio', 'Ricky', 'B.', '101294120004', 'Creslie A. Batbatan', '09514328305', 'DISIPLINA VILLAGE BIGNAY BLDG.6 BLCK.6 UNIT.211', 'Male', 11, 'DB'),
('Apole', 'Irene Ann', 'moreno', '136812110029', 'marissa moreno', '9271522140', ' blk 32 lot 6 valenzuela view bignay  valenzuela city, ', 'Female', 11, 'DB'),
('APUYA', 'CHRISTINE', 'E.', '136815120330', 'LESLIE ECLEO', '09564698638', '#18 Sapang Bakaw St. Lawang Bato Val City', 'Female', 11, 'DB'),
('Baccay', 'John Michael', 'S.', '136818110013', 'Josephine Baccay', '09394352160', 'Blk.10 Lot 17 Northville 1, Bignay Valenzuela City', 'Male', 11, 'DB'),
('Barotil', 'Jorie Gabriel ', 'A.', '136815110035', 'Jorge Sajolga Barotil', '09084516882', 'Blk.18 Lot10 Northville 1B Punturin, Valenzuela City', 'Male', 11, 'DB'),
('Benigla', 'Maelyn Rose ', 'G.', '136812110067', 'Ruben Benigla', '09232126693', '0353 LE SERV 12  GALAS ST.BIGNAY VALENZUELA CITY ', 'Female', 11, 'DB'),
('Bersabe', 'Caleb', 'S', '104923110025', 'Ailene Bersabe', '09612086563', 'Blk 5 Lt 7 Orange St. Natividad Townhomes, Punturin, Valenzuela City', 'Male', 11, 'DB'),
('Borja', 'Mark Daniel', 'H', '136815110051', 'Dianne Carla Borja', '09067179167', 'Disiplina Village Bignay Blk 2 Bldg 3 Unit 202 Valenzuela City', 'Male', 11, 'DB'),
('BRIZO', 'ERICH', 'ERICH', '113827120007', 'BRIZO, DECHIE E.', '+639561750939', 'block 5 Altea Subdivision, Bignay, Valenzuela city ', 'Female', 11, 'DB'),
('Caña', 'Julliene Frankie', 'R.', '136653120504', 'Jenny Caña', '09367910380', 'Blk.6 Bldg.8 Unit-302 Disiplina Village Bignay Valenzuela City', 'Female', 11, 'DB'),
('Cezar', 'Keziah', 'N/A', '406472150386', 'Kathy I. Cezar', '09254701167', '1132 Avocado St. Bagbaguin Valenzuela City', 'Female', 11, 'DB'),
('CLARITO', 'CREA MAE', 'M.', '108631110018', 'EDWIN CLARITO', '639129957370', '#28 MULAWINAN ST.LAWANG BATO VALENZUELA CITY', 'Female', 11, 'DB'),
('Consignado', 'Rica ', 'A.', '136797110192', 'Janice G. Atienza', '09636886482', 'Disiplina Village bignay block6 building9 unit105', 'Female', 11, 'DB'),
('Corral', 'Allan', 'B', '136818100084', 'Elena B. Corral', '09481792570', '#64 Galas st. Bignay Valenzuela city', 'Male', 11, 'DB'),
('CRISTOBAL', 'CHAMP JIAN', 'SAN ROQUE', '136818110043', 'Maria Cristina Cristobal', '09329176456', '#32. P Faustino St., Punturin, Valenzuela City', 'Male', 11, 'DB'),
('De Jesus ', 'Angelica ', 'V.', '136812110119', 'Jocelyn Villanueva ', '09169147673', '76 Becona Compound Galas st Bignay Valenzuela City ', 'Female', 11, 'DB'),
('DELA CRUZ', 'ANGELO', 'M.', '136818120002', 'Angelita Dela Cruz', '09561609854', 'Blk.3 Lot#14 Sampaguita St. Sta.Lucia Phase 6 Village Punturin Valenzuela City', 'Male', 11, 'DB'),
('Fa-ala', 'Rochelle Anne Cloe ', 'C.', '136812120037', 'Cherry C. Fa-ala', '09666383106', 'B20 L12 Benahavis St., Tierra Nova Royale 3, Bagumbong, Caloocan City', 'Female', 11, 'DB'),
('FRANCISCO ', 'JOHN MARK ', 'ORBISTA', '136812120839', 'GLENDA ORBISTA', '09517874175', 'blk 10 lot 1 phase 1 ammabelle llano road caloocan city ', 'Male', 11, 'DB'),
('Gazo', 'Jenalyn', 'Alba', '136539122481', 'Nelia alba gazo', '09982916671', 'blk 13 disiplina village bignay valenzuela city ', 'Female', 11, 'DB'),
('Geradila ', 'Amceilay Angel', 'V.', '136505120952', 'Carmelita V. Geradila', '09216388407', 'Blk 1 Lot 2 Avisea Subd. Bignay Valenzuela City', 'Female', 11, 'DB'),
('Gomez', 'James Patrick', 'D', '136818110068', 'Rosenda D. Gomez', '09292930773', 'Blk 3 lot 17 gonzales compound mahabang parang bignay valenzuela city', 'Male', 11, 'DB'),
('LABESORIS ', 'KEMBERLY ANN', 'R.', '136797110378', 'Veronica Labesoris ', '09219238060', 'BLK 11 BLDG 1 UNIT 202 DISIPLINA VILLAGE BIGNAY VALENZUELA CITY', 'Female', 11, 'DB'),
('LEONARDO', 'MARK ZENKI', 'E', '136812110202', 'Edward D. Leonardo', '09278790227', 'Block 50 Lot 1 Northville 2 Bignay,Valenzuela City ', 'Male', 11, 'DB'),
('LUCAYA', 'REYCHIE ', 'IBAÑEZ', '227003110104', 'MAY-CHERRY IBAÑEZ LUCAYA ', '09970784620', 'NORTH VILLE 1B BLK4 LOT18 PUNTURIN VALENZUELA CITY ', 'Male', 11, 'DB'),
('Manalo ', 'Nicka', 'S.', '136812110231', 'Bernadith Manalo', '09454493178', 'Blk 38 Lot 3 Northville ll Bignay Valenzuela City', 'Female', 11, 'DB'),
('Mangahas ', 'Jhennesis ', 'L.', '136830110280', 'Pedro Mangahas', '09164401191', 'blk4lot19 apple st natividad townhome punturin val city', 'Female', 11, 'DB'),
('Mangalindan ', 'Francis Bean', 'Sta.cruz', '136818110088', 'Jeanne S. Mangalindan ', '09126969927', 'North ville 1-b block 19 lot 20', 'Male', 11, 'DB'),
('Manicane', 'John Warren', 'Ducusin', '104915120277', 'Manicane, Lailine Ducusin', '09303933038', 'Blk 19 Lot 14 Tudela St. TNR 3 Bagumbong Caloocan City', 'Male', 11, 'DB'),
('Martin', 'Miguel Esteban ', 'M. ', '136815110166', 'Elma Macugay Martin ', '9166009600', '#63Mulawinan st. Lawang bato Valenzuela city ', 'Male', 11, 'DB'),
('Ojeda', 'Franchette Anne ', 'Solis', '136812130889', 'Imee S. Ojeda', '09633949830', 'Blk 3 Lot 2 Grande Vita 2 Bignay Valenzuela City', 'Female', 11, 'DB'),
('Olleras ', 'Francine Joy ', 'M.', '104923110108', 'Rabecca M. Olleras ', '09395500790', 'Hulo st. Libtong Meycauayan Bulacan ', 'Female', 11, 'DB'),
('Osiel ', 'Vea', 'J.', '136818120054', 'Vivian J. Osiel ', '09296914689', 'BLK 3 LOT 8 Apple St. Natividad Townhomes Punturin Valenzuela City ', 'Female', 11, 'DB'),
('Pachica', 'Franchesca', 'C.', '136693110111', 'Cheryle Corcuera', '09208409800', 'Blk 8 Lot 43, Camella Vera, Ibaba St. Bignay, Valenzuela City.', 'Female', 11, 'DB'),
('Pacifico', 'Kevin Chan', 'NONE', '136815120182', 'Gracelyn P. Monterroyo', '09459925617', 'B.6 L.4 Devega compound ulingan west lawang bato Val. city', 'Male', 11, 'DB'),
('PADILLA', 'KRISTINE SHIRLET ', 'ESPARTINEZ ', '136815120273', 'ARNOLD VALDEZ PADILLA', '09770219854', 'Blk.1 Lot 18 Ilang-ilang Street Phase 6 Sta. Lucia Village Punturin Valenzuela City ', 'Female', 11, 'DB'),
('PANAL', 'JULIE CENN', 'N.', '407188150603', 'ROWENA A. NEPOMUCENO ', '09977732195', 'Blk 33 lot 4 Valenzuela view brgy bignay galas st. valenzuela city', 'Female', 11, 'DB'),
('PANAMBO', 'KEANU KING', 'S.', '136815100338', 'PANAMBO, EVANGELINE S.', '09322298435', '77 P. FAUSTINO STREET SMBAPI COMP. PUNTURIN VALENZUELA CITY', 'Male', 11, 'DB'),
('PAPIONA', 'JOHN DAVE', 'H.', '136815120221', 'Juvelyn Papiona', '09994822779', 'Block 29 Lot 16 Sassafras Street Grand Cedar Homes Bignay Valenzuela City', 'Male', 11, 'DB'),
('Prieto', 'Kane Aldren', 'P.', '136812120579', 'Arcelie P. Prieto', '09352723986', '893 Brilliant View Phase 1, Bagumbog Dulo, Caloocan City', 'Male', 11, 'DB'),
('Rico', 'Jonny ', 'P', '136818100233', 'Lolita S. Panzuelo', '09385500079', 'Blk 4 Bldg 15 Unit 212 Disiplina village Bignay Val. City', 'Male', 11, 'DB'),
('Rosales', 'Joshua', 'F.', '136815110230', 'Alona Frias', '09758720433', 'Disiplina Village Bignay Blk 2 Bldg 7 Unit 106 Valenuela City.', 'Male', 11, 'DB'),
('Rosales', 'Euri, Dustin', 'A', '136818120065', 'Mylene A. Rosales', '09309369208', 'Blk 5 lot 24 northville 1-B Punturin Valenzuela City', 'Male', 11, 'DB'),
('Salmorin', 'Jennilyn', 'De Jesus ', '136812110339', 'Nerissa Salmorin ', '09101735553', '#096 Ibaba St.Bignay Valenzuela City ', 'Female', 11, 'DB'),
('Segundo', 'John Lendon', 'P.', '136652120181', 'Visitacion B. Palanas', '09323551612', 'Blk 7 lot 17 grand cedar homes brgy bignay valenzuela city', 'Male', 11, 'DB'),
('Sollestre', 'Ninotchca', 'B.', '136812110352', 'Sylvia Sollestre', '09178293236', 'Blk 4 Lot 11 Avisea Subdivision Bignay Valenzuela City ', 'Female', 11, 'DB'),
('Templado ', 'Ronillo ', 'Caguiat ', '136648120898', 'Ronillo A. Templado ', '09636203170', 'Grand cedar home"s blk 26 lot 47', 'Male', 11, 'DB'),
('Tolentino', 'Rommel', 'Enobio', '127995121362', 'Chezza Mae E. Tolentino', '09974155286', '078-C Ibaba St. Bignay Valenzuela City', 'Male', 11, 'DB'),
('TORRECAMPO ', 'MIGUEL ARGIO', 'G.', '227003120112', 'Gloria Torrecampo ', '09270496101', 'Blk 7 lot 10 Avante Townhomes Bagumbong Caloocan City,', 'Male', 11, 'DB'),
('UGMAD ', 'MITCH ', 'L', '136818120056', 'RESTITUTO P. UGMAD ', '09219964864', 'BLK 18 LOT 2 NORTHVILLE 1 PHASE 1 BIGNAY VALENZUELA CITY ', 'Female', 11, 'DB'),
('Velarde ', 'Sophia ', 'G.', '136797110668', 'Adelfa G. Velarde', '09489456084', 'Blk10 lt 2 phase 4 pearl island malinis st. Lawang bato Valenzuela City', 'Female', 11, 'DB'),
('Yabut', 'Stephanie ', 'Valera', '483514150215', 'Cheryl Yabut', '09338510459', 'Blk 57 Lot 13 Spruce St. Phase 1 Cedar Homes, Bignay, Valenzuela City', 'Female', 11, 'DB'),
('Dellosa', 'Andrea Jellah', 'C.', '136652100292', 'Analyn T. Cericos', '09467534613', '1832 Tagumpay Village Bagbaguin, Caloocan City', 'Female', 11, 'DB');




-- @block
INSERT INTO Students
VALUES(136818110047,'ADRIAN','MONTANA','DE VERA',12,'M','MC');

-- @block
CREATE TYPE status AS ENUM('LATE','ONTIME');

CREATE TABLE Attendance(
    id SERIAL,
    student_id BIGINT NOT NULL,
    attendance_status status,
    date DATE DEFAULT CURRENT_DATE,
    time TIME DEFAULT LOCALTIME,
    time_out TIME DEFAULT LOCALTIME,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES Students(lrn)
);

-- @block
ALTER TABLE Attendance
    ALTER COLUMN date SET DEFAULT CURRENT_DATE,
ALTER COLUMN time SET DEFAULT LOCALTIME;
-- @block
SELECT * FROM students
-- @block
INSERT INTO Attendance(student_id,date,time,attendance_status) VALUES (136815120330,CURRENT_DATE,LOCALTIME,CASE
WHEN LOCALTIME < '07:00:00' THEN status('ONTIME')
    ELSE status('LATE')
    END);
-- @block
select * from attendance
                  INNER JOIN students
                             ON students.lrn = attendance.student_id
WHERE section_id = 'DB';
-- @block
SELECT * FROM attendance
                  INNER JOIN students
                             ON students.lrn = attendance.student_id;



-- @block
-- Change it to character varying.
ALTER TABLE attendance
ALTER COLUMN attendance_status TYPE character varying;

ALTER TABLE scan
    ADD CONSTRAINT fk_lrn_students FOREIGN KEY (lrn) REFERENCES students (lrn);