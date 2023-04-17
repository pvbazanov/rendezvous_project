package com.example.revuproject.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user", indices = {@Index(value = {"user_login"}, unique = false)})
public class UserEntity {
    @PrimaryKey @ColumnInfo(name = "user_id")
    @NonNull public String userId = "";

    @ColumnInfo(name = "user_login")
    @NonNull public String userLogin="";

    @ColumnInfo(name = "user_password")
    @NonNull public String userPassword="";

    @ColumnInfo(name = "user_name")
    @NonNull public String userName="";

    @ColumnInfo(name = "user_avatar")
    public String userAvatar;

    @ColumnInfo(name = "user_bio")
    public String userBio="";

    public UserEntity(){};

    @Ignore
    public UserEntity(String userId, @NonNull String userLogin, @NonNull String userPassword, @NonNull String userName, String userAvatar, String userBio) {
        this.userId = userId;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.userBio = userBio;
    }
}
