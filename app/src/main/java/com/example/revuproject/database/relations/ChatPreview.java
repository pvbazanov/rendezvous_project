package com.example.revuproject.database.relations;

public class ChatPreview {
    public ChatWithMeeting chat;
    public MessageWithSender lastMessage;

    public ChatPreview(ChatWithMeeting chat, MessageWithSender message){
        this.chat = chat;
        this.lastMessage = message;
    }
}
