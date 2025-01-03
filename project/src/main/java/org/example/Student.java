package org.example;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "student", schema = "education")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentId", nullable = false)
    private Integer studentId;

    @Column(name = "studentName", length = 50)
    private String studentName;

    @Column(name = "studentBirthday")
    private LocalDate studentBirthday;

    @Column(name = "studentGender", length = 50)
    private String studentGender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentCourseId")
    private Course studentCourseId;

    @OneToMany(mappedBy = "examStudentId", cascade = CascadeType.REMOVE)
    private List<Exam> exams;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer id) {
        this.studentId = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDate getStudentBirthday() {
        return studentBirthday;
    }

    public void setStudentBirthday(LocalDate studentBirthday) {
        this.studentBirthday = studentBirthday;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public void setStudentGender(String studentGender) {
        this.studentGender = studentGender;
    }

    public Course getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(Course studentCourse) {
        this.studentCourseId = studentCourse;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Birthday: %s, Gender: %s", studentName, studentBirthday, studentGender);
    }
}
