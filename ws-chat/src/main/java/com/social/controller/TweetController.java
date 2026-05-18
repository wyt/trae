package com.social.controller;

import com.social.entity.Tweet;
import com.social.entity.User;
import com.social.service.FollowService;
import com.social.service.TweetService;
import com.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @GetMapping("/timeline")
    public String timeline(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        List<Tweet> timeline = tweetService.getTimeline(currentUser.getId());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("tweets", timeline);
        model.addAttribute("followService", followService);
        return "timeline";
    }

    @GetMapping("/profile")
    public String myProfile(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        List<Tweet> tweets = tweetService.getUserTweets(currentUser.getId());
        List<User> followers = followService.getFollowers(currentUser.getId());
        List<User> following = followService.getFollowing(currentUser.getId());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profileUser", currentUser);
        model.addAttribute("tweets", tweets);
        model.addAttribute("followers", followers);
        model.addAttribute("following", following);
        model.addAttribute("isOwnProfile", true);
        model.addAttribute("followService", followService);
        return "profile";
    }

    @GetMapping("/profile/{userId}")
    public String viewProfile(@PathVariable Long userId, Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        User profileUser = userService.findById(userId);
        if (profileUser == null) {
            return "redirect:/home";
        }
        List<Tweet> tweets = tweetService.getUserTweets(userId);
        List<User> followers = followService.getFollowers(userId);
        List<User> following = followService.getFollowing(userId);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("tweets", tweets);
        model.addAttribute("followers", followers);
        model.addAttribute("following", following);
        model.addAttribute("isOwnProfile", currentUser.getId().equals(userId));
        model.addAttribute("followService", followService);
        return "profile";
    }

    @PostMapping("/tweet")
    public String createTweet(String content, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        try {
            tweetService.createTweet(content, currentUser.getId());
            redirectAttributes.addFlashAttribute("success", "推文发布成功");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        String referer = session.getAttribute("referer") != null ? 
                         session.getAttribute("referer").toString() : "/timeline";
        return "redirect:" + referer;
    }

    @PostMapping("/tweet/delete/{tweetId}")
    public String deleteTweet(@PathVariable Long tweetId, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        try {
            tweetService.deleteTweet(tweetId, currentUser.getId());
            redirectAttributes.addFlashAttribute("success", "推文已删除");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }
}
