package com.example.revuproject.database.relations;

public class OutgoingMessage extends MessageWithSender{
    public OutgoingMessage(MessageWithSender message){
        this.message = message.message;
        this.sender = message.sender;
    }
}
