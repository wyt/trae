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

    /**
     * 关注用户
     * 校验规则：不能关注自己、用户必须存在、不能重复关注
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @throws RuntimeException 关注校验失败时抛出异常
     */
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

    /**
     * 取消关注用户
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     */
    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    /**
     * 获取指定用户关注的所有人
     * @param userId 用户ID
     * @return 该用户关注的用户列表
     */
    public List<User> getFollowing(Long userId) {
        return followRepository.findFollowingByFollowerId(userId);
    }

    /**
     * 获取关注指定用户的所有人（粉丝列表）
     * @param userId 用户ID
     * @return 关注该用户的粉丝列表
     */
    public List<User> getFollowers(Long userId) {
        return followRepository.findFollowersByFollowingId(userId);
    }

    /**
     * 检查是否已关注某个用户
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return true表示已关注，false表示未关注
     */
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    /**
     * 检查两个用户是否互相关注
     * 用于判断是否可以开启聊天
     * @param userId1 用户1的ID
     * @param userId2 用户2的ID
     * @return true表示互相关注，false表示未互相关注
     */
    public boolean isMutualFollow(Long userId1, Long userId2) {
        return followRepository.existsByFollowerIdAndFollowingId(userId1, userId2) &&
               followRepository.existsByFollowerIdAndFollowingId(userId2, userId1);
    }
}
