package com.example.PPQ.respository;

import com.example.PPQ.Entity.ClassesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRespository extends JpaRepository<ClassesEntity,Integer> {
    public List<ClassesEntity> findByIdCourses(int id);
    public List<ClassesEntity> findByIdTeachers(int id);
    public ClassesEntity findByClassName(String classname);
}
