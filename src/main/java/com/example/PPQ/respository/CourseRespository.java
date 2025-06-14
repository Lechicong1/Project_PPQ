package com.example.PPQ.respository;

import com.example.PPQ.Entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CourseRespository extends JpaRepository<CourseEntity,Integer> {
}
