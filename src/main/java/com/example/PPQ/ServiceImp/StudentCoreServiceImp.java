package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.AttendanceEntity;
import com.example.PPQ.Entity.StudentCoreEntity;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Projection_Interface.StudentCoreView;
import com.example.PPQ.Payload.Request.AttendanceRequest;
import com.example.PPQ.Payload.Request.StudentCoreRequest;
import com.example.PPQ.Payload.Response.StudentCoreDTO;
import com.example.PPQ.Service.StudentCoreService;
import com.example.PPQ.respository.AttendanceRepo;
import com.example.PPQ.respository.StudentCoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentCoreServiceImp implements StudentCoreService {
    @Autowired
    StudentCoreRepo studentCoreRepo;
    @Autowired
    AttendanceRepo attendanceRepo;

    @Override
    public void updateCoreStudent(Integer idStudent, Integer idClass, StudentCoreRequest studentCoreRequest) {

        StudentCoreEntity studentCoreEntity = studentCoreRepo.findByIdStudentAndIdClass(idStudent, idClass);

        if (studentCoreEntity == null) {
            studentCoreEntity = new StudentCoreEntity();
            studentCoreEntity.setIdStudent(idStudent);
            studentCoreEntity.setIdClass(idClass);
        }
        if (studentCoreRequest.getScore1() != null) studentCoreEntity.setScore1(studentCoreRequest.getScore1());
        if (studentCoreRequest.getScore2() != null) studentCoreEntity.setScore2(studentCoreRequest.getScore2());
        if (studentCoreRequest.getScore3() != null) studentCoreEntity.setScore3(studentCoreRequest.getScore3());
        if (studentCoreRequest.getScoreHomework() != null)
            studentCoreEntity.setScoreHomework(studentCoreRequest.getScoreHomework());
        studentCoreRepo.save(studentCoreEntity);
    }

    @Override
    public void updateAbsentStudent(List<AttendanceRequest> attendanceReq) {
        // Group theo idClass (trong trường hợp có nhiều class khác nhau)
        Map<Integer, List<AttendanceRequest>> groupedByClass = attendanceReq.stream()
                .collect(Collectors.groupingBy(AttendanceRequest::getIdClass));

        List<StudentCoreEntity> toUpdate = new ArrayList<>();

        for (Map.Entry<Integer, List<AttendanceRequest>> entry : groupedByClass.entrySet()) {
            Integer idClass = entry.getKey();
            List<AttendanceRequest> requests = entry.getValue();
            List<Integer> studentIds = requests.stream()
                    .map(AttendanceRequest::getIdStudent)
                    .collect(Collectors.toList());

            List<StudentCoreEntity> existing = studentCoreRepo.findAllByStudentIdsAndClass(studentIds, idClass);

            // Tạo map để tra nhanh
            Map<Integer, StudentCoreEntity> entityMap = existing.stream()
                    .collect(Collectors.toMap(StudentCoreEntity::getIdStudent, e -> e));

            for (AttendanceRequest attendance : requests) {
                StudentCoreEntity entity = entityMap.get(attendance.getIdStudent());

                if (entity == null) {
                    entity = new StudentCoreEntity();
                    entity.setIdStudent(attendance.getIdStudent());
                    entity.setIdClass(idClass);
                    entity.setAbsentDays(0);
                    entity.setAttentedDay(0);
                }

                if ("absent".equalsIgnoreCase(attendance.getStatus())) {
                    entity.setAbsentDays(Optional.ofNullable(entity.getAbsentDays()).orElse(0) + 1);
                } else {
                    entity.setAttentedDay(Optional.ofNullable(entity.getAttentedDay()).orElse(0) + 1);
                }

                toUpdate.add(entity);
            }
        }
        studentCoreRepo.saveAll(toUpdate);
        // lưu thông tin điểm danh vào bảng attendance
        List<AttendanceEntity> listAttendance = new ArrayList<>();
        for (AttendanceRequest AttendanceRequest : attendanceReq) {
            AttendanceEntity entity = new AttendanceEntity(AttendanceRequest);
            listAttendance.add(entity);
        }
        attendanceRepo.saveAll(listAttendance);
    }


}
