package com.example.revuproject.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.revuproject.database.entities.ParticipantEntity;
import com.example.revuproject.database.entities.UserEntity;

public class ParticipantFullData {
    @Embedded
    public ParticipantEntity participant;

    @Relation(parentColumn = "participant_id", entity = UserEntity.class, entityColumn = "user_id")
    public UserEntity user;

    //Participant
    public String getParticipantId(){
        return participant.participantId;
    }

    public String getPMeetingId(){
        return participant.pMeetingId;
    }

    //User
    public String getParticipantName(){
        return user.userName;
    }

    public String getParticipantLogin(){
        return user.userLogin;
    }

    public String getParticipantBio(){
        return user.userBio;
    }

    public String getParticipantAvatar(){
        return user.userAvatar;
    }

}
