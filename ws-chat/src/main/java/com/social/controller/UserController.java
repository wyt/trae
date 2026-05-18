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

    /**
     * 显示用户首页，展示所有可关注的用户列表
     * @param model 视图模型，传递用户列表和当前用户信息
     * @param session Http会话，获取当前登录用户
     * @return 首页视图，未登录跳转到登录页
     */
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

    /**
     * 关注指定用户
     * @param userId 要关注的用户ID
     * @param session Http会话，获取当前登录用户
     * @param redirectAttributes 重定向属性，传递操作结果信息
     * @return 重定向回首页
     */
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

    /**
     * 取消关注指定用户
     * @param userId 要取消关注的用户ID
     * @param session Http会话，获取当前登录用户
     * @param redirectAttributes 重定向属性，传递操作结果信息
     * @return 重定向回首页
     */
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
