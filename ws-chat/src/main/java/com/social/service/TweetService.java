package com.social.service;

import com.social.entity.Tweet;
import com.social.entity.User;
import com.social.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Transactional
    public Tweet createTweet(String content, Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("内容不能为空");
        }
        if (content.length() > 280) {
            throw new RuntimeException("内容不能超过280个字符");
        }
        Tweet tweet = new Tweet(content.trim(), user);
        return tweetRepository.save(tweet);
    }

    public List<Tweet> getUserTweets(Long userId) {
        return tweetRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Tweet> getTimeline(Long userId) {
        List<User> following = followService.getFollowing(userId);
        following.add(userService.findById(userId));
        List<Long> userIds = following.stream()
                .map(User::getId)
                .toList();
        return tweetRepository.findByUserIdInOrderByCreatedAtDesc(userIds);
    }

    public List<Tweet> getAllTweets() {
        return tweetRepository.findAllByOrderByCreatedAtDesc();
    }

    public Tweet getTweetById(Long id) {
        return tweetRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteTweet(Long tweetId, Long userId) {
        Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        if (tweet == null) {
            throw new RuntimeException("推文不存在");
        }
        if (!tweet.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权删除此推文");
        }
        tweetRepository.delete(tweet);
    }
}
