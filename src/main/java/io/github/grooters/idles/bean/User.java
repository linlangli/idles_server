package io.github.grooters.idles.bean;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    private int code;

    private int level = 0;

    private long time = 0;

    private String grade = "";

    private String location;

    private String resume;

    private String university;

    // 买到的东西的序列号
    private String[] myGoodsOrderNumber;

    // 买到的事务的序列号
    private String[] myWorksOrderNumber;

    // 发布的东西的序列号
    private String[] myGoodsPushNumber;

    // 发布的事务的序列号
    private String[] myWorksPushNumber;

    // 收藏的东西的序列号
    private String[] myGoodsCollectionNumber;

    // 收藏的事务的序列号
    private String[] myWorksCollectionNumber;

    // 浏览历史
    private String[] myGoodsHistoryNumber;

    private String[] myWorksHistoryNumber;

    // 关注者的number
    private String[] concernNumber;

    // 关注的number
    private String[] followNumber;

    @NotNull
    private String tokenNumber;

    private String name;

    private String gender;

    @NotNull
    private String userNumber;

    private String avatarUrl;

    @NotNull
    private String password;

    @NotNull
    private String email;

    public User(){}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String[] getMyGoodsOrderNumber() {
        return myGoodsOrderNumber;
    }

    public void setMyGoodsOrderNumber(String[] myGoodsOrderNumber) {
        this.myGoodsOrderNumber = myGoodsOrderNumber;
    }

    public String[] getMyWorksOrderNumber() {
        return myWorksOrderNumber;
    }

    public void setMyWorksOrderNumber(String[] myWorksOrderNumber) {
        this.myWorksOrderNumber = myWorksOrderNumber;
    }

    public String[] getMyGoodsPushNumber() {
        return myGoodsPushNumber;
    }

    public void setMyGoodsPushNumber(String[] myGoodsPushNumber) {
        this.myGoodsPushNumber = myGoodsPushNumber;
    }

    public String[] getMyWorksPushNumber() {
        return myWorksPushNumber;
    }

    public void setMyWorksPushNumber(String[] myWorksPushNumber) {
        this.myWorksPushNumber = myWorksPushNumber;
    }

    public String[] getMyGoodsCollectionNumber() {
        return myGoodsCollectionNumber;
    }

    public void setMyGoodsCollectionNumber(String[] myGoodsCollectionNumber) {
        this.myGoodsCollectionNumber = myGoodsCollectionNumber;
    }

    public String[] getMyWorksCollectionNumber() {
        return myWorksCollectionNumber;
    }

    public void setMyWorksCollectionNumber(String[] myWorksCollectionNumber) {
        this.myWorksCollectionNumber = myWorksCollectionNumber;
    }

    public String[] getMyGoodsHistoryNumber() {
        return myGoodsHistoryNumber;
    }

    public void setMyGoodsHistoryNumber(String[] myGoodsHistoryNumber) {
        this.myGoodsHistoryNumber = myGoodsHistoryNumber;
    }

    public String[] getMyWorksHistoryNumber() {
        return myWorksHistoryNumber;
    }

    public void setMyWorksHistoryNumber(String[] myWorksHistoryNumber) {
        this.myWorksHistoryNumber = myWorksHistoryNumber;
    }

    public String[] getConcernNumber() {
        return concernNumber;
    }

    public void setConcernNumber(String[] concernNumber) {
        this.concernNumber = concernNumber;
    }

    public String[] getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(String[] followNumber) {
        this.followNumber = followNumber;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }
}
