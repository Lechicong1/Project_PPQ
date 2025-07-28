package com.example.PPQ.respository;

import com.example.PPQ.Entity.AttendanceEntity;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceEntity,Integer> {
}
