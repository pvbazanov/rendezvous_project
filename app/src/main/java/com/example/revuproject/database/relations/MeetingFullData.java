package com.example.revuproject.database.relations;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.revuproject.database.entities.MeetingEntity;
import com.example.revuproject.database.entities.ParticipantEntity;
import com.example.revuproject.database.entities.PlaceEntity;
import com.example.revuproject.database.entities.UserEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import com.example.revuproject.database.MeetingParticipantEntity;


public class MeetingFullData {
    @Embedded
    public MeetingEntity meeting;

    @Relation(parentColumn = "meeting_place_id", entity = PlaceEntity.class, entityColumn = "place_id")
    public PlaceEntity meetingPlace;

    @Relation(parentColumn = "meeting_owner_id", entity = UserEntity.class, entityColumn = "user_id")
    public UserEntity meetingOwner;

    @Relation(parentColumn = "meeting_id", entity = ParticipantEntity.class, entityColumn = "p_meeting_id")
    public List<ParticipantEntity> meetingParticipants;

    //public List<UserEntity> participantsData = new ArrayList<>();

    //Meeting
    public String getMeetingId() {
        return meeting.meetingId;
    }

    public String getMeetingName() {
        return meeting.meetingName;
    }

    public Date getMeetingStart() {
        return meeting.meetingStart;
    }

    public String getMeetingEnd() {
        return meeting.meetingEnd;
    }

    //Chat
    public String getMeetingChatId() {
        return meeting.meetingChatId;
    }

    //Owner
    public String getMeetingOwnerId() {
        return meeting.meetingOwnerId;
    }

    //Place
    public String getMeetingPlaceId() {
        return meeting.meetingPlaceId;
    }

    public String getMeetingPlaceName() {
        return meetingPlace.placeName;
    }

    public String getMeetingPlaceAltName() {
        return meetingPlace.placeAltName;
    }

    public String getMeetingPlaceAddress() {
        return meetingPlace.placeAddress;
    }

    public String getMeetingPlaceAvatar() {
        return meetingPlace.placeAvatar;
    }

    //Participant
    public List<String> getParticipantIds(){
        List<String> p_ids = new ArrayList<>();
        for (ParticipantEntity participant: meetingParticipants){
//            p_ids.add(Integer.toString(participant.participantId));
            p_ids.add(participant.participantId);
        }
        Log.i("MeetingFullData", "Participant ID's:" + p_ids);
        return p_ids;
    }
}
