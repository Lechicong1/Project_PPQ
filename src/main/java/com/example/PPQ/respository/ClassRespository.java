package com.example.PPQ.respository;

import com.example.PPQ.Entity.ClassesEntity;
import com.example.PPQ.Payload.Projection_Interface.ClassView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ClassRespository extends JpaRepository<ClassesEntity,Integer> {

    @Query("SELECT c FROM ClassesEntity c WHERE c.idCourses= :id and c.currentStudents < c.maxStudents and c.status LIKE 'inactive' ")
    public List<ClassesEntity> findByIdCourses(int id);
    @Query(value = "select " +
            "c.ID as id, " +
            "c.className as nameClass, " +
            "co.nameCourse as nameCourse, " +
            "t.fullName as nameTeacher, " +
            "c.maxStudents as maxStudents, " +
            "c.currentStudents as currentStudents, " +
            "c.status as status " +
            "from Class c " +
            "inner join Course co on co.ID = c.idCourses " +
            "inner join Teacher t on t.ID = c.idTeachers " +
             "WHERE c.idTeachers= :idTeachers",nativeQuery = true)
    public List<ClassView> findByIdTeachers(int idTeachers);
    public ClassesEntity findByClassName(String classname);
    @Modifying
    @Query("delete from ClassesEntity c where c.idCourses= :idCourses")
    void deleteClassByIdCourses(int idCourses);
    List<ClassesEntity> findAllByIdIn(Set<Integer> ids);
    @Modifying
    @Query("update ClassesEntity c set c.idTeachers = null  where c.idTeachers= :idTeacher")
    void setIdTeacherNull(int idTeacher);
    @Query(value = "select " +
            "c.ID as id, " +
            "c.className as nameClass, " +
            "co.nameCourse as nameCourse, " +
            "t.fullName as nameTeacher, " +
            "c.maxStudents as maxStudents, " +
            "c.currentStudents as currentStudents, " +
            "c.roadMap as roadMap,   " +
            "c.status as status " +
            "from Class c " +
            "left join Course co on co.ID = c.idCourses " +
            "left join Teacher t on t.ID = c.idTeachers",nativeQuery = true)
    List<ClassView> findAllClass();
}
