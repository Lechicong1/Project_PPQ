package com.example.PPQ.respository;

import com.example.PPQ.Entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRespository extends JpaRepository<ScheduleEntity,Integer> {
    ScheduleEntity findByIdClassAndThuAndStartTime(int idClass, String thu , LocalTime gioHoc);
    ScheduleEntity findByNameRoomAndThuAndStartTime(String nameRoom , String thu , LocalTime gioHoc);
    List<ScheduleEntity> findByIdClass(int idClass);
    @Query("SELECT s FROM ScheduleEntity s WHERE " +
            "(:thu IS NULL OR :thu = '' OR s.thu LIKE :thu) AND " +
            "(:idClass IS NULL OR s.idClass =:idClass)")
    List<ScheduleEntity> findScheduleByThuAndIdClass(String thu, Integer idClass);
    List<ScheduleEntity> findByIdClassIn(List<Integer> listClassId);
}
