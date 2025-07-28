package com.example.PPQ.respository;

import com.example.PPQ.Entity.ScheduleEntity;
import com.example.PPQ.Payload.Projection_Interface.ScheduleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRespository extends JpaRepository<ScheduleEntity,Integer> {
    ScheduleEntity findByIdClassAndThuAndStartTime(int idClass, String thu , LocalTime gioHoc);
    ScheduleEntity findByNameRoomAndThuAndStartTime(String nameRoom , String thu , LocalTime gioHoc);
    List<ScheduleEntity> findByIdClass(int idClass);
    @Query(value = "SELECT " +
            "sc.ID as id, " +
            "sc.idClass as idClass, " +
            "sc.nameRoom as nameRoom, " +
            "sc.thu as thu, " +
            "sc.startTime as startTime, " +
            "sc.endTime as endTime, " +
            "cl.className as nameClass " +
            "FROM Schedule sc " +
            "INNER JOIN Class cl on sc.idClass = cl.ID where " +
            "(:thu IS NULL OR :thu = '' OR sc.thu LIKE :thu) AND " +
            "(:idClass IS NULL OR sc.idClass =:idClass)",nativeQuery = true)
    List<ScheduleView> findScheduleByThuAndIdClass(String thu, Integer idClass);
    @Query(value = "SELECT " +
            "sc.ID as id, " +
            "sc.idClass as idClass, " +
            "sc.nameRoom as nameRoom, " +
            "sc.thu as thu, " +
            "sc.startTime as startTime, " +
            "sc.endTime as endTime, " +
            "cl.className as nameClass, " +
            "Course.nameCourse as nameCourse " +
            "FROM CourseStudentClass c " +
            "INNER JOIN Student s ON c.idStudent = s.ID " +
            "INNER JOIN Class cl ON c.idClass = cl.ID " +
            "INNER JOIN Schedule sc ON c.idClass = sc.idClass " +
            "INNER JOIN Course ON Course.ID = c.idCourse " +
            "WHERE s.ID = :idStudent", nativeQuery = true)
    List<ScheduleView> findByStudent(int idStudent);
    @Query(value = "SELECT " +
            "sc.ID as id, " +
            "sc.idClass as idClass, " +
            "sc.nameRoom as nameRoom, " +
            "sc.thu as thu, " +
            "sc.startTime as startTime, " +
            "sc.endTime as endTime, " +
            "cl.className as nameClass, " +
            "co.nameCourse as nameCourse " +
            "FROM Schedule sc " +
            "INNER JOIN Class cl on sc.idClass = cl.ID " +
            "INNER JOIN Course co on co.ID = cl.idCourses " +
            "WHERE cl.idTeachers = :idTeacher", nativeQuery = true)
    List<ScheduleView> findByTeacher(int idTeacher);
    List<ScheduleEntity> findByIdClassIn(List<Integer> listClassId);
    @Modifying
    @Query("delete ScheduleEntity s where  s.idClass=:idClass")
    void deleteByIdClass(Integer idClass);
    @Query(value = "SELECT " +
            "sc.ID as id, " +
            "sc.idClass as idClass, " +
            "sc.nameRoom as nameRoom, " +
            "sc.thu as thu, " +
            "sc.startTime as startTime, " +
            "sc.endTime as endTime, " +
            "cl.className as nameClass " +
            "FROM Schedule sc " +
            "INNER JOIN Class cl on sc.idClass = cl.ID ",nativeQuery = true)
    List<ScheduleView> findScheduleAll();
}
