package com.example.revuproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.revuproject.FragmentTypes;
import com.example.revuproject.R;
import com.example.revuproject.RevUtil;
import com.example.revuproject.UserDefaultAvatar;
import com.example.revuproject.database.entities.UserEntity;
import com.example.revuproject.view.fragments.MapFragment;
import com.example.revuproject.view.fragments.MyMeetingsFragment;
import com.example.revuproject.view.fragments.MyChatsFragment;
import com.example.revuproject.view.fragments.SettingsFragment;
import com.example.revuproject.viewmodel.SharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static Integer fragmentContainerID = R.id.fragment_container;
    private FragmentTransaction transaction;
    SharedViewModel sharedViewModel;
    Context context;

    private Button mapActivityButton;
    private Button chatsActivityButton;
    private Button meetingsActivityButton;
    private Button settingsActivityButton;

    private BottomSheetDialog bottomSheetDialog;
    List<UserEntity> usersList = new ArrayList<>();
    List<String> namesList = new ArrayList<>();

    private TextView appHeader;

    RevUtil util = new RevUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        mapActivityButton = findViewById(R.id.button_map);
        chatsActivityButton = findViewById(R.id.button_chats);
        meetingsActivityButton = findViewById(R.id.button_meetings);
        settingsActivityButton = findViewById(R.id.button_settings);

        appHeader = findViewById(R.id.app_header);

        MapFragment mapFragment = new MapFragment();
        MyChatsFragment chatsFragment = new MyChatsFragment();
        MyMeetingsFragment myMeetingsFragment = new MyMeetingsFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        sharedViewModel = ViewModelProviders.of(this, getDefaultViewModelProviderFactory()).get(SharedViewModel.class);
        switchFragment(mapFragment);

        mapActivityButton.setOnClickListener(v -> switchFragment(mapFragment));
        chatsActivityButton.setOnClickListener(v -> switchFragment(chatsFragment));
        meetingsActivityButton.setOnClickListener(v -> switchFragment(myMeetingsFragment));
        settingsActivityButton.setOnClickListener(v -> switchFragment(settingsFragment));

        initBottomSheet();
        showLoginSheet();
    }

    private void switchFragment(Fragment target){
        changeHeader(target);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainerID, target);
        transaction.commit();
    }

    private void changeHeader(Fragment target){
        switch (target.getClass().getSimpleName()){
            case "MapFragment":
                appHeader.setText("ChillMatch");
                break;
            case "MyChatsFragment":
                appHeader.setText("Мои чаты");
                break;
            case "MyMeetingsFragment":
                appHeader.setText("Мои встречи");
                break;
            case "SettingsFragment":
                appHeader.setText("Мой профиль | Настройки");
                break;
            default:
                appHeader.setText("<Error>");
                break;
        }
    }

    public void showLoginSheet(){
        bottomSheetDialog.show();
    }

    private void initBottomSheet(){
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.dialog_authorization);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.getBehavior().setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        //login
        ListView usersListView = bottomSheetDialog.findViewById(R.id.dialog_authorization_names_list);

        final ArrayAdapter<String> namesAdapter;
        namesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, namesList);
        sharedViewModel.allUsers.observe(this, usersId -> {
            updateNames(usersId);
            namesAdapter.notifyDataSetChanged();
        });
        usersListView.setAdapter(namesAdapter);
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedViewModel.setUserId(usersList.get(position).userId);
                bottomSheetDialog.dismiss();
            }
        });

        //registration
        EditText newUserName = bottomSheetDialog.findViewById(R.id.dialog_authorization_new_name_edit);
        Button addNewUser = bottomSheetDialog.findViewById(R.id.dialog_authorization_reg_button);

        addNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String err_msg = "Не все поля заполнены верно";
                boolean checkNull = true;
                String name = "";
                try{
                name = newUserName.getText().toString();
                } catch (Exception e){
                    checkNull = false;
                }
                String login = "@auto_login";
                String password = "1234";
                String bio = "Auto bio";
                String avatar = UserDefaultAvatar.getRandom();

                if(checkNull && !name.trim().isEmpty()){
                    sharedViewModel.addUser(name, login, password, bio, avatar);
                    newUserName.getText().clear();
                    Toast.makeText(context, "Можете выбрать нового пользователя из списка", Toast.LENGTH_LONG).show();
                } else Toast.makeText(context, err_msg, Toast.LENGTH_LONG).show();
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sharedViewModel.onCall(FragmentTypes.SETTINGS);
            }
        });
    }

    private  void updateNames(List<UserEntity> newData){
        usersList = newData;
        namesList.clear();
        for (UserEntity user:
            usersList) {
            namesList.add(user.userName);
        }
    }
}