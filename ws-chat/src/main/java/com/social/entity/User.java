package com.social.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表users，存储系统用户信息
 */
@Entity
@Table(name = "users")
public class User {

    /** 用户ID，主键自增 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户名，唯一，长度3-50 */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    @Column(unique = true, nullable = false)
    private String username;

    /** 密码，长度至少6位 */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少6位")
    @Column(nullable = false)
    private String password;

    /** 用户创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** 持久化前自动设置创建时间为当前时间 */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /** 默认构造函数 */
    public User() {}

    /**
     * 构造函数
     * @param username 用户名
     * @param password 密码
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
