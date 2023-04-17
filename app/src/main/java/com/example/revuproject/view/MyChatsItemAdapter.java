package com.example.revuproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.revuproject.R;
import com.example.revuproject.database.relations.ChatPreview;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyChatsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int TYPE_CHAT = 0;

    private List<ChatPreview> dataList = new ArrayList<>();
    private final OnItemClick onItemClick;

    public MyChatsItemAdapter(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataList(List<ChatPreview> list){
        this.dataList = list;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private OnItemClick onItemClick;
        private String imgPath;

        private TextView chatName;
        private TextView lastMessage;
        private TextView lastMessageAuthor;
        private TextView lastMessageTime;
        private ImageView chatAvatar;

        public ViewHolder (View itemView, Context context,
                           OnItemClick onItemClick, List<ChatPreview> dataList){
            super(itemView);
            this.context = context;
            this.onItemClick = onItemClick;
            imgPath = context.getString(R.string.avatarPath);

            chatName = itemView.findViewById(R.id.cc_chat_name);
            lastMessage = itemView.findViewById(R.id.cc_last_msg_text);
            lastMessageAuthor = itemView.findViewById(R.id.cc_last_msg_author);
            lastMessageTime = itemView.findViewById(R.id.inc_msg_time);
            chatAvatar = itemView.findViewById(R.id.chat_avatar);

            itemView.setOnClickListener(view -> {
                if(onItemClick != null){
                    int position = getBindingAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                        onItemClick.onClick(dataList.get(position));
                }
            });
        }

        public void bind (final ChatPreview chatPreview){
            try {chatName.setText(chatPreview.chat.name());
            }catch (NullPointerException e){
                chatName.setText("ERROR_chat_name");
            }
            try {lastMessageAuthor.setText(getShortName(chatPreview.lastMessage.senderName()));
            }catch (NullPointerException e){
                lastMessageAuthor.setText("ERROR_Auth.:");

            }try {lastMessage.setText(chatPreview.lastMessage.text());
            }catch (NullPointerException e){
                lastMessage.setText("ERROR_last_message_in_the_chat.");
            }
            try {lastMessageTime.setText(dateTimeFormat(chatPreview.lastMessage.sendTime()));
            }catch (NullPointerException e){
                lastMessageTime.setText(dateTimeFormat(new Date()));
            }
            try {setImage(chatAvatar, Uri.parse(imgPath + chatPreview.chat.meeting.meetingAvatar));
            }catch (NullPointerException e){
                chatAvatar.setBackgroundColor(1);
            }
        }

        private String getShortName(String fullName){
            String[] split = fullName.split(" ");
            if(split.length > 1)
                return split[0] + " " + split[1].charAt(0) + ".:";
            return fullName;
        }

        private String dateTimeFormat(Date dateTime){
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.getDefault());
            return sdf.format(dateTime);
        }

        private void setImage (ImageView imageView, Uri uri){
            Glide.with(context).load(uri).into(imageView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == TYPE_CHAT){
            View contactView = inflater.inflate(R.layout.card_chat, parent, false);
            return new MyChatsItemAdapter.ViewHolder(contactView, context, onItemClick, dataList);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_CHAT;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ChatPreview data = dataList.get(position);
        if (viewHolder instanceof MyChatsItemAdapter.ViewHolder) {
            ((MyChatsItemAdapter.ViewHolder) viewHolder).bind(data);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClick{
        void onClick(ChatPreview data);
    }
}
