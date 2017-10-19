package com.example.mohamedashour.mytask;

/**
 * Created by Mohamed Ashour on 12/10/2017.
 */
public class Plans {

    String planName, location, noFriends, money;

    public Plans() {
        this.location = "";
        this.noFriends = "";
        this.money = "";
        this.planName = "";
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getLocation() {
        return location;
    }

    public String getMoney() {
        return money;
    }

    public String getNoFriends() {
        return noFriends;
    }

    public void setLocation(String location) {

        this.location = location;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setNoFriends(String noFriends) {
        this.noFriends = noFriends;
    }
}
