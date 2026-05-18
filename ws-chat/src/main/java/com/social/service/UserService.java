package com.social.service;

import com.social.entity.User;
import com.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 用户注册
     * 校验规则：用户名不能重复
     * @param user 用户注册信息
     * @return 注册成功后的用户实体
     * @throws RuntimeException 用户名已存在时抛出异常
     */
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        return userRepository.save(user);
    }

    /**
     * 用户登录
     * 校验规则：用户必须存在、密码必须正确
     * 登录成功后将用户信息存入HttpSession
     * @param username 用户名
     * @param password 密码
     * @param session Http会话，用于存储登录用户信息
     * @return 登录成功的用户实体
     * @throws RuntimeException 用户不存在或密码错误时抛出异常
     */
    public User login(String username, String password, HttpSession session) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        session.setAttribute("currentUser", user);
        return user;
    }

    /**
     * 用户退出登录
     * 从HttpSession中移除当前用户信息
     * @param session Http会话
     */
    public void logout(HttpSession session) {
        session.removeAttribute("currentUser");
    }

    /**
     * 获取当前登录用户
     * @param session Http会话
     * @return 当前登录用户，未登录返回null
     */
    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    /**
     * 获取系统中所有用户列表
     * @return 所有用户的列表
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return 对应用户实体，不存在返回null
     */
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 对应用户实体，不存在返回null
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
