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
import com.example.revuproject.database.relations.IncomingMessage;
import com.example.revuproject.database.relations.MessageWithSender;
import com.example.revuproject.database.relations.OutgoingMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TYPE_INCOMING = 0;
    private final static int TYPE_OUTGOING = 1;
    private final static int TYPE_TECHNICAL = 2;

    private List<MessageWithSender> dataList = new ArrayList<>();
    private final OnItemClick onItemClick;

    public ChatItemAdapter(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataList(List<MessageWithSender> list){
        this.dataList = list;
        this.notifyDataSetChanged();
    }

    public static class ViewHolderIncoming extends RecyclerView.ViewHolder {
        private Context context;
        private OnItemClick onItemClick;
        private String imgPath;

        TextView incMessageAuthor;
        TextView incMessageTime;
        TextView incMessageText;

        public ViewHolderIncoming(View itemView, Context context,
                          OnItemClick onItemClick, List<MessageWithSender> dataList) {
            super(itemView);
            this.context = context;
            this.onItemClick = onItemClick;
            imgPath = context.getString(R.string.avatarPath);

            incMessageAuthor = itemView.findViewById(R.id.inc_msg_author);
            incMessageTime = itemView.findViewById(R.id.inc_msg_time);
            incMessageText = itemView.findViewById(R.id.inc_msg_text);

            itemView.setOnClickListener(view -> {
                if (onItemClick != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        onItemClick.onClick(dataList.get(position));
                }
            });
        }

        public void bind (final MessageWithSender message){
            incMessageAuthor.setText(message.senderName());
            incMessageTime.setText(dateTimeFormat(message.sendTime()));
            incMessageText.setText(message.text());
        }

        private String dateTimeFormat(Date dateTime){
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.getDefault());
            return sdf.format(dateTime);
        }
    }

    public static class ViewHolderOutgoing extends RecyclerView.ViewHolder {
        private Context context;
        private OnItemClick onItemClick;
        private String imgPath;

        TextView outMessageTime;
        TextView outMessageText;

        public ViewHolderOutgoing(View itemView, Context context,
                          OnItemClick onItemClick, List<MessageWithSender> dataList) {
            super(itemView);
            this.context = context;
            this.onItemClick = onItemClick;
            imgPath = context.getString(R.string.avatarPath);

            outMessageTime = itemView.findViewById(R.id.out_msg_time);
            outMessageText = itemView.findViewById(R.id.out_msg_text);

            itemView.setOnClickListener(view -> {
                if (onItemClick != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        onItemClick.onClick(dataList.get(position));
                }
            });
        }

        public void bind (final MessageWithSender message){
            outMessageTime.setText(dateTimeFormat(message.sendTime()));
            outMessageText.setText(message.text());
        }

        private String dateTimeFormat(Date dateTime){
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.getDefault());
            return sdf.format(dateTime);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == TYPE_INCOMING){
            View contactView = inflater.inflate(R.layout.chat_incoming_msg, parent, false);
            return new ViewHolderIncoming(contactView, context, onItemClick, dataList);
        }
        if(viewType == TYPE_OUTGOING){
            View contactView = inflater.inflate(R.layout.chat_outgoing_msg, parent, false);
            return new ViewHolderOutgoing(contactView, context, onItemClick, dataList);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MessageWithSender data = dataList.get(position);
        if (data instanceof IncomingMessage)
            ((ViewHolderIncoming) viewHolder).bind(data);
        else if (data instanceof OutgoingMessage)
            ((ViewHolderOutgoing) viewHolder).bind(data);
        else throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public int getItemViewType(int position) {
        MessageWithSender item = dataList.get(position);
        if (item instanceof IncomingMessage)
            return TYPE_INCOMING;
        if (item instanceof OutgoingMessage)
            return TYPE_OUTGOING;
        return TYPE_TECHNICAL;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClick{
        void onClick(MessageWithSender data);
    }
}
