package com.social.repository;

import com.social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问接口
 * 继承JpaRepository获得基础CRUD功能
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象，可能为空
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return true表示已存在，false表示不存在
     */
    boolean existsByUsername(String username);
}
