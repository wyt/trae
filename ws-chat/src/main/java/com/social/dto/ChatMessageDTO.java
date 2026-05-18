package com.social.dto;

/**
 * 聊天消息传输对象（DTO）
 * 用于在WebSocket和HTTP接口中传递聊天消息
 */
public class ChatMessageDTO {

    /** 发送者用户ID */
    private Long senderId;

    /** 发送者用户名 */
    private String senderName;

    /** 接收者用户ID */
    private Long receiverId;

    /** 接收者用户名 */
    private String receiverName;

    /** 消息内容 */
    private String content;

    /** 消息时间戳 */
    private String timestamp;

    /** 默认构造函数 */
    public ChatMessageDTO() {}

    /**
     * 构造函数（不含时间戳）
     * @param senderId 发送者ID
     * @param senderName 发送者名称
     * @param receiverId 接收者ID
     * @param receiverName 接收者名称
     * @param content 消息内容
     */
    public ChatMessageDTO(Long senderId, String senderName, Long receiverId, 
                          String receiverName, String content) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
