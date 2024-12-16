package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "city", schema = "education")
public class City {
    @Id
    @Column(name = "cityId", nullable = false)
    private Integer cityId;

    @Column(name = "cityName")
    private String cityName;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
