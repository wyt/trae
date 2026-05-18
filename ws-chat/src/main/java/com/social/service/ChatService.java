package com.social.service;

import com.social.entity.ChatMessage;
import com.social.entity.User;
import com.social.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    /**
     * 保存聊天消息到数据库
     * 校验规则：只有互相关注的用户之间才能发送消息
     * @param senderId 发送者用户ID
     * @param receiverId 接收者用户ID
     * @param content 消息内容
     * @return 保存后的聊天消息实体
     * @throws RuntimeException 非互相关注或用户不存在时抛出异常
     */
    public ChatMessage saveMessage(Long senderId, Long receiverId, String content) {
        if (!followService.isMutualFollow(senderId, receiverId)) {
            throw new RuntimeException("只有互相关注的用户才能聊天");
        }
        User sender = userService.findById(senderId);
        User receiver = userService.findById(receiverId);
        if (sender == null || receiver == null) {
            throw new RuntimeException("用户不存在");
        }
        ChatMessage message = new ChatMessage(sender, receiver, content);
        return chatMessageRepository.save(message);
    }

    /**
     * 获取两个用户之间的历史聊天记录
     * @param userId1 用户1的ID
     * @param userId2 用户2的ID
     * @return 两个用户之间的所有聊天消息列表，按时间排序
     */
    public List<ChatMessage> getConversation(Long userId1, Long userId2) {
        return chatMessageRepository.findConversation(userId1, userId2);
    }
}
