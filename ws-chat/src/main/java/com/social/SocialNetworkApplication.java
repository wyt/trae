package com.social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 社交网络聊天应用主启动类
 * 基于Spring Boot开发，提供用户注册、登录、关注和实时聊天功能
 */
@SpringBootApplication
public class SocialNetworkApplication {

    /**
     * 应用程序入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkApplication.class, args);
    }
}
