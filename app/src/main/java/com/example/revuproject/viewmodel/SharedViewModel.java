package com.example.revuproject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.revuproject.FragmentTypes;
import com.example.revuproject.database.entities.UserEntity;
import com.example.revuproject.database.relations.ChatPreview;
import com.example.revuproject.database.relations.ChatWithMeeting;
import com.example.revuproject.database.relations.MeetingWithParticipants;
import com.example.revuproject.database.relations.MessageWithSender;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {
    private final static String TAG = "SharedViewModel";
    private Repository repository;
    private FragmentTypes activeFragmentType;
    private DatabaseReference fireBaseRef;
    private DatabaseReference messagesReference;

    //UserID
    public LiveData<String> userId;

    //User
    public LiveData<UserEntity> user;
    //All other users
    public LiveData<List<UserEntity>> allUsers;
    //MyMeetings
    private LiveData<List<MeetingWithParticipants>> meetingsUnsorted;
    public LiveData<List<MeetingWithParticipants>> meetingsSorted;
    //Selected Meeting
    private MeetingWithParticipants selectedMeeting;
    //All places
    public LiveData<List<String>> allPlacesId;
    //MyChats
    public LiveData<List<ChatWithMeeting>> myChatsUnsorted;
    public LiveData<List<ChatWithMeeting>> myChatsSorted;
    //My chats' previews
    public LiveData<List<ChatPreview>> myChatsPreviews;
    //Selected Chat
    private ChatWithMeeting selectedChat;
    //Messages
    public LiveData<List<MessageWithSender>> messagesUnsorted;
    public LiveData<List<MessageWithSender>> messagesDivided;
    //New meeting
    public String editNewMeetingNameUnsaved = "";
    public String editNewMeetingPlaceUnsaved = "";
    public String editNewMeetingStartUnsaved = "";
    public String editNewMeetingParticipantsUnsaved = "";

    public SharedViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        userId = repository.getUserId();
        updateUsersId();
        allPlacesId = repository.getAllPlacesId();

        onCallSettings();
        onCallMyMeetings();
        onCallMyChats();

        Log.i(TAG, "ViewModel created");
    }

    public void setUserId(String authUserId){
        repository.setUserId(authUserId);
        updateUsersId();
    }

    private void updateUsersId(){
        allUsers = repository.getAllUsers(userId.getValue());
    }

    public ChatWithMeeting getSelectedChat(){
        return selectedChat;
    }
    public void setSelectedChat(ChatWithMeeting chat){
        this.selectedChat = chat;
    }

    public MeetingWithParticipants getSelectedMeeting() {
        return selectedMeeting;
    }
    public void setSelectedMeeting(MeetingWithParticipants meeting){
        this.selectedMeeting = meeting;
    }

    public void onCall(FragmentTypes fragmentType){
        activeFragmentType = fragmentType;
        switch (fragmentType){
            case MAP:{
                onCallMap();
                break;
            }
            case MY_MEETINGS:{
                onCallMyMeetings();
                break;
            }
            case MEETING:{
                onCallMeeting();
                break;
            }
            case MY_CHATS:{
                onCallMyChats();
                break;
            }
            case CHAT:{
                onCallChat();
                break;
            }
            case SETTINGS:{
                onCallSettings();
                break;
            }
            default:{
                onCallDefault();
                break;
            }
        }
    }

    private void onCallMap(){
        Log.i(TAG, "SharedViewModel called from Map fragment.");

    }

    private void onCallMyMeetings(){
        Log.i(TAG, "SharedViewModel called from MyMeetings fragment.");
        meetingsUnsorted = repository.getUltimateMeetingsData();
        meetingsSorted = Transformations.switchMap(meetingsUnsorted, meetings ->
                repository.getSortedMeetings(meetings));
    }

    public void addMeeting(String meetingName,String meetingPlaceId,
                           String meetingStart, String meetingEnd,
                           List<UserEntity> participants){
        String meetingOwnerId = userId.getValue();

        List<String> _participants = new ArrayList<>();
        for (UserEntity user:
             participants) {
            _participants.add(user.userId);
        }
        _participants.add(userId.getValue());

        repository.addMeeting(meetingName, meetingOwnerId, meetingPlaceId, meetingStart, meetingEnd, _participants);
    }

    private void onCallMeeting(){
        Log.i(TAG, "SharedViewModel called from Meeting fragment.");
    }

    private void onCallMyChats(){
        Log.i(TAG, "SharedViewModel called from MyChats fragment.");
        myChatsUnsorted = repository.getChatsWithMeetings();
        myChatsSorted = Transformations.switchMap(myChatsUnsorted, myChats ->
                repository.getSortedChatsWithMeetings(myChats, meetingsSorted.getValue()));
        myChatsPreviews = Transformations.switchMap(myChatsSorted, myChats ->
                repository.getChatsPreviews(myChats));
    }

    private void onCallChat(){
        Log.i(TAG, "SharedViewModel called from Chat fragment.");
        messagesUnsorted = repository.getChatMessages(selectedChat.chat.chatId);
        messagesDivided = Transformations.switchMap(messagesUnsorted, messages ->
                repository.divideMessages(messages, userId.getValue()));
    }

    public void sendMessage(String messageText){
        repository.sendMessage(messageText, selectedChat.chat.chatId, userId.getValue());
    }

    private void onCallSettings(){
        Log.i(TAG, "SharedViewModel called from Settings fragment.");
        user = repository.getUser(userId.getValue());
    }

    public void addUser(String name, String login, String password, String bio, String avatar){
        repository.addUser(name, login, password, bio, avatar);
    }

    private void onCallDefault(){
        Log.i(TAG, "I have no idea how you got into on call default case.");
    }

}
