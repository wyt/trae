package com.social.service;

import com.social.entity.Follow;
import com.social.entity.User;
import com.social.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserService userService;

    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new RuntimeException("不能关注自己");
        }
        User follower = userService.findById(followerId);
        User following = userService.findById(followingId);
        if (follower == null || following == null) {
            throw new RuntimeException("用户不存在");
        }
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new RuntimeException("已经关注该用户");
        }
        Follow follow = new Follow(follower, following);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    public List<User> getFollowing(Long userId) {
        return followRepository.findFollowingByFollowerId(userId);
    }

    public List<User> getFollowers(Long userId) {
        return followRepository.findFollowersByFollowingId(userId);
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    public boolean isMutualFollow(Long userId1, Long userId2) {
        return followRepository.existsByFollowerIdAndFollowingId(userId1, userId2) &&
               followRepository.existsByFollowerIdAndFollowingId(userId2, userId1);
    }
}
