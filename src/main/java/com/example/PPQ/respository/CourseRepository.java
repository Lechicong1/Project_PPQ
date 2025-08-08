package com.example.PPQ.respository;

import com.example.PPQ.Entity.CourseEntity;
import com.example.PPQ.Payload.Projection_Interface.CourseView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity,Integer> , JpaSpecificationExecutor<CourseEntity> {
    @Query("SELECT DISTINCT c.language FROM CourseEntity c")
    List<String> getAllLanguages();
//    Page<CourseEntity> findAll(Pageable pageable);
//    Page<CourseEntity> findByLanguage(String language,Pageable pageable);
    List<CourseEntity> findAllByIdIn(Set<Integer> listIdCourse);
    @Query(value = "select " +
            "            c.nameCourse as nameCourse, " +
            "            cl.className as nameClass, " +
            "            csc.enrollmentDate as enrollmentDate, " +
            "            c.Fee as fee, " +
            "            c.numberSessions as numberSessions, " +
            "            sc.score1,sc.score2,sc.score3,sc.scoreHomework, " +
            "            sc.attentedDay,sc.absentDays " +
            "            from Course c " +
            "            inner join CourseStudentClass csc on csc.idCourse = c.ID " +
            "            inner join Class cl on cl.ID =csc.idClass " +
            "            left join StudentCore sc on csc.idStudent = sc.idStudent and cl.ID = sc.idClass " +
            "            where csc.idStudent = :idStudent",nativeQuery = true)
    List<CourseView> findCourseByIdStudent(Integer idStudent);

    List<CourseEntity> language(String language);
}
