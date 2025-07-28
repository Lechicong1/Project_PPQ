package com.example.PPQ.respository;

import com.example.PPQ.Entity.StudentCoreEntity;
import com.example.PPQ.Payload.Projection_Interface.StudentCoreView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCoreRepo extends JpaRepository<StudentCoreEntity,Integer> {
    @Query(value = "select s.fullName as fullName, " +
            "s.phoneNumber , " +
            "        sc.absentDays , sc.attentedDay, " +
            "        sc.score1 ,sc.score2,sc.score3, " +
            "        sc.scoreHomework , " +
            "        cl.className  " +
            "from StudentCore sc " +
            "inner join Student s on s.ID = sc.idStudent " +
            "inner join Class cl on cl.ID = sc.idClass " +
            "where (:idStudent is NULL or sc.idStudent = :idStudent)  and  (:idClass  is Null or  sc.idClass = 20)",nativeQuery = true)
    List<StudentCoreView> findCoreByStudentAndClass(Integer idStudent, Integer idClass);
    @Query("Select s from StudentCoreEntity s where s.idStudent=:idStudent and s.idClass = :idClass")
    StudentCoreEntity findByIdStudentAndIdClass(Integer idStudent, Integer idClass);


    @Query("SELECT s FROM StudentCoreEntity s WHERE s.idStudent IN :studentIds AND s.idClass = :idClass")
    List<StudentCoreEntity> findAllByStudentIdsAndClass(List<Integer> studentIds, Integer idClass);
}
