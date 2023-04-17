package com.example.revuproject.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "meeting",
        indices = {@Index(value = {"meeting_place_id", "meeting_chat_id", "meeting_owner_id"})},
        foreignKeys =
        {@ForeignKey(entity  = PlaceEntity.class, parentColumns = "place_id", childColumns = "meeting_place_id"),
                @ForeignKey(entity  = ChatEntity.class, parentColumns = "chat_id", childColumns = "meeting_chat_id"),
                @ForeignKey(entity  = UserEntity.class, parentColumns = "user_id", childColumns = "meeting_owner_id")})
public class MeetingEntity {
    @PrimaryKey @ColumnInfo(name = "meeting_id")
    @NonNull public String meetingId = "";

    @ColumnInfo(name = "meeting_place_id", index = true)
    @NonNull public String meetingPlaceId;

    @ColumnInfo(name = "meeting_name")
    @NonNull public String meetingName = "";

    @ColumnInfo(name = "meeting_chat_id", index = true)
    @NonNull public String meetingChatId = "";

    @ColumnInfo(name = "meeting_owner_id", index = true)
    @NonNull public String meetingOwnerId = "";

    @ColumnInfo(name = "meeting_start")
    @NonNull public Date meetingStart = new Date();

    @ColumnInfo(name = "meeting_end")
    public String meetingEnd;

    @ColumnInfo(name = "meeting_avatar")
    @NonNull public String meetingAvatar = "example_photo_meeting";

    public MeetingEntity(){}

    @Ignore
    public MeetingEntity(String meetingId, String meetingPlaceId, @NonNull String meetingName,
                         String meetingChatId, String meetingOwnerId, @NonNull Date meetingStart,
                         String meetingEnd, @NonNull String meetingAvatar) {
        this.meetingId = meetingId;
        this.meetingPlaceId = meetingPlaceId;
        this.meetingName = meetingName;
        this.meetingChatId = meetingChatId;
        this.meetingOwnerId = meetingOwnerId;
        this.meetingStart = meetingStart;
        this.meetingEnd = meetingEnd;
        this.meetingAvatar = meetingAvatar;
    }
}