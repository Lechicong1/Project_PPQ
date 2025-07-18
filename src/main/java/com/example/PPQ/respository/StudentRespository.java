package com.example.PPQ.respository;

import com.example.PPQ.Entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRespository extends JpaRepository<StudentEntity,Integer> {
    StudentEntity findByIdUsers(int idUser);
    @Query("SELECT s FROM StudentEntity s WHERE " +
            "(:name IS NULL OR :name = '' OR s.fullName LIKE %:name%) AND " +
            "(:phoneNumber IS NULL OR :phoneNumber = '' OR s.phoneNumber LIKE %:phoneNumber%)")
    List<StudentEntity> searchByNameAndPhoneNumber(String name, String phoneNumber);
    @Query("SELECT s FROM StudentEntity s WHERE s.fullName LIKE %:name%")
    List<StudentEntity> searchByName(String name);

    List<StudentEntity> findAllByIdIn(Collection<Integer> ids);
}
