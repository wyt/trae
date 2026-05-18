package com.social.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置类
 * 配置WebSocket消息代理、端点注册等功能，用于实现实时聊天功能
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 配置消息代理
     * @param config 消息代理注册表
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简单的消息代理，/topic用于广播，/queue用于点对点
        config.enableSimpleBroker("/topic", "/queue");
        // 设置应用程序目标前缀，客户端发送到/app开头的目的地会路由到@MessageMapping注解的方法
        config.setApplicationDestinationPrefixes("/app");
        // 设置用户目标前缀，用于点对点消息发送
        config.setUserDestinationPrefix("/user");
    }

    /**
     * 注册STOMP端点
     * 客户端通过这些端点连接到WebSocket服务
     * @param registry STOMP端点注册表
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册WebSocket端点，支持SockJS，并允许跨域
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
