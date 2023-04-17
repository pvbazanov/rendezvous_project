package com.example.revuproject.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.revuproject.database.entities.ChatEntity;
import com.example.revuproject.database.entities.MeetingEntity;
import com.example.revuproject.database.entities.PlaceEntity;
import com.example.revuproject.database.entities.UserEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class DatabaseManager {
    private DatabaseHelper db;
    private static DatabaseManager instance;
    private static final String TAG = "DATABASE MANAGER";

    public static DatabaseManager getInstance(Context context){
        Log.wtf(TAG,"getInstance");
        if(instance == null){
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseManager(Context context){
        Log.wtf(TAG,"Constructor");
//        db = Room.databaseBuilder(context,
//                        DatabaseHelper.class, DatabaseHelper.DATABASE_NAME)
//                .addCallback(new RoomDatabase.Callback() {
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        Log.d(TAG, "onCreate callback");
//                        Executors.newSingleThreadExecutor().execute(new Runnable() {
//                            @Override
//                            public void run(){ initData(context); }
//                        });
//                    }
//                })
//                .build();
        db = Room.databaseBuilder(context,
                DatabaseHelper.class,
                DatabaseHelper.DATABASE_NAME)
                .createFromAsset("database/rendezvous_database.db")
                .allowMainThreadQueries()
                .build();
    }

    public RevuDao getDao() {
        Log.wtf(TAG,"getDao");
        return db.revuDao();
    }

//    private void initData(Context context) {
//        Log.wtf(TAG, "initData");
//
//        //Users
//        List<UserEntity> users = new ArrayList<>();
//        UserEntity user = new UserEntity();
//        user.userId = 1;
//        user.userLogin = "sha_sha_run";
//        user.userPassword = "1234";
//        user.userName = "Саша Беглова";
//        user.userAvatar = /*"drawable://" + */"example_photo_profile.jpg";
//        user.userBio = "Example of bio#1";
//        users.add(user);
//
//        user = new UserEntity();
//        user.userId = 2;
//        user.userLogin = "vika_a";
//        user.userPassword = "1234";
//        user.userName = "Вика Несчастливая";
//        user.userAvatar = "drawable://" + "example_photo_friend.jpg";
//        user.userBio = "Example of bio#2";
//        users.add(user);
//
//        user = new UserEntity();
//        user.userId = 3;
//        user.userLogin = "metalhead";
//        user.userPassword = "1234";
//        user.userName = "Павел Крик";
//        user.userAvatar = "";
//        user.userBio = "Example of bio#3";
//        users.add(user);
//
//        user = new UserEntity();
//        user.userId = 4;
//        user.userLogin = "zhekeha";
//        user.userPassword = "1234";
//        user.userName = "Ев Гений";
//        user.userAvatar = "";
//        user.userBio = "Example of bio#4";
//        users.add(user);
//        DatabaseManager.getInstance(context).getDao().insertUser(users);
//        Log.wtf(TAG, "Users created");
//
//        //Places
//        List<PlaceEntity> places = new ArrayList<>();
//        PlaceEntity place = new PlaceEntity();
//        place.placeId = 1;
//        place.placeName = "Городская эспланада";
//        place.placeAltName = "Городская эспланада";
//        place.placeBio = "";
//        place.placeAddress = "Городская эспланада, Пермь";
//        place.placeAvatar = "drawable://" + "example_photo_place.jpg";
//        places.add(place);
//        DatabaseManager.getInstance(context).getDao().insertPlace(places);
//        Log.wtf(TAG, "Places created");
//
//        //Chats
//        List<ChatEntity> chats = new ArrayList<>();
//        ChatEntity chat = new ChatEntity();
//        chat.chatId = 1;
//        chat.chatOwnerId = 1;
//        chats.add(chat);
//        DatabaseManager.getInstance(context).getDao().insertChat(chats);
//
//        //Meetings
//        List<MeetingEntity> meetings = new ArrayList<>();
//        MeetingEntity meeting = new MeetingEntity();
//        meeting.meetingId = 1;
//        meeting.meetingName = "Несчастливая встреча";
//        meeting.meetingOwnerId = 2;
//        meeting.meetingPlaceId = 1;
//        meeting.meetingChatId = 1;
//        meeting.meetingStart = "28 Февраля, Вторник, 14:30";
//        meeting.meetingEnd = "28 Февраля, Вторник, 16:00";
//        meetings.add(meeting);
//
//        meeting = new MeetingEntity();
//        meeting.meetingId = 2;
//        meeting.meetingName = "Счастливая встреча";
//        meeting.meetingOwnerId = 2;
//        meeting.meetingPlaceId = 1;
//        meeting.meetingChatId = 1;
//        meeting.meetingStart = "31 Февраля, Пятница, 14:30";
//        meeting.meetingEnd = "31 Февраля, Пятница, 16:00";
//        meetings.add(meeting);
//        DatabaseManager.getInstance(context).getDao().insertMeeting(meetings);
//        Log.wtf(TAG, "Meetings created");
//
//        /*List<MeetingParticipantEntity> mps = new ArrayList<>();
//        MeetingParticipantEntity mp = new MeetingParticipantEntity();
//        mp.mp_meeting_id = 1;
//        mp.mp_participant_id = 1;
//        mps.add(mp);
//        mp = new MeetingParticipantEntity();
//        mp.mp_meeting_id = 1;
//        mp.mp_participant_id = 2;
//        mps.add(mp);
//
//        mp = new MeetingParticipantEntity();
//        mp.mp_meeting_id = 2;
//        mp.mp_participant_id = 1;
//        mps.add(mp);
//        mp = new MeetingParticipantEntity();
//        mp.mp_meeting_id = 2;
//        mp.mp_participant_id = 2;
//        mps.add(mp);
//        DatabaseManager.getInstance(context).getDao().insertParticipant(mps);*/
//    }

    private Date dateFromString(String val){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try{
            return simpleDateFormat.parse(val);
        }catch (ParseException e){
            Log.wtf(TAG, e);
        }
        return null;
    }
}
