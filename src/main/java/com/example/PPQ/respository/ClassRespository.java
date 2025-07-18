package com.example.PPQ.respository;

import com.example.PPQ.Entity.ClassesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ClassRespository extends JpaRepository<ClassesEntity,Integer> {

    @Query("SELECT c FROM ClassesEntity c WHERE c.idCourses= :id and c.currentStudents < c.maxStudents and c.status LIKE 'inactive' ")
    public List<ClassesEntity> findByIdCourses(int id);
    public List<ClassesEntity> findByIdTeachers(int id);
    public ClassesEntity findByClassName(String classname);

    List<ClassesEntity> findAllByIdIn(Set<Integer> ids);
}
