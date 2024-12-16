CREATE DATABASE education;

CREATE TABLE city(
                     cityId INT NOT NULL AUTO_INCREMENT UNIQUE,
                     cityName VARCHAR(255),
                     PRIMARY KEY (cityId)
);

CREATE TABLE school
(
    schoolId       INT NOT NULL AUTO_INCREMENT UNIQUE,
    schoolName     VARCHAR(255),
    schoolCityId INT,
    PRIMARY KEY (schoolId),
    FOREIGN KEY (schoolCityId) REFERENCES city (cityId)
);

CREATE TABLE course (
                        courseID INT NOT NULL AUTO_INCREMENT UNIQUE,
                        courseName VARCHAR(255),
                        courseSchoolId INT,
                        PRIMARY KEY (courseID),
                        FOREIGN KEY (courseSchoolId) REFERENCES school (schoolId)
);

CREATE TABLE student (
                         studentId INT NOT NULL AUTO_INCREMENT,
                         studentName VARCHAR(50),
                         studentBirthday INT,
                         studentGender VARCHAR(50),
                         studentCourseId INT,
                         PRIMARY KEY (studentId),
                         FOREIGN KEY  (studentCourseId) REFERENCES course (courseId)
);
CREATE TABLE exam(
                     examId INT NOT NULL AUTO_INCREMENT,
                     examName VARCHAR (255) NOT NULL,
                     examRating INT NOT NULL,
                     examStudentId INT,
                     PRIMARY KEY (examId),
                     FOREIGN KEY (examStudentId) REFERENCES student (studentId)
);
