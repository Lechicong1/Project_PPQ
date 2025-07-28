package com.example.PPQ.respository;

import com.example.PPQ.Entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TeacherRespository extends JpaRepository<TeacherEntity,Integer> {
    TeacherEntity findByIdUsers(int id);
    @Query("SELECT t FROM TeacherEntity t WHERE t.fullName LIKE %:name%")
    List<TeacherEntity> searchByName(String name);
    @Modifying
    @Query("delete from TeacherEntity t where t.idUsers = :idUsers")
    void deleteTeacherByIdUsers(Integer idUsers);
    @Query(value = "select t.* from Teacher t \n" +
            "inner join Users u on \n" +
            "u.ID = t.idUsers\n" +
            "where u.username = :username ",nativeQuery = true)
    TeacherEntity findByUserName(String username);
    List<TeacherEntity> findAllByIdUsersIn(Set<Integer> idUsers);

    List<TeacherEntity> findAllByIdIn(Set<Integer> listIdTeacher);
}
