package com.social.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 * 对应数据库表chat_messages，存储用户之间的聊天消息
 */
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    /** 消息ID，主键自增 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 发送者用户 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    /** 接收者用户 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    /** 消息内容，最大长度1000 */
    @Column(nullable = false, length = 1000)
    private String content;

    /** 创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** 持久化前自动设置创建时间为当前时间 */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /** 默认构造函数 */
    public ChatMessage() {}

    /**
     * 构造函数
     * @param sender 发送者
     * @param receiver 接收者
     * @param content 消息内容
     */
    public ChatMessage(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
