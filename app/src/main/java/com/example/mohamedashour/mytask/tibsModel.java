package com.example.mohamedashour.mytask;

/**
 * Created by Mohamed Ashour on 17/10/2017.
 */
public class tibsModel {
    private String tibsID, tibsTitles, tibsImages;

    public tibsModel(){
        this.tibsID = "";
        this.tibsTitles = "";
        this.tibsImages = "";
    }

    public String getTibsID() {
        return tibsID;
    }

    public void setTibsID(String tibsID) {
        this.tibsID = tibsID;
    }

    public String getTibsTitles() {
        return tibsTitles;
    }

    public void setTibsTitles(String tibsTitles) {
        this.tibsTitles = tibsTitles;
    }

    public String getTibsImages() {
        return tibsImages;
    }

    public void setTibsImages(String tibsImages) {
        this.tibsImages = tibsImages;
    }
}
