package com.example.PPQ.respository;

import com.example.PPQ.Entity.Teacher_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRespository extends JpaRepository<Teacher_Entity,Integer> {
    Teacher_Entity findByIdUsers(int id);
    @Query("SELECT t FROM Teacher_Entity t WHERE t.fullName LIKE %:name%")
    List<Teacher_Entity> searchByName(String name);
}
