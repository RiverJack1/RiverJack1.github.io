package com.atriver.QQSys;

import java.io.File;
import java.io.Serializable;

/**
 * @anthor river
 * @creat 2022-07-31-15:38
 */
public class Message implements Serializable {
    private static final long seriaVersionUid=2l;
    private MessageStatus messageType;//消息类型
    private String messageContent;//消息内容
    private String sendTime;//发送时间
    private String sender;//发送者
    private String receiver;//接收者
    private File fileContent;//存储发送的文件

    public MessageStatus getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageStatus messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
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

    public Message() {
    }
}
