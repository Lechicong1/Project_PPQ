package com.example.PPQ.respository;

import com.example.PPQ.Entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TeacherRespository extends JpaRepository<TeacherEntity,Integer> {
    TeacherEntity findByIdUsers(int id);
    @Query("SELECT t FROM TeacherEntity t WHERE t.fullName LIKE %:name%")
    List<TeacherEntity> searchByName(String name);


    List<TeacherEntity> findAllByIdUsersIn(Set<Integer> idUsers);

    List<TeacherEntity> findAllByIdIn(Set<Integer> listIdTeacher);
}
