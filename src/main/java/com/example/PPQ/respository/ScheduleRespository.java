package com.example.PPQ.respository;

import com.example.PPQ.Entity.Schedule_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRespository extends JpaRepository<Schedule_Entity,Integer> {
    Schedule_Entity findByIdClassAndThuAndStartTime(int idClass, String thu , LocalTime gioHoc);
    Schedule_Entity findByNameRoomAndThuAndStartTime(String nameRoom , String thu , LocalTime gioHoc);
    List<Schedule_Entity> findByIdClass(int idClass);

    List<Schedule_Entity> findByIdClassIn(List<Integer> listClassId);
}
