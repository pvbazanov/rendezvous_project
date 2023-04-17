package com.example.revuproject.view.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.revuproject.FragmentTypes;
import com.example.revuproject.R;
import com.example.revuproject.database.entities.UserEntity;
import com.example.revuproject.view.MainActivity;
import com.example.revuproject.viewmodel.SharedViewModel;

public class SettingsFragment extends Fragment {

    private final static String TAG = "SettingsFragment";
    Context context;
    Context applicationContext;
    String imgPath;
    SharedViewModel viewModel;

    ImageView profileAvatar;
    TextView profileName;
    TextView profileLogin;
    TextView profileAge;
    Button toChangeUserButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationContext = getActivity().getApplicationContext();
        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        viewModel.onCall(FragmentTypes.SETTINGS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        imgPath = context.getString(R.string.avatarPath);

        View settingsFragment = inflater.inflate(R.layout.fragment_settings, container, false);
        profileName = settingsFragment.findViewById(R.id.pc_name);
        profileLogin = settingsFragment.findViewById(R.id.pc_login);
        profileAge = settingsFragment.findViewById(R.id.pc_age);
        profileAvatar = settingsFragment.findViewById(R.id.pc_profile_avatar);
        toChangeUserButton = settingsFragment.findViewById(R.id.settings_change_user_button);

        viewModel.user.observe(getViewLifecycleOwner(), this::updateProfileData);

        toChangeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showLoginSheet();
            }
        });

        return settingsFragment;
    }

    private void updateProfileData(UserEntity user){
        profileName.setText(user.userName);
        profileLogin.setText(user.userLogin);
        profileAge.setText(user.userBio);
        Glide.with(getContext()).load(imgPath + user.userAvatar).into(profileAvatar);
    }
}