package com.social.repository;

import com.social.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT t FROM Tweet t WHERE t.user.id IN :userIds ORDER BY t.createdAt DESC")
    List<Tweet> findByUserIdInOrderByCreatedAtDesc(@Param("userIds") List<Long> userIds);

    List<Tweet> findAllByOrderByCreatedAtDesc();
}
