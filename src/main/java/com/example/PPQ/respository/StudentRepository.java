package com.example.PPQ.respository;

import com.example.PPQ.Entity.StudentEntity;
import com.example.PPQ.Payload.Projection_Interface.StudentCoreView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,Integer> {
    StudentEntity findByIdUsers(int idUser);
    @Query("SELECT s FROM StudentEntity s WHERE " +
            "(:name IS NULL OR :name = '' OR s.fullName LIKE %:name%) AND " +
            "(:phoneNumber IS NULL OR :phoneNumber = '' OR s.phoneNumber LIKE %:phoneNumber%)")
    List<StudentEntity> searchByNameAndPhoneNumber(String name, String phoneNumber);
    @Query("SELECT s FROM StudentEntity s WHERE s.fullName LIKE %:name%")
    List<StudentEntity> searchByName(String name);
    @Query(value = "select s.*  from Student s \n" +
           "inner join Users u on \n" +
           "s.idUsers = u.ID\n" +
           "WHERE u.username = :username",nativeQuery = true
   )
    StudentEntity   findByUserName(String username);
    @Query(value = "SELECT  s.ID as id , \n" +
            "\t\ts.fullName as fullName,\n" +
            "\t\ts.phoneNumber ,\n" +
            "        sc.absentDays , sc.attentedDay,\n" +
            "        sc.score1 ,sc.score2,sc.score3,\n" +
            "        sc.scoreHomework \n" +
            "FROM Student s\n" +
            "INNER JOIN CourseStudentClass c ON s.ID = c.idStudent\n" +
            "LEFT JOIN StudentCore sc ON sc.idClass = c.idClass AND sc.idStudent = s.ID\n" +
            "WHERE c.idClass = :classId",nativeQuery = true)
    List<StudentCoreView> findStudentByClassId(int classId);
    List<StudentEntity> findAllByIdIn(Collection<Integer> ids);
    @Modifying
    @Query(value = "update Class as cl\n" +
            "inner join CourseStudentClass as c\n" +
            "on c.idClass = cl.ID\n" +
            "set cl.currentStudents = cl.currentStudents-1\n" +
            "WHERE c.idStudent = :idStudent",nativeQuery = true)
    void decreaseCurrentStudent(int idStudent);
}
