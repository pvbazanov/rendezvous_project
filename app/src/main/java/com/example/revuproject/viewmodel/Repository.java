package com.example.revuproject.viewmodel;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.revuproject.MeetingDefaultAvatar;
import com.example.revuproject.RevUtil;
import com.example.revuproject.database.Converters;
import com.example.revuproject.database.DatabaseManager;
import com.example.revuproject.database.RevuDao;
import com.example.revuproject.database.entities.ChatEntity;
import com.example.revuproject.database.entities.MeetingEntity;
import com.example.revuproject.database.entities.MessageEntity;
import com.example.revuproject.database.entities.ParticipantEntity;
import com.example.revuproject.database.entities.PlaceEntity;
import com.example.revuproject.database.entities.UserEntity;
import com.example.revuproject.database.relations.ChatPreview;
import com.example.revuproject.database.relations.ChatWithMeeting;
import com.example.revuproject.database.relations.IncomingMessage;
import com.example.revuproject.database.relations.MeetingFullData;
import com.example.revuproject.database.relations.MeetingWithParticipants;
import com.example.revuproject.database.relations.MessageWithSender;
import com.example.revuproject.database.relations.OutgoingMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Repository {
    RevUtil util = new RevUtil();
    private final static String TAG = "Repository";
    private DatabaseManager databaseManager;
    private RevuDao dao;
    private MutableLiveData<String> _authUserId = new MutableLiveData<>("1");
    private DatabaseReference firebase, messagesRef, meetingsRef, usersRef, placesRef, chatsRef, mParticipantsRef;
    ValueEventListener messagesListener, meetingsListener, usersListener, placesListener, chatsListener, mParticipantsListener;

    public Repository(Context context){
        Log.wtf("Repository", "Constructor");
        databaseManager = DatabaseManager.getInstance(context);
        dao = databaseManager.getDao();

        firebaseReferenceInit();
        firebaseUsersListenerInit();
//        try {
//            TimeUnit.MILLISECONDS.sleep(200);
//        } catch (InterruptedException e){}
        firebasePlacesListenerInit();
//        try {
//            TimeUnit.MILLISECONDS.sleep(200);
//        } catch (InterruptedException e){}
        firebaseChatsListenerInit();
//        try {
//            TimeUnit.MILLISECONDS.sleep(1000);
//        } catch (InterruptedException e){}
        firebaseMeetingsListenerInit();
//        try {
//            TimeUnit.MILLISECONDS.sleep(200);
//        } catch (InterruptedException e){}
        firebaseMParticipantsListenerInit();
//        try {
//            TimeUnit.MILLISECONDS.sleep(200);
//        } catch (InterruptedException e){}
        firebaseMessagesListenerInit();
    }

    //Firebase references initialization
    private void firebaseReferenceInit(){
        firebase = FirebaseDatabase.getInstance().getReference();
        messagesRef = firebase.child("messages");
        meetingsRef = firebase.child("meetings");
        usersRef = firebase.child("users") ;
        placesRef = firebase.child("places");
        chatsRef = firebase.child("chats");
        mParticipantsRef = firebase.child("meetingsParticipants");
    }

    //Firebase listeners initialization
    private void firebaseUsersListenerInit(){
        usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserEntity> snapUsers = new ArrayList<>();
                for (DataSnapshot snap:
                        snapshot.getChildren()) {
                    snapUsers.add(snap.getValue(UserEntity.class));
                }
                dao.insertUser(snapUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf(TAG, "Error while trying to listen users: " + error.toString());
            }
        };
        usersRef.addValueEventListener(usersListener);
    }

    private void firebasePlacesListenerInit(){
        placesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<PlaceEntity> snapPlaces = new ArrayList<>();
                for (DataSnapshot snap:
                        snapshot.getChildren()) {
                    snapPlaces.add(snap.getValue(PlaceEntity.class));
                }
                dao.insertPlace(snapPlaces);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf(TAG, "Error while trying to listen places: " + error.toString());
            }
        };
        placesRef.addValueEventListener(placesListener);
    }

    private void firebaseChatsListenerInit(){
        chatsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ChatEntity> snapChats = new ArrayList<>();
                for (DataSnapshot snap:
                        snapshot.getChildren()) {
                    snapChats.add(snap.getValue(ChatEntity.class));
                }
                try{
                dao.insertChat(snapChats);
                }catch (SQLiteConstraintException e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf(TAG, "Error while trying to listen chats: " + error.toString());
            }
        };
        chatsRef.addValueEventListener(chatsListener);
    }

    private void firebaseMeetingsListenerInit(){
        meetingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MeetingEntity> snapMeetings = new ArrayList<>();
                for (DataSnapshot snap:
                        snapshot.getChildren()) {
                    snapMeetings.add(snap.getValue(MeetingEntity.class));
                }
                dao.insertMeeting(snapMeetings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf(TAG, "Error while trying to listen meetings: " + error.toString());
            }
        };
        meetingsRef.addValueEventListener(meetingsListener);
    }

    private void firebaseMParticipantsListenerInit(){
        mParticipantsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ParticipantEntity> snapMParticipants = new ArrayList<>();
                for (DataSnapshot snap:
                        snapshot.getChildren()) {
                    snapMParticipants.add(snap.getValue(ParticipantEntity.class));
                }
                dao.insertParticipant(snapMParticipants);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf(TAG, "Error while trying to listen meetings participants: " + error.toString());
            }
        };
        mParticipantsRef.addValueEventListener(mParticipantsListener);
    }

    private void firebaseMessagesListenerInit(){
        messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MessageEntity> snapMessages = new ArrayList<>();
                for (DataSnapshot snap:
                        snapshot.getChildren()) {
                    snapMessages.add(snap.getValue(MessageEntity.class));
                }
                dao.insertMessages(snapMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf(TAG, "Error while trying to listen messages: " + error.toString());
            }
        };
        messagesRef.addValueEventListener(messagesListener);
    }

    //Queries
    public LiveData<String> getUserId(){
        return _authUserId;
    }
    public void setUserId(String userId){
        _authUserId.setValue(userId);
        _authUserId = _authUserId;
    }

    public LiveData<UserEntity> getUser(String userID){
        return dao.getUser(userID);
    }

    public LiveData<List<UserEntity>> getAllUsers(String userId){
        return dao.getAllUsers(userId);
    }

    public List<UserEntity> _getSpecificUsers(List<String> usersId){
        return dao._getSpecificUsers(usersId);
    }

    public LiveData<List<String>> getAllPlacesId(){
        return dao.getAllPlacesId();
    }

    public LiveData<List<MeetingWithParticipants>> getUltimateMeetingsData(){
        List<MeetingWithParticipants> _meetings = new ArrayList<>();
        LiveData<List<MeetingWithParticipants>> meetings;

        List<MeetingFullData> liteMeetings = dao._getMeetingsFullData();

        while (liteMeetings == null) {
            Log.wtf(TAG, "Processing query");
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Log.w(TAG, "InterruptedException thrown");
            }
        }

        List<UserEntity> participants;
        for (MeetingFullData meeting:
             liteMeetings) {
            participants = _getSpecificUsers(meeting.getParticipantIds());
            _meetings.add(new MeetingWithParticipants(meeting, participants));
        }

        meetings = new MutableLiveData<>(_meetings);
        return meetings;
    }

    public LiveData<List<MeetingWithParticipants>> getSortedMeetings(List<MeetingWithParticipants> unsortedMeetings){
        List<MeetingWithParticipants> sortedMeetings = new ArrayList<>();
        for (MeetingWithParticipants meeting:
             unsortedMeetings) {
            List<String> participantsId = new ArrayList<>();
            for (UserEntity user:
                 meeting.getParticipants()) {
                participantsId.add(user.userId);
            }
            if(participantsId.contains(_authUserId.getValue()))
                sortedMeetings.add(meeting);
        }
        return new MutableLiveData<>(sortedMeetings);
    }

    public LiveData<List<ChatWithMeeting>> getChatsWithMeetings(){
        return dao.getChatsWithMeetings();
    }

    public LiveData<List<ChatWithMeeting>> getSortedChatsWithMeetings(List<ChatWithMeeting> chatsUnsorted,
                                                                 List<MeetingWithParticipants> meetingsSorted){
        List<ChatWithMeeting> chatsSorted = new ArrayList<>();
        List<String> chatsIdSorted = new ArrayList<>();
        //
        if(meetingsSorted != null){
            for (MeetingWithParticipants meeting:
                    meetingsSorted) {
                List<String> participantsId = new ArrayList<>();
                for (UserEntity user:
                        meeting.getParticipants()) {
                    participantsId.add(user.userId);
                }
                if(participantsId.contains(_authUserId.getValue()))
                    chatsIdSorted.add(meeting.getMeetingChatId());
            }
            //
            for (ChatWithMeeting chat:
                 chatsUnsorted) {
                if(chatsIdSorted.contains(chat.chat.chatId))
                    chatsSorted.add(chat);
            }
        }
        return new MutableLiveData<>(chatsSorted);
    }

    public LiveData<List<ChatPreview>> getChatsPreviews(List<ChatWithMeeting> chats){
        List<ChatPreview> chatPreviews = new ArrayList<>();
        for (ChatWithMeeting chat:
             chats) {
            chatPreviews.add(new ChatPreview(chat,
                    dao.getLastMessage(chat.chat.chatId)));
        }
        return new MutableLiveData<>(chatPreviews);
    }

    public LiveData<List<MessageWithSender>> getChatMessages(String chatId){
        return dao.getChatMessages(chatId);
    }

    public LiveData<List<MessageWithSender>> divideMessages(List<MessageWithSender> messages, String userId){
        List<MessageWithSender> _messages = new ArrayList<>();

        for (MessageWithSender message:
             messages) {
            if (message.senderId().equals(userId))
                _messages.add(new OutgoingMessage(message));
            else
                _messages.add(new IncomingMessage(message));
        }
        return new MutableLiveData<>(_messages);
    }

    public void sendMessage(String messageText, String chatId, String userId){
        DatabaseReference newMsgRef = messagesRef.push();
        String id = newMsgRef.getKey();
        Date dateTime = Calendar.getInstance().getTime();
        MessageEntity message = new MessageEntity(id, chatId, userId, dateTime, messageText);
        newMsgRef.setValue(message);
    }

    public void addMeeting(String meetingName, String meetingOwnerId, String meetingPlaceId,
                           String meetingStart, String meetingEnd,
                           List<String> participants){
        //TODO: move to separate fun
        //New chat sending
        DatabaseReference newChatRef = chatsRef.push();
        String chatId = newChatRef.getKey();
        ChatEntity newChat = new ChatEntity(chatId, meetingOwnerId);
        newChatRef.setValue(newChat);
        //TODO: move to separate fun
        //New meeting construction
        DatabaseReference newMeetingRef = meetingsRef.push();
        String newMeetingId = newMeetingRef.getKey();
            //TODO: move to separate fun
            //parsing time start
        Date dateTimeStart;
        try {
            dateTimeStart = util.sdf.parse(meetingStart);
        } catch (ParseException e) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 7);
            dateTimeStart = c.getTime();
        }
        //TODO: move to separate fun, same as new meeting construction
            //Meeting avatar generation
        String meetingAvatar = MeetingDefaultAvatar.getRandom();
        //New meeting sending
        MeetingEntity newMeeting = new MeetingEntity(newMeetingId, meetingPlaceId, meetingName, chatId,
                meetingOwnerId, dateTimeStart, meetingEnd, meetingAvatar);
        newMeetingRef.setValue(newMeeting);

        //TODO: move to separate fun
        //Participants sending
        ParticipantEntity newParticipant;
        for (String userId:
             participants) {
            newParticipant = new ParticipantEntity(userId, newMeetingId);
            mParticipantsRef.push().setValue(newParticipant);
        }
    }

    public void addUser(String name, String login, String password, String bio, String avatar){
        DatabaseReference newUserRef = usersRef.push();
        String id = newUserRef.getKey();

        UserEntity newUser = new UserEntity(id, login, password, name, avatar, bio);
        newUserRef.setValue(newUser);
    }

}
