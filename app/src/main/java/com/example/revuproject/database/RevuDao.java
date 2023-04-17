package com.example.revuproject.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.revuproject.database.entities.ChatEntity;
import com.example.revuproject.database.entities.MeetingEntity;
import com.example.revuproject.database.entities.MessageEntity;
import com.example.revuproject.database.entities.PlaceEntity;
import com.example.revuproject.database.entities.UserEntity;
import com.example.revuproject.database.relations.ChatWithMeeting;
import com.example.revuproject.database.relations.IncomingMessage;
import com.example.revuproject.database.relations.MeetingFullData;
import com.example.revuproject.database.entities.ParticipantEntity;
import com.example.revuproject.database.relations.MessageWithSender;
import com.example.revuproject.database.relations.OutgoingMessage;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RevuDao {

    //User
    @Query("SELECT * FROM 'user' WHERE user_id != :userId")
    LiveData<List<UserEntity>> getAllUsers(String userId);
    @Transaction
    @Query("select * from 'user' WHERE user_id in (:usersId)")
    LiveData<List<UserEntity>> getSpecUsers(List<String> usersId);
    @Transaction
    @Query("SELECT * FROM 'user' WHERE user_id = :userId")
    LiveData<UserEntity> getUser(String userId);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(List<UserEntity> data);
    @Delete
    void delete(UserEntity data);

    //Place
    @Query("SELECT * FROM 'place'")
    LiveData<List<PlaceEntity>> getAllPlaces();
    @Query("select place_id from 'place'")
    LiveData<List<String>> getAllPlacesId();
    @Transaction
    @Query("SELECT * FROM 'place' WHERE place_id = :placeId")
    LiveData<PlaceEntity> getPlace(int placeId);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlace(List<PlaceEntity> data);
    @Delete
    void delete(PlaceEntity data);

    //Chat
    @Query("SELECT * FROM 'chat'")
    LiveData<List<ChatEntity>> getAllChats();
    @Transaction
    @Query("SELECT * FROM 'chat' WHERE chat_id = :chatId")
    LiveData<ChatEntity> getChat(int chatId);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertChat(List<ChatEntity> data);
    @Delete
    void delete(ChatEntity data);

    @Transaction
    @Query("select * from 'chat' where chat_id = :chatId")
    LiveData<ChatWithMeeting> getCatWithMeeting(int chatId);
    @Transaction
    @Query("select * from 'chat'")
    LiveData<List<ChatWithMeeting>> getChatsWithMeetings();

    //Meeting
    @Query("SELECT * FROM 'meeting'")
    LiveData<List<MeetingEntity>> getAllMeetings();
    @Transaction
    @Query("SELECT * FROM 'meeting' WHERE meeting_id = :meetingId")
    LiveData<MeetingEntity> getMeeting(int meetingId);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMeeting(List<MeetingEntity> data);
    @Delete
    void delete(MeetingEntity data);

    //MeetingFullData не забудь про transaction
    @Transaction
    @Query("SELECT * FROM 'meeting'")
    LiveData<List<MeetingFullData>> getAllMeetingsFullData();
    @Transaction
    @Query("SELECT * FROM 'meeting' WHERE meeting_id = :meeting_id")
    LiveData<MeetingFullData> getMeetingFullData(int meeting_id);
    @Transaction
    default void insert(MeetingFullData meetingFullData) {
        List<MeetingEntity> meeting = new ArrayList<>();
        meeting.add(meetingFullData.meeting);
        insertMeeting(meeting);

        insertParticipant(meetingFullData.meetingParticipants);
    }

    //Participant
    @Query("SELECT * FROM 'participant'")
    LiveData<List<ParticipantEntity>> getAllParticipants();
    @Query("select * from 'participant' where p_meeting_id = :meetingId")
    LiveData<List<ParticipantEntity>> getMeetingParticipants(int meetingId);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertParticipant(List<ParticipantEntity> data);
    @Delete
    void delete(ParticipantEntity data);

    //Ultimate queries
    @Transaction
    @Query("select * from 'meeting'")
    List<MeetingFullData> _getMeetingsFullData();
    @Query("select * from 'user' where user_id in (:usersId)")
    List<UserEntity> _getSpecificUsers(List<String> usersId);
    @Query("select * from 'user' where user_id == :userId")
    UserEntity _getUser(int userId);

    //Message
    @Transaction
    @Query("SELECT * FROM 'message' ORDER BY message_id DESC LIMIT 1;")
    MessageWithSender getTotalLastMessage();

    @Transaction
    @Query("SELECT * FROM 'message' where owner_chat_id = :chatId ORDER BY send_time DESC LIMIT 1;")
    MessageWithSender getLastMessage(String chatId);

    @Transaction
    @Query("select * from 'message' where owner_chat_id = :chatId ORDER BY send_time ASC")
    LiveData<List<MessageWithSender>> getChatMessages(String chatId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMessage(MessageEntity data);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMessages(List<MessageEntity> data);

    @Delete
    void delete(MessageEntity data);

    @Transaction
    default void insert(List<MessageWithSender> messages){
        List<MessageEntity> _messages = new ArrayList<>();
        for (MessageWithSender message:
             messages) {
            _messages.add(message.message);
        }
        insertMessages(_messages);
    }
}
