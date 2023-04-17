package com.example.revuproject.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.revuproject.database.entities.MessageEntity;
import com.example.revuproject.database.entities.UserEntity;

import java.util.Date;

public class MessageWithSender {
    @Embedded
    public MessageEntity message;

    @Relation(parentColumn = "sender_id", entity = UserEntity.class, entityColumn = "user_id")
    public UserEntity sender;

    //Message
    public String id(){
        return this.message.messageId;
    }

    public Date sendTime(){
        return this.message.sendTime;
    }

    public String text(){
        return this.message.text;
    }

    //Sender
    public String senderId(){
        return this.message.senderId;
    }

    public String senderName(){
        return this.sender.userName;
    }

    public String senderAvatar(){
        return this.sender.userAvatar;
    }
}
