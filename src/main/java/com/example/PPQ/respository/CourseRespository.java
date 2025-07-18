package com.example.PPQ.respository;

import com.example.PPQ.Entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CourseRespository extends JpaRepository<CourseEntity,Integer> {
    @Query("SELECT DISTINCT c.language FROM CourseEntity c")
    List<String> getAllLanguages();
    List<CourseEntity> findByLanguage(String language);

    List<CourseEntity> findAllByIdIn(Set<Integer> listIdCourse);
}
