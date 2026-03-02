CREATE TABLE students
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(50)  NOT NULL,
    lastname  VARCHAR(50)  NOT NULL,
    password  VARCHAR(100) NOT NULL,
    grade        VARCHAR(10) NOT NULL,
    section      VARCHAR(10) DEFAULT 'NONE',
    roll         VARCHAR(10) NOT NULL,
    academic_year VARCHAR(10) NOT NULL,
    CONSTRAINT unique_class_roll UNIQUE (grade, section, roll, academic_year)
);

CREATE TABLE teachers
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(50)  NOT NULL,
    lastname  VARCHAR(50)  NOT NULL,
    username  VARCHAR(50)  NOT NULL,
    password  VARCHAR(100) NOT NULL
);
