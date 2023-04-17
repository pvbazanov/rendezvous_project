package com.example.revuproject.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.revuproject.database.entities.ChatEntity;
import com.example.revuproject.database.entities.MeetingEntity;

public class ChatWithMeeting {
    @Embedded
    public ChatEntity chat;

    @Relation(parentColumn = "chat_id", entity = MeetingEntity.class, entityColumn = "meeting_chat_id")
    public MeetingEntity meeting;

    //
    public String name(){
        return this.meeting.meetingName;
    }
}
