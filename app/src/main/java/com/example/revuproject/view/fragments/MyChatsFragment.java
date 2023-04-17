package com.example.revuproject.view.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.revuproject.FragmentTypes;
import com.example.revuproject.R;
import com.example.revuproject.database.relations.ChatPreview;
import com.example.revuproject.database.relations.ChatWithMeeting;
import com.example.revuproject.view.MainActivity;
import com.example.revuproject.view.MyChatsItemAdapter;
import com.example.revuproject.viewmodel.SharedViewModel;

public class MyChatsFragment extends Fragment implements  MyChatsItemAdapter.OnItemClick{

    Context context;
    Context applicationContext;
    RecyclerView recyclerMyChats;
    MyChatsItemAdapter myChatsAdapter;
    SharedViewModel viewModel;
    private final static String TAG = "MyChatsFragment";

    public MyChatsFragment() {
    }

    public static MyChatsFragment newInstance() {
        return new MyChatsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationContext = getActivity().getApplicationContext();
        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        viewModel.onCall(FragmentTypes.MY_CHATS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myChatsFragment = inflater
                .inflate(R.layout.fragment_my_chats, container, false);
        context = myChatsFragment.getContext();

        myChatsAdapter = new MyChatsItemAdapter(this::onClick);

        recyclerMyChats = myChatsFragment.findViewById(R.id.my_chats_recycler);
        recyclerMyChats.setLayoutManager(new LinearLayoutManager(context));
        recyclerMyChats.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerMyChats.setAdapter(myChatsAdapter);

        viewModel.myChatsPreviews.observe(getViewLifecycleOwner(), myChatsPreviews ->{
            myChatsAdapter.setDataList(myChatsPreviews);
        });

        return myChatsFragment;
    }

    @Override
    public void onClick(ChatPreview data) {
        viewModel.setSelectedChat(data.chat);

        ChatFragment chatFragment = new ChatFragment();
        try {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(MainActivity.fragmentContainerID, chatFragment)
                    .commit();
        }catch (NullPointerException e){
            String msg = "getActivity().getSupportFragmentManager() returns null!";
            Log.wtf(TAG, msg);
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show();
        }
    }
}