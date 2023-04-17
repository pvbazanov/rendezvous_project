package com.example.revuproject.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.revuproject.FragmentTypes;
import com.example.revuproject.R;
import com.example.revuproject.database.relations.ChatWithMeeting;
import com.example.revuproject.database.relations.MessageWithSender;
import com.example.revuproject.view.ChatItemAdapter;
import com.example.revuproject.viewmodel.SharedViewModel;

public class ChatFragment extends Fragment implements ChatItemAdapter.OnItemClick {
    Context context;
    Context applicationContext;
    SharedViewModel viewModel;

    RecyclerView recyclerChat;
    ChatItemAdapter chatAdapter;
    ImageView chatAvatar;
    TextView chatName;

    EditText messageText;
    Button sendButton;

    private final static String TAG = "ChatFragment";

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {
        return new ChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationContext = getActivity().getApplicationContext();
        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        viewModel.onCall(FragmentTypes.CHAT);
    }

    private void setImage (ImageView imageView, Uri uri){
        Glide.with(context).load(uri).into(imageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  chatFragment = inflater.inflate(R.layout.fragment_chat, container, false);
        context = chatFragment.getContext();
        ChatWithMeeting currentChat = viewModel.getSelectedChat();

        chatAvatar = chatFragment.findViewById(R.id.cohb_friend_avatar);
        chatName = chatFragment.findViewById(R.id.cohb_chat_name);
        sendButton = chatFragment.findViewById(R.id.chat_one_send_msg);
        messageText = chatFragment.findViewById(R.id.chat_one_text);

        String imgPath = context.getString(R.string.avatarPath);
        setImage(chatAvatar,
                Uri.parse(imgPath + currentChat.meeting.meetingAvatar));
        chatName.setText("Чат " + currentChat.name());

        chatAdapter = new ChatItemAdapter(this::onClick);

        recyclerChat = chatFragment.findViewById(R.id.chat_recycler);
//        recyclerChat.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        recyclerChat.smoothScrollToPosition(recyclerChat.getAdapter().getItemCount() - 1);
//                    }
//                }
//        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setStackFromEnd(true);
        recyclerChat.setLayoutManager(layoutManager);
        recyclerChat.setAdapter(chatAdapter);

        viewModel.messagesDivided.observe(getViewLifecycleOwner(), sortedMessages ->{
            chatAdapter.setDataList(sortedMessages);
        });

        sendButton.setOnClickListener(view -> {
            if (messageText.getText() != null) {
                viewModel.sendMessage(messageText.getText().toString());
                messageText.getText().clear();

                recyclerChat.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerChat.smoothScrollToPosition(recyclerChat.getAdapter().getItemCount() - 1);
                    }
                }, 500);
            }
        });

        return chatFragment;
    }

    @Override
    public void onClick(MessageWithSender data) {

    }
}