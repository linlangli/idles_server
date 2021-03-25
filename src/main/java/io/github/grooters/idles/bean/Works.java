package io.github.grooters.idles.bean;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="works")
public class Works {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    private int code;

    @NotNull
    private String worksNumber;

    @NotNull
    private String worksName;

    private String[] buyerNumber;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @NotNull
    private String description;

    @NotNull
    private String sellerNumber;

    private String location;

    @NotNull
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getWorksNumber() {
        return worksNumber;
    }

    public void setWorksNumber(String worksNumber) {
        this.worksNumber = worksNumber;
    }

    public String getWorksName() {
        return worksName;
    }

    public void setWorksName(String worksName) {
        this.worksName = worksName;
    }

    public String[] getBuyerNumber() {
        return buyerNumber;
    }

    public void setBuyerNumber(String[] buyerNumber) {
        this.buyerNumber = buyerNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerNumber() {
        return sellerNumber;
    }

    public void setSellerNumber(String sellerNumber) {
        this.sellerNumber = sellerNumber;
    }
}
