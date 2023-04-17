package com.example.revuproject.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "participant",
        primaryKeys = {"participant_id", "p_meeting_id"},
        foreignKeys = {
        @ForeignKey(entity  = UserEntity.class, parentColumns = "user_id", childColumns = "participant_id"),
                @ForeignKey(entity = MeetingEntity.class, parentColumns = "meeting_id", childColumns = "p_meeting_id")
        })
public class ParticipantEntity {
    @ColumnInfo(name = "participant_id")
    @NonNull public String participantId;

    @ColumnInfo(name = "p_meeting_id", index = true)
    @NonNull public String pMeetingId;

    public ParticipantEntity(){}

    @Ignore
    public ParticipantEntity(@NonNull String participantId, @NonNull String pMeetingId) {
        this.participantId = participantId;
        this.pMeetingId = pMeetingId;
    }
}
