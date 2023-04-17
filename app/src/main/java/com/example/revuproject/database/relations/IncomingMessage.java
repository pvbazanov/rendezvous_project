package com.example.revuproject.database.relations;

public class IncomingMessage extends MessageWithSender{
    public IncomingMessage(MessageWithSender message){
        this.message = message.message;
        this.sender = message.sender;
    }
}
