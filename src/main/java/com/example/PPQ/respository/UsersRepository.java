package com.example.PPQ.respository;

import com.example.PPQ.Entity.User_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UsersRepository extends JpaRepository<User_Entity,Integer> {
    @Query("SELECT u FROM User_Entity u")
    List<User_Entity> getAllUsersBasic();
    boolean existsByUsername(String username);
    List<User_Entity> findByIdRoles(int id);
    User_Entity findByUsername(String username);

    List<User_Entity> findAllByIdIn(Set<Integer> id);
}
