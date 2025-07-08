package com.example.PPQ.respository;

import com.example.PPQ.Entity.Teacher_Entity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface TeacherRespository extends JpaRepository<Teacher_Entity,Integer> {
    Teacher_Entity findByIdUsers(int id);
    @Query("SELECT t FROM Teacher_Entity t WHERE t.fullName LIKE %:name%")
    List<Teacher_Entity> searchByName(String name);


    List<Teacher_Entity> findAllByIdUsersIn(Set<Integer> idUsers);
}
