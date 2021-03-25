package io.github.grooters.idles.entity;

import java.util.List;

public class Cities {

    private String city_name;

    private List<String> universities;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public List<String> getUniversities() {
        return universities;
    }

    public void setUniversities(List<String> universities) {
        this.universities = universities;
    }
}
