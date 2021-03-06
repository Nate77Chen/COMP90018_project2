package com.example.assignment_2.friendlist;

public class Chat {

    private String sender;
    private String receiver;
    private String msg;
    private String username;
    private String image;

    public Chat(String sender, String receiver, String msg, String username, String image) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.username = username;
        this.image = image;
    }
 public Chat()
 {

 }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
