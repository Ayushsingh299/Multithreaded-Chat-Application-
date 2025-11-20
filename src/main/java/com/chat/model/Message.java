package com.chat.model;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private int senderId;
    private Integer recipientId; // null for broadcast
    private String content;
    private Timestamp sentAt;

    public Message() {}

    public Message(int senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
