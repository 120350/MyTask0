package com.example.mohamedashour.mytask;

/**
 * Created by Mohamed Ashour on 17/10/2017.
 */
public class toDoListModel {
    private String ID, title;

    public toDoListModel(){
        this.ID = "";
        this.title = "";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
