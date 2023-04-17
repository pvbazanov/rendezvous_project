package com.example.revuproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.revuproject.R;
import com.example.revuproject.database.entities.MeetingHeader;
import com.example.revuproject.database.relations.MeetingWithParticipants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeetingsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private  final static int TYPE_ITEM  = 0;
    private final static int TYPE_HEADER = 1;

    private List<MeetingWithParticipants> dataList = new ArrayList<>();
    private final OnToMapClick onToMapClick;
    private final OnToChatClick onToChatClick;

    public MeetingsItemAdapter(OnToMapClick onToMapClick, OnToChatClick onToChatClick) {
        this.onToMapClick = onToMapClick;
        this.onToChatClick = onToChatClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataList(List<MeetingWithParticipants> list){
        this.dataList = list;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final OnToMapClick onToMapClick;
        private final OnToChatClick onToChatClick;
        private final String imgPath;

        private final ImageView meetingAvatar;
        private Button toMapButton, toChatButton;
        private final TextView meetingName, meetingTimeStart, meetingAddress, meetingOrganization;

        public ViewHolder (View itemView, Context context, List<MeetingWithParticipants> dataList,
                           OnToMapClick onToMapClick, OnToChatClick onToChatClick){
            super(itemView);
            this.context = context;
            this.onToMapClick = onToMapClick;
            this.onToChatClick = onToChatClick;
            imgPath = context.getString(R.string.avatarPath);

            meetingAvatar = itemView.findViewById(R.id.item_meeting_avatar);
            meetingName = itemView.findViewById(R.id.item_meeting_name);
            meetingTimeStart = itemView.findViewById(R.id.item_meeting_time_start);
            meetingAddress = itemView.findViewById(R.id.item_meeting_address);
            meetingOrganization = itemView.findViewById(R.id.item_meeting_organization);
            toMapButton = itemView.findViewById(R.id.item_meeting_button_to_map);
            toChatButton = itemView.findViewById(R.id.item_meeting_button_to_chat);

            toMapButton.setOnClickListener(view -> {
                if(onToMapClick != null){
                    int position = getBindingAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                        onToMapClick.onToMapClick(dataList.get(position));
                }
            });

            toChatButton.setOnClickListener(view -> {
                if(onToChatClick != null){
                    int position = getBindingAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                        onToChatClick.onToChatClick(dataList.get(position));
                }
            });
        }

        public void bind(final MeetingWithParticipants meeting){
            meetingName.setText(meeting.meetingName());
            meetingTimeStart.setText(dateTimeFormat(meeting.start()));
            meetingAddress.setText(meeting.address());
            meetingOrganization.setText(meeting.placeAltName());
            setImage(meetingAvatar, Uri.parse(imgPath + meeting.avatar()));
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

        if (viewType == TYPE_ITEM){
            View contactView = inflater.inflate(R.layout.card_meeting, parent, false);
            return new ViewHolder(contactView, context, dataList, onToMapClick, onToChatClick);
        } else if (viewType == TYPE_HEADER) {
            View contactView = inflater.inflate(R.layout.card_meeting, parent, false);
            return new ViewHolder(contactView, context, dataList, onToMapClick, onToChatClick);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public int getItemViewType(int position) {
        Object data = dataList.get(position);
        if (data instanceof MeetingHeader) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MeetingWithParticipants data = dataList.get(position);
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).bind(data);
        } /*else if (viewHolder instanceof ViewHolderHeader){
            ((ViewHolderHeader) viewHolder).bind((ScheduleItemHeader) data);
        }*/
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnToMapClick {
        void onToMapClick(MeetingWithParticipants data);
    }
    public interface OnToChatClick{
        void onToChatClick(MeetingWithParticipants data);
    }
}
