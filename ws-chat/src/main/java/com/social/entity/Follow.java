package com.social.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 关注关系实体类
 * 对应数据库表follows，存储用户之间的关注关系
 * 注意：一个用户不能重复关注同一个用户（唯一约束）
 */
@Entity
@Table(name = "follows", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"follower_id", "following_id"})
})
public class Follow {

    /** 关注关系ID，主键自增 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 关注者用户 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    /** 被关注者用户 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    /** 关注时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** 持久化前自动设置关注时间为当前时间 */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /** 默认构造函数 */
    public Follow() {}

    /**
     * 构造函数
     * @param follower 关注者
     * @param following 被关注者
     */
    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
