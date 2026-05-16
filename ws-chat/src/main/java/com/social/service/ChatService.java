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

    public List<ChatMessage> getConversation(Long userId1, Long userId2) {
        return chatMessageRepository.findConversation(userId1, userId2);
    }
}
