package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "course", schema = "education")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseID", nullable = false)
    private Integer courseId;

    @Column(name = "courseName")
    private String courseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseSchoolId")
    private School courseSchoolId;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer id) {
        this.courseId = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public School getCourseSchoolId() {
        return courseSchoolId;
    }

    public void setCourseSchoolId(School courseSchool) {
        this.courseSchoolId = courseSchool;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseSchoolId=" + courseSchoolId +
                '}';
    }
}
