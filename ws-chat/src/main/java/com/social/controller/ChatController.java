package com.social.controller;

import com.social.dto.ChatMessageDTO;
import com.social.entity.ChatMessage;
import com.social.entity.User;
import com.social.service.ChatService;
import com.social.service.FollowService;
import com.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private FollowService followService;

    @GetMapping("/chat/{userId}")
    public String chatPage(@PathVariable Long userId, Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        if (!followService.isMutualFollow(currentUser.getId(), userId)) {
            model.addAttribute("error", "只有互相关注的用户才能聊天");
            return "redirect:/home";
        }

        User chatUser = userService.findById(userId);
        List<ChatMessage> messages = chatService.getConversation(currentUser.getId(), userId);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(m -> {
                    ChatMessageDTO dto = new ChatMessageDTO(
                            m.getSender().getId(),
                            m.getSender().getUsername(),
                            m.getReceiver().getId(),
                            m.getReceiver().getUsername(),
                            m.getContent()
                    );
                    dto.setTimestamp(m.getCreatedAt().format(formatter));
                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("chatUser", chatUser);
        model.addAttribute("messages", messageDTOs);
        return "chat";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        try {
            System.out.println("Received WebSocket message: " + message.getContent() + " from " + message.getSenderId() + " to " + message.getReceiverId());
            chatService.saveMessage(message.getSenderId(), message.getReceiverId(), message.getContent());
            User sender = userService.findById(message.getSenderId());
            User receiver = userService.findById(message.getReceiverId());
            message.setSenderName(sender.getUsername());
            message.setReceiverName(receiver.getUsername());
            System.out.println("Message saved and broadcast to topic");
            return message;
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("发送消息失败: " + e.getMessage());
        }
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public ChatMessageDTO sendMessageHttp(@RequestBody ChatMessageDTO message) {
        try {
            System.out.println("Received HTTP message: " + message.getContent() + " from " + message.getSenderId() + " to " + message.getReceiverId());
            chatService.saveMessage(message.getSenderId(), message.getReceiverId(), message.getContent());
            User sender = userService.findById(message.getSenderId());
            User receiver = userService.findById(message.getReceiverId());
            message.setSenderName(sender.getUsername());
            message.setReceiverName(receiver.getUsername());
            
            messagingTemplate.convertAndSend("/topic/messages", message);
            System.out.println("Message sent via HTTP and broadcast to topic");
            return message;
        } catch (Exception e) {
            System.err.println("Error sending HTTP message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("发送消息失败: " + e.getMessage());
        }
    }
}
