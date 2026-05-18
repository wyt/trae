package com.social.repository;

import com.social.entity.Follow;
import com.social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 关注关系数据访问接口
 * 继承JpaRepository获得基础CRUD功能
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * 根据关注者和被关注者查询关注关系
     * @param follower 关注者
     * @param following 被关注者
     * @return 关注关系对象，可能为空
     */
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    /**
     * 查询指定用户关注的所有人
     * @param followerId 关注者ID
     * @return 该用户关注的用户列表
     */
    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :followerId")
    List<User> findFollowingByFollowerId(@Param("followerId") Long followerId);

    /**
     * 查询关注指定用户的所有人（粉丝列表）
     * @param followingId 被关注者ID
     * @return 关注该用户的粉丝列表
     */
    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :followingId")
    List<User> findFollowersByFollowingId(@Param("followingId") Long followingId);

    /**
     * 检查是否已存在关注关系
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return true表示已关注，false表示未关注
     */
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * 删除关注关系
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     */
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
