package com.social.repository;

import com.social.entity.ChatMessage;
import com.social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.sender.id = :senderId AND m.receiver.id = :receiverId) OR " +
           "(m.sender.id = :receiverId AND m.receiver.id = :senderId) " +
           "ORDER BY m.createdAt ASC")
    List<ChatMessage> findConversation(@Param("senderId") Long senderId, 
                                       @Param("receiverId") Long receiverId);
}
