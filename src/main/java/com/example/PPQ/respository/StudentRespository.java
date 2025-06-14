package com.example.PPQ.respository;

import com.example.PPQ.Entity.Student_Entity;
import com.example.PPQ.Payload.Response.Student_response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRespository extends JpaRepository<Student_Entity,Integer> {
    Student_Entity findByIdUsers(int idUser);
    @Query("SELECT s FROM Student_Entity s WHERE " +
            "(:name IS NULL OR :name = '' OR s.fullName LIKE %:name%) AND " +
            "(:phoneNumber IS NULL OR :phoneNumber = '' OR s.phoneNumber LIKE %:phoneNumber%)")
    List<Student_Entity> searchByNameAndPhoneNumber(String name,String phoneNumber);
    @Query("SELECT s FROM Student_Entity s WHERE s.fullName LIKE %:name%")
    List<Student_Entity > searchByName(String name);
}
