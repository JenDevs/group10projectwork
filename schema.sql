CREATE DATABASE IF NOT EXISTS education;

USE education;

CREATE TABLE city
(
    cityId   INT NOT NULL AUTO_INCREMENT UNIQUE,
    cityName VARCHAR(255),
    PRIMARY KEY (cityId)
);


CREATE TABLE school
(
    schoolId     INT NOT NULL AUTO_INCREMENT UNIQUE,
    schoolName   VARCHAR(255),
    schoolCityId INT,
    PRIMARY KEY (schoolId),
    FOREIGN KEY (schoolCityId) REFERENCES city (cityId)
);


CREATE TABLE course
(
    courseID       INT NOT NULL AUTO_INCREMENT,
    courseName     VARCHAR(255),
    courseSchoolId INT,
    PRIMARY KEY (courseID),
    FOREIGN KEY (courseSchoolId) REFERENCES school (schoolId)
);


CREATE TABLE student
(
    studentId       INT NOT NULL AUTO_INCREMENT,
    studentName     VARCHAR(50),
    studentBirthday DATE,
    studentGender   VARCHAR(50),
    studentCourseId INT,
    PRIMARY KEY (studentId),
    FOREIGN KEY (studentCourseId) REFERENCES course (courseId)
);


CREATE TABLE exam
(
    examId        INT          NOT NULL AUTO_INCREMENT,
    examName      VARCHAR(255) NOT NULL,
    examRating    INT          NOT NULL,
    examStudentId INT,
    PRIMARY KEY (examId),
    FOREIGN KEY (examStudentId) REFERENCES student (studentId)
);


INSERT INTO city(cityName)
VALUES ('Gothenburg'), -- 1
       ('Stockholm'),  -- 2
       ('Borås'),      -- 3
       ('Malmö'); -- 4


INSERT INTO school(schoolName, schoolCityId)
VALUES

    -- Göteborg
    ('Hvitfeldtska Gymnasium', 1),
    ('Katrinelund Gymnasium', 1),
    ('NTI Gymnasium', 1),

    -- Stockholm
    ('Södra Latin', 2),
    ('Kungsholmens Gymnasium', 2),
    ('Norra Real', 2),

    -- Borås
    ('Borås Gymnasieskola', 3),
    ('Särskilda Gymnasiet i Borås', 3),
    ('Thoren Business School Borås', 3),

    -- Malmö
    ('Malmö Borgarskola', 4),
    ('ProCivitas Privata Gymnasium Malmö', 4),
    ('Malmö Latin', 4);

INSERT INTO course(courseName, courseSchoolId)
VALUES ('kivi', 2);

INSERT INTO course(courseName, courseSchoolId)
VALUES
    -- Hvitfeldtska Gymnasium
    ('Mathematics', 1),
    ('History', 2),
    ('Physics', 3),

    -- Södra Latin
    ('Mathematics', 4),
    ('Philosophy', 5),
    ('Sociology', 6),

    -- Borås Gymnasieskola
    ('Mathematics', 7),
    ('English', 8),
    ('Economics', 9),

    -- Malmö Borgarskola
    ('Geography', 10),
    ('Art', 11),
    ('Music', 12);


INSERT INTO student(studentName, studentBirthday, studentGender, studentCourseId)
VALUES
    -- Student 1, kopplad till Mathematics (courseId = 1)
    ('Alice Andersson', '2004-01-15', 'Female', 1),

    -- Student 2, kopplad till History (courseId = 2)
    ('Bob Berg', '2003-09-10', 'Male', 2),

    -- Student 3, kopplad till Physics (courseId = 3)
    ('Carla Carlsson', '2004-04-22', 'Female', 3),

    -- Student 4, kopplad till Philosophy (courseId = 4)
    ('David Dahl', '2003-11-30', 'Male', 4),

    -- Student 5, kopplad till Sociology (courseId = 5)
    ('Eva Eriksson', '2004-05-11', 'Female', 5),

    -- Student 6, kopplad till English (courseId = 6)
    ('Freddie Frisk', '2004-03-17', 'Male', 6),

    -- Student 7, kopplad till Economics (courseId = 7)
    ('Gina Gustavsson', '2004-02-04', 'Female', 7),

    -- Student 8, kopplad till Geography (courseId = 8)
    ('Hans Håkansson', '2004-07-25', 'Male', 8),

    -- Student 9, kopplad till Art (courseId = 9)
    ('Ingrid Ivarsson', '2003-12-08', 'Female', 9),

    -- Student 10, kopplad till Music (courseId = 10)
    ('Jack Johansson', '2004-06-13', 'Male', 10);


