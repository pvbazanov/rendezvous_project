package com.example.revuproject.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "message",
        foreignKeys = {
        @ForeignKey(entity = ChatEntity.class, parentColumns = "chat_id", childColumns = "owner_chat_id"),
        @ForeignKey(entity = UserEntity.class, parentColumns = "user_id", childColumns = "sender_id")})
public class MessageEntity {
    @PrimaryKey @ColumnInfo(name = "message_id")
    @NonNull public String messageId = "";

    @ColumnInfo(name = "owner_chat_id", index = true)
    @NonNull public String ownerChatId = "";

    @ColumnInfo(name = "sender_id", index = true)
    @NonNull  public String senderId = "";

    @ColumnInfo(name = "send_time")
    public Date sendTime;

    @ColumnInfo(name = "text")
    public String text;

    @Ignore
    public MessageEntity(String messageId, String ownerChatId, String senderId, Date sendTime, String text){
        this.messageId = messageId;
        this.senderId = senderId;
        this.ownerChatId = ownerChatId;
        this.sendTime= sendTime;
        this.text = text;
    }

    public MessageEntity(){}
}
