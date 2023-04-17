package com.example.revuproject.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.revuproject.database.entities.UserEntity;

@Entity(tableName = "chat",
        foreignKeys =
        {@ForeignKey(entity  = UserEntity.class, parentColumns = "user_id", childColumns = "chat_owner_id", onDelete = ForeignKey.NO_ACTION)})
public class ChatEntity {
    @PrimaryKey @ColumnInfo(name = "chat_id")
    @NonNull public String chatId = "";

    @ColumnInfo(name = "chat_owner_id", index = true)
    @NonNull public String chatOwnerId = "";

    public ChatEntity(){}

    @Ignore
    public ChatEntity(String chatId, @NonNull String chatOwnerId) {
        this.chatId = chatId;
        this.chatOwnerId = chatOwnerId;
    }
}
