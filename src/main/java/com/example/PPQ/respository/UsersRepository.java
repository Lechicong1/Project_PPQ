package com.example.PPQ.respository;

import com.example.PPQ.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity,Integer> {
    @Query("SELECT u FROM UserEntity u")
    List<UserEntity> getAllUsersBasic();
    boolean existsByUsername(String username);
    List<UserEntity> findByIdRoles(int id);
    UserEntity findByUsername(String username);
    @Query("SELECT u FROM UserEntity u WHERE " +
            "(:username IS NULL OR :username = '' OR u.username LIKE %:username%) AND " +
            "(:role IS NULL OR u.idRoles = :role)")
    List<UserEntity> findByUsernameAndRoles(String username, Integer role);
    List<UserEntity> findAllByIdIn(Set<Integer> id);

}
