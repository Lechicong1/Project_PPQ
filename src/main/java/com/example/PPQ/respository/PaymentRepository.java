package com.example.PPQ.respository;

import com.example.PPQ.Entity.PaymentEntity;
import com.example.PPQ.Payload.Projection_Interface.paymentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {
    PaymentEntity findByCourseIdAndUserId(int courseId, int userId);
    PaymentEntity findTopByUserIdAndCourseIdOrderByCreatedAtDesc(int userId, int courseId); // tim ban ghi theo thoi gian tao gan nhat ( neu nho hon 5 phut thi k cho tao)
    List<PaymentEntity> findByStatusAndCreatedAtBefore(String status, LocalDateTime time);
   @Query(value = "select p.*, cl.className , c.nameCourse from payment p \n" +
           "left join Class cl on cl.ID =p.classId\n" +
           "left join Course c on c.ID = p.courseId",nativeQuery = true)
    List<paymentView> findAllPayment();
}
