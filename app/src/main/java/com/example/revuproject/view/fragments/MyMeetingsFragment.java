package com.example.revuproject.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.revuproject.FragmentTypes;
import com.example.revuproject.R;
import com.example.revuproject.RevUtil;
import com.example.revuproject.database.entities.UserEntity;
import com.example.revuproject.database.relations.MeetingWithParticipants;
import com.example.revuproject.view.MainActivity;
import com.example.revuproject.view.MeetingsItemAdapter;
import com.example.revuproject.viewmodel.SharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyMeetingsFragment extends Fragment implements MeetingsItemAdapter.OnToMapClick, MeetingsItemAdapter.OnToChatClick {
    Context context;
    SharedViewModel viewModel;
    Context applicationContext;

    Button toAddMeetingDialogButton;
    RecyclerView recyclerMeetings;
    MeetingsItemAdapter meetingsAdapter;

    BottomSheetDialog bottomSheetDialog;
    List<String> placesList = new ArrayList<>();
    List<String> userNamesList = new ArrayList<>();
    List<UserEntity> usersList = new ArrayList<>();
    List<UserEntity> usersListOriginal = new ArrayList<>();

    RevUtil util = new RevUtil();
    private final static String TAG = "MeetingsFragment";

    public MyMeetingsFragment() {
        // Required empty public constructor
    }

    public static MyMeetingsFragment newInstance() {
        return new MyMeetingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationContext = getActivity().getApplicationContext();
        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        viewModel.onCall(FragmentTypes.MY_MEETINGS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View meetingFragment = inflater
                .inflate(R.layout.fragment_meetings, container, false);
        context = meetingFragment.getContext();

        toAddMeetingDialogButton = meetingFragment.findViewById(R.id.f_meetings_button_add_meeting);

        meetingsAdapter = new MeetingsItemAdapter(this::onToMapClick, this::onToChatClick);

        recyclerMeetings = meetingFragment.findViewById(R.id.f_meetings_recycler_meetings);
        recyclerMeetings.setLayoutManager(new LinearLayoutManager(context));
        recyclerMeetings.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerMeetings.setAdapter(meetingsAdapter);

        viewModel.meetingsSorted.observe(getViewLifecycleOwner(), meetings -> {
            meetingsAdapter.setDataList(meetings);
            Log.d(TAG, "MeetingsFullData changed");
        });

        bottomSheetDialogInit();
        toAddMeetingDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        return meetingFragment;
    }

    private void bottomSheetDialogInit(){
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.dialog_add_meeting);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.getBehavior().setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        EditText editName = bottomSheetDialog.findViewById(R.id.dialog_add_meeting_edit_name);
        EditText editPlace = bottomSheetDialog.findViewById(R.id.dialog_add_meeting_edit_place);
        ListView placeListView = bottomSheetDialog.findViewById(R.id.dialog_add_meeting_list_places);
        EditText editStart = bottomSheetDialog.findViewById(R.id.dialog_add_meeting_edit_start);
        EditText editParticipants = bottomSheetDialog.findViewById(R.id.dialog_add_meeting_edit_participants);
        ListView userListView = bottomSheetDialog.findViewById(R.id.dialog_add_meeting_list_participants);
        Button addMeetingButton = bottomSheetDialog.findViewById(R.id.dialog_add_meeting_button);

        List<UserEntity> userParticipants = new ArrayList<>();

        //init data
        //TODO: сделать загрузку списка участников

        //places list
        final ArrayAdapter<String> placesAdapter;
        placesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, placesList);
        viewModel.allPlacesId.observe(getViewLifecycleOwner(), placesId -> {
            placesList = placesId;
            placesAdapter.notifyDataSetChanged();
        });
        placeListView.setAdapter(placesAdapter);
        placeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editPlace.setText(placesList.get(position));
            }
        });

        //users list
        final ArrayAdapter<String> usersAdapter;
        usersAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, userNamesList);
        viewModel.allUsers.observe(getViewLifecycleOwner(), usersId -> {
            updateUsersList(usersId);
            usersListOriginal = new ArrayList<>(usersId);
            usersAdapter.notifyDataSetChanged();
        });
        userListView.setAdapter(usersAdapter);
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editParticipants.append(userNamesList.get(position) + ", ");
                userParticipants.add(usersList.get(position));

                usersList.remove(position);
                userNamesList.remove(position);
                usersAdapter.notifyDataSetChanged();
            }
        });

        //saving data on dismiss
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                editParticipants.getText().clear();
                updateUsersList(usersListOriginal);
                usersAdapter.notifyDataSetChanged();
                Toast.makeText(context, usersListOriginal.toString(), Toast.LENGTH_LONG);
            }
        });
        //adding new meeting
        addMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean chekNull = true;
                boolean checkDateFormat;
                boolean checkEmptyFields;
                String name = "";
                String place = "";
                String start = "";
                String end = "";
                String _participants = "";
                List<String> participants = new ArrayList<>();
                String err_msg = "Не все поля заполнены корректно";
                //инит и проверка на нулл
                try {
                    name = editName.getText().toString();
                    place = editPlace.getText().toString();
                    start = editStart.getText().toString();
                    end = "1111";
                    _participants = editParticipants.getText().toString();
                } catch (Exception e) {
                    chekNull = false;
                }
                //проверка даты
                checkDateFormat = util.correctDateFormat(start);
                //проверка участников
                participants = Arrays.asList(_participants.split(", "));
                checkEmptyFields = util.noEmptyFields(name, place, start, end, participants);
                //проверка проверок
                if (chekNull && checkDateFormat && checkEmptyFields){
                    viewModel.addMeeting(name, place, start, end, userParticipants);
                    updateUsersList(usersListOriginal);
                    usersAdapter.notifyDataSetChanged();
                    Toast.makeText(context, usersListOriginal.toString(), Toast.LENGTH_LONG);

                    editName.getText().clear();
                    editPlace.getText().clear();
                    editStart.getText().clear();
                    editParticipants.getText().clear();

                    bottomSheetDialog.dismiss();
                }
                else
                    Toast.makeText(context, err_msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUsersList(List<UserEntity> newData){
        usersList = newData;
        userNamesList.clear();
        for (UserEntity user:
             newData) {
            userNamesList.add(user.userName);
        }
    }

    @Override
    public void onToMapClick(MeetingWithParticipants data){
        viewModel.setSelectedMeeting(data);
        MapFragment mapFragment = new MapFragment();
        try {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(MainActivity.fragmentContainerID, mapFragment)
                    .commit();
        }catch (NullPointerException e){
            String msg = "getActivity().getSupportFragmentManager() returns null!";
            Log.wtf(TAG, msg);
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onToChatClick(MeetingWithParticipants data) {

    }
}