package org.example;

import jakarta.persistence.*;
import org.w3c.dom.ls.LSOutput;

@Entity
@Table(name = "city", schema = "education")
public class City {
    @Id
    @Column(name = "cityId", nullable = false)
    private int cityId;

    @Column(name = "cityName")
    private String cityName;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }
}
