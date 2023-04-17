package com.example.revuproject.database.entities;

import com.example.revuproject.database.relations.MeetingFullData;

public class MeetingHeader extends MeetingFullData {
    private String title;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
}
