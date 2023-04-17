package com.example.revuproject.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.revuproject.database.entities.ChatEntity;
import com.example.revuproject.database.entities.MeetingEntity;
import com.example.revuproject.database.entities.MessageEntity;
import com.example.revuproject.database.entities.ParticipantEntity;
import com.example.revuproject.database.entities.PlaceEntity;
import com.example.revuproject.database.entities.UserEntity;

@Database(entities = {UserEntity.class, PlaceEntity.class, ChatEntity.class, MeetingEntity.class,
        ParticipantEntity.class, MessageEntity.class},
        version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class DatabaseHelper extends RoomDatabase{
    public static final String DATABASE_NAME = "rendezvous_database";

    public abstract RevuDao revuDao();
}
