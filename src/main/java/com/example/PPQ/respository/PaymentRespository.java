package com.example.PPQ.respository;

import com.example.PPQ.Entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRespository extends JpaRepository<PaymentEntity,Integer> {
    PaymentEntity findByCourseIdAndUserId(int courseId, int userId);
    PaymentEntity findTopByUserIdAndCourseIdOrderByCreatedAtDesc(int userId, int courseId); // tim ban ghi theo thoi gian tao gan nhat ( neu nho hon 5 phut thi k cho tao)
    List<PaymentEntity> findByStatusAndCreatedAtBefore(String status, LocalDateTime time);

}
