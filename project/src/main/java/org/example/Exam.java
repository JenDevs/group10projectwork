package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "exam", schema = "education")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examId", nullable = false)
    private Integer examId;

    @Column(name = "examName", nullable = false)
    private String examName;

    @Column(name = "examRating", nullable = false)
    private Integer examRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examStudentId")
    private Student examStudentId;

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer id) {
        this.examId = id;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Integer getExamRating() {
        return examRating;
    }

    public void setExamRating(Integer examRating) {
        this.examRating = examRating;
    }

    public Student getExamStudentId() {
        return examStudentId;
    }

    public void setExamStudentId(Student examStudent) {
        this.examStudentId = examStudent;
    }

}
