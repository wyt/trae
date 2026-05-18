package com.social.controller;

import com.social.entity.User;
import com.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 首页访问，重定向到登录页面
     * @return 登录页面重定向地址
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    /**
     * 显示登录页面
     * @return 登录页面视图名
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 处理用户登录请求
     * @param username 用户名
     * @param password 密码
     * @param session Http会话，用于存储登录用户信息
     * @param redirectAttributes 重定向属性，用于传递错误信息
     * @return 登录成功跳转到首页，失败返回登录页
     */
    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, 
                        RedirectAttributes redirectAttributes) {
        try {
            userService.login(username, password, session);
            return "redirect:/home";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    /**
     * 显示用户注册页面
     * @param model 视图模型，用于传递空的User对象
     * @return 注册页面视图名
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * 处理用户注册请求
     * @param user 用户注册信息
     * @param bindingResult 数据绑定结果，用于表单验证
     * @param redirectAttributes 重定向属性，用于传递成功/错误信息
     * @return 注册成功跳转到登录页，失败返回注册页
     */
    @PostMapping("/register")
    public String register(@Valid User user, BindingResult bindingResult, 
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            userService.register(user);
            redirectAttributes.addFlashAttribute("success", "注册成功，请登录");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * 用户退出登录，清除会话中的用户信息
     * @param session Http会话
     * @return 重定向到登录页面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/login";
    }
}
