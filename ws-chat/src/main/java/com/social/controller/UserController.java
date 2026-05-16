package com.social.controller;

import com.social.entity.User;
import com.social.service.FollowService;
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
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        List<User> allUsers = userService.getAllUsers().stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .collect(Collectors.toList());
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", allUsers);
        model.addAttribute("followService", followService);
        return "home";
    }

    @PostMapping("/follow/{userId}")
    public String follow(@PathVariable Long userId, HttpSession session, 
                         RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        try {
            followService.follow(currentUser.getId(), userId);
            redirectAttributes.addFlashAttribute("success", "关注成功");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }

    @PostMapping("/unfollow/{userId}")
    public String unfollow(@PathVariable Long userId, HttpSession session, 
                           RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }
        followService.unfollow(currentUser.getId(), userId);
        redirectAttributes.addFlashAttribute("success", "取消关注成功");
        return "redirect:/home";
    }
}
