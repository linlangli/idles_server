package io.github.grooters.idles.entity;

import java.util.List;

public class Province {

    private String province_name;

    private List<Cities> cities;

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public List<Cities> getCities() {
        return cities;
    }

    public void setCities(List<Cities> cities) {
        this.cities = cities;
    }
}
