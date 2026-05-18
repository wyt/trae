package com.social.repository;

import com.social.entity.ChatMessage;
import com.social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 聊天消息数据访问接口
 * 继承JpaRepository获得基础CRUD功能
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 查询两个用户之间的聊天记录
     * 查询用户A发给用户B和用户B发给用户A的所有消息，按时间升序排序
     * @param senderId 用户1的ID
     * @param receiverId 用户2的ID
     * @return 两个用户之间的聊天消息列表
     */
    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.sender.id = :senderId AND m.receiver.id = :receiverId) OR " +
           "(m.sender.id = :receiverId AND m.receiver.id = :senderId) " +
           "ORDER BY m.createdAt ASC")
    List<ChatMessage> findConversation(@Param("senderId") Long senderId, 
                                       @Param("receiverId") Long receiverId);
}
