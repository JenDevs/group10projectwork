package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "school", schema = "education")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schoolId", nullable = false)
    private Integer schoolId;

    @Column(name = "schoolName")
    private String schoolName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolCityId")
    private City schoolCityId;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer id) {
        this.schoolId = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public City getSchoolCityId() {
        return schoolCityId;
    }

    public void setSchoolCityId(City schoolCity) {
        this.schoolCityId = schoolCity;
    }

    @Override
    public String toString() {
        return "School{" +
                "schoolId=" + schoolId +
                ", schoolName='" + schoolName + '\'' +
                ", schoolCityId=" + schoolCityId +
                '}';
    }
}
