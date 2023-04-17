package com.example.revuproject.database.relations;

import com.example.revuproject.database.entities.ParticipantEntity;
import com.example.revuproject.database.entities.UserEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeetingWithParticipants {
    private MeetingFullData meeting;
    private List<UserEntity> participants;

    //Constructors
    public MeetingWithParticipants(MeetingFullData meeting){
        this.meeting = meeting;
        participants = new ArrayList<>();
    }

    public MeetingWithParticipants(MeetingFullData meeting, List<UserEntity> participants){
        this.meeting = meeting;
        this.participants = participants;
    }

    public MeetingWithParticipants(MeetingWithParticipants meetingWithParticipants){
        this.meeting = meetingWithParticipants.meeting;
        this.participants = meetingWithParticipants.participants;
    }

    //Get set
    public MeetingFullData getMeeting() {
        return meeting;
    }

    public void setMeeting(MeetingFullData meeting) {
        this.meeting = meeting;
    }

    public List<UserEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserEntity> participants) {
        this.participants = participants;
    }

    //Specific parameters
    public String meetingName(){
        return this.meeting.getMeetingName();
    }

    public Date start(){
        return this.meeting.getMeetingStart();
    }

    public String placeName(){
        return this.meeting.getMeetingPlaceName();
    }

    public String address(){
        return this.meeting.getMeetingPlaceAddress();
    }

    public String placeAltName(){
        return this.meeting.getMeetingPlaceAltName();
    }

    public String avatar(){
        return this.meeting.getMeetingPlaceAvatar();
    }

    public List<String> getParticipantIds(){
        return this.meeting.getParticipantIds();
    }

    public String getPlaceCoords(){
        return this.meeting.meetingPlace.placeCoords;
    }

    public String getMeetingChatId(){
        return this.meeting.getMeetingChatId();
    }
}
