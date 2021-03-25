package io.github.grooters.idles.bean;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int code;

    private String message;

    @NotNull
    private String goodsNumber;

    @NotNull
    private String goodsName;

    @NotNull
    private String sellerNumber;

    private String sellerName;

    // 商品购买的时间
    @NotNull
    private String time;

    @NotNull
    private float price;

    // 卖家最近来查看该商品的时间
    private String comeLatelyTime;

    @NotNull
    private String description;

    @NotNull
    private String titleImage;

    @NotNull
    private String[] introImage;

    private String introVideo;

    private long fansNumber = 0;

    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerNumber() {
        return sellerNumber;
    }

    public void setSellerNumber(String sellerNumber) {
        this.sellerNumber = sellerNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitleImage() { return titleImage; }

    public void setTitleImage(String titleImage) { this.titleImage = titleImage; }

    public String getIntroVideo() { return introVideo; }

    public void setIntroVideo(String introVideo) { this.introVideo = introVideo; }

    public long getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(long fansNumber) {
        this.fansNumber = fansNumber;
    }

    public String[] getIntroImage() { return introImage; }

    public void setIntroImage(String[] introImage) { this.introImage = introImage; }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComeLatelyTime() {
        return comeLatelyTime;
    }

    public void setComeLatelyTime(String comeLatelyTime) {
        this.comeLatelyTime = comeLatelyTime;
    }
}