INSERT INTO exam (examName, examRating, examStudentId)
VALUES
    -- Prov för Alice
    ('Mathematics Exam', 20, 1),

    -- Prov för Bob
    ('History Exam', 78, 2),

    -- Prov för Carla
    ('Physics Exam', 88, 3),

    -- Prov för David
    ('Mathematics Exam', 76, 4),

    -- Prov för Eva
    ('Philosophy Exam', 60, 5),

    -- Prov för Freddie
    ('Sociology Exam', 80, 6),

    -- Prov för Gina
    ('Mathematics Exam', 90, 7),

    -- Prov för Hans
    ('English Exam', 85, 8),

    -- Prov för Ingrid
    ('Economics Exam', 91, 9),

    -- Prov för Jack
    ('Geography Exam', 45, 10);


INSERT INTO student(studentName, studentBirthday, studentGender, studentCourseId)
VALUES
    -- Nya elever för Mathematics (courseId = 1)
    ('Lina Larsson', '2005-02-14', 'Female', 1),
    ('Mikael Månsson', '2004-10-10', 'Male', 1),

    -- Nya elever för History (courseId = 2)
    ('Nina Nilsson', '2005-06-18', 'Female', 2),

    -- Nya elever för Philosophy (courseId = 4)
    ('Oscar Olsson', '2004-03-02', 'Male', 4),

    -- Nya elever för Geography (courseId = 8)
    ('Paula Pettersson', '2003-11-22', 'Female', 8),

    -- Nya elever för Music (courseId = 10)
    ('Rickard Rasmusson', '2004-01-29', 'Male', 10),
    ('Sara Svensson', '2005-07-04', 'Female', 10);


INSERT INTO exam (examName, examRating, examStudentId)
VALUES
    -- Prov för Lina och Mikael i Mathematics
    ('Mathematics Exam', 82, 11),
    ('Mathematics Exam', 74, 12),

    -- Prov för Nina i History
    ('History Exam', 76, 13),

    -- Prov för Oscar i Philosophy
    ('Philosophy Exam', 79, 14),

    -- Prov för Paula i Geography
    ('Geography Exam', 84, 15),

    -- Prov för Rickard och Sara i Music
    ('Music Exam', 92, 16),
    ('Music Exam', 50, 17);



-- Antal examiner per skola
SELECT schoolName, COUNT(examId) AS numberOfExams
FROM school
         JOIN course ON school.schoolId = course.courseSchoolId
         JOIN student ON course.courseID = student.studentCourseId
         JOIN exam ON student.studentId = exam.examStudentId
GROUP BY schoolName;


-- Fördelning av betygskategorier
SELECT
    CASE
        WHEN examRating BETWEEN 0 AND 50 THEN '0-50'
        WHEN examRating BETWEEN 51 AND 75 THEN '51-75'
        WHEN examRating BETWEEN 76 AND 100 THEN '76-100'
        END AS ratingCategory,
    COUNT(examId) AS numberOfExams
FROM exam
GROUP BY ratingCategory;


-- Topp 3 elever med högsta genomsnittliga betyg
SELECT studentName, ROUND(AVG(examRating) ,0) AS averageRating
FROM student
         JOIN exam ON student.studentId = exam.examStudentId
GROUP BY studentName
ORDER BY averageRating DESC
LIMIT 3;


-- Högsta och lägsta betyg per kurs
SELECT courseName, MAX(examRating) AS highestRating, MIN(examRating) AS lowestRating
FROM course
         JOIN student ON course.courseID = student.studentCourseId
         JOIN exam ON student.studentId = exam.examStudentId
GROUP BY courseName;

SELECT exam.examRating, student.studentName, course.courseName, city.cityName, school.schoolName
FROM city
         JOIN school ON city.cityId = school.schoolCityId
         LEFT JOIN course ON school.schoolId = course.courseSchoolId
         JOIN student ON course.courseID = student.studentCourseId
         JOIN exam ON student.studentId = exam.examStudentId
GROUP BY city.cityName, school.schoolName, course.courseName, student.studentName, exam.examRating;
