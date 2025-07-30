package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.BusinessLogicException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.ScheduleRequest;
import com.example.PPQ.Payload.Response.ScheduleDTO;
import com.example.PPQ.Payload.Projection_Interface.ScheduleView;
import com.example.PPQ.Service.ScheduleServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ScheduleService implements ScheduleServiceImp {
    @Autowired
    ScheduleRespository scheduleRespository;
    @Autowired
    ClassRespository classRespository;
    @Autowired
    StudentRespository studentRespository;
    @Autowired
    UsersRepository UsersRepository;
    @Autowired
    CourseRespository courseRespository;
    @Autowired
    CourseStudentClassRepository courseStudentClassRepository;
    @Autowired
    TeacherRespository teacherRespository;

    @Override
    public List<ScheduleDTO> gettAllSchedule() {
        List<ScheduleDTO> listSchedule_dto = new ArrayList<>();
        List<ScheduleView> schedule=scheduleRespository.findScheduleAll();
        if(schedule.isEmpty())
            throw new ResourceNotFoundException("Không tồn tại lịch học");
        for(ScheduleView sc:schedule){
            ScheduleDTO schedule_dto=new ScheduleDTO(sc);
            listSchedule_dto.add(schedule_dto);
        }
        return listSchedule_dto;
    }

    @Override
    public ScheduleDTO getScheduleById(int id) {
        ScheduleEntity schedule=scheduleRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tồn tại lịch học"));
            ScheduleDTO schedule_dto=new ScheduleDTO();
            schedule_dto.setStartTime(schedule.getStartTime());
            schedule_dto.setEndTime(schedule.getEndTime());
           ClassesEntity classes = classRespository.findById(schedule.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại "));
            schedule_dto.setNameClass(classes.getClassName());
            schedule_dto.setThu(schedule.getThu());
            schedule_dto.setNameRoom(schedule.getNameRoom());
        return schedule_dto;
    }

    @Override
    public void addSchedule(ScheduleRequest scheduleRequest) {
        // kiem tra xem co ton tai id_class khong
        ClassesEntity classes=classRespository.findById(scheduleRequest.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
        ScheduleEntity scheduleEntity=new ScheduleEntity();
        // dam bao 1 lop khong bi trung gio hoc (1 lop cung gio hoc khong the hoc 2 phong)
        ScheduleEntity classConflict=scheduleRespository.findByIdClassAndThuAndStartTime(scheduleRequest.getIdClass(), scheduleRequest.getThu(),scheduleRequest.getStartTime());
        if(classConflict!=null){
            throw new BusinessLogicException("Lớp học đã tồn tại lịch học này rồi");
        }
        // dam bao 1 phong hoc k bi trung gio hoc ( 2 lop cung lich hoc khong the hoc cung 1 phong)
        ScheduleEntity roomConflict=scheduleRespository.findByNameRoomAndThuAndStartTime(scheduleRequest.getNameRoom(),scheduleRequest.getThu(),scheduleRequest.getStartTime());
        if(roomConflict!=null){
            throw new RuntimeException("Phòng học đã tồn tại lịch học này rồi");
        }
        scheduleEntity.setThu(scheduleRequest.getThu());
        scheduleEntity.setNameRoom(scheduleRequest.getNameRoom());
        scheduleEntity.setStartTime(scheduleRequest.getStartTime());
        scheduleEntity.setEndTime(scheduleRequest.getEndTime());
        scheduleEntity.setIdClass(scheduleRequest.getIdClass());

        scheduleRespository.save(scheduleEntity);

    }

    @Override
    public void updateSchedule(int id, ScheduleRequest scheduleRequest) {
        ScheduleEntity scheduleEntity=scheduleRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tìm thấy lịch học")) ;
        // kiem tra xem co ton tai id_class khong
        ClassesEntity classes=classRespository.findById(scheduleRequest.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
        // dam bao 1 lop khong bi trung gio hoc (1 lop cung gio hoc khong the hoc 2 phong)
        ScheduleEntity classConflict=scheduleRespository.findByIdClassAndThuAndStartTime(scheduleRequest.getIdClass(), scheduleRequest.getThu(),scheduleRequest.getStartTime());
        if(classConflict!=null){
            throw new BusinessLogicException("Lớp học đã tồn tại lịch học này rồi");
        }
        // dam bao 1 phong hoc k bi trung gio hoc ( 2 lop cung lich hoc khong the hoc cung 1 phong)
        ScheduleEntity roomConflict=scheduleRespository.findByNameRoomAndThuAndStartTime(scheduleRequest.getNameRoom(),scheduleRequest.getThu(),scheduleRequest.getStartTime());
        if(roomConflict!=null){
            throw new RuntimeException("Phòng học đã tồn tại lịch học này rồi");
        }
        if(scheduleRequest.getIdClass()!=null){
            scheduleEntity.setIdClass(scheduleRequest.getIdClass());
        }
        if(scheduleRequest.getThu()!=null){
            scheduleEntity.setThu(scheduleRequest.getThu());
        }
        if(scheduleRequest.getNameRoom()!=null){
            scheduleEntity.setNameRoom(scheduleRequest.getNameRoom());
        }
        if(scheduleRequest.getStartTime()!=null){
            scheduleEntity.setStartTime(scheduleRequest.getStartTime());
        }
        if(scheduleRequest.getEndTime()!=null){
            scheduleEntity.setEndTime(scheduleRequest.getEndTime());
        }
        scheduleRespository.save(scheduleEntity);
    }

    @Override
    public void deleteSchedule(int id) {
        if(!scheduleRespository.existsById(id)){
            throw new ResourceNotFoundException("Lịch học không tồn tại") ;
        }
        scheduleRespository.deleteById(id);
    }
//    @Cacheable(value = "scheduleForStudent", key = "#username", sync = true)
    @Override
    public List<ScheduleDTO> getScheduleForStudent(String username) {

        List<ScheduleView> scheduleViews = scheduleRespository.findScheduleForStudentByUserName( username);
        if(scheduleViews.isEmpty())
            throw new ResourceNotFoundException("Không tồn tại lịch học");
        List<ScheduleDTO> list=new ArrayList<>();
        for(ScheduleView s:scheduleViews){
            ScheduleDTO scheduleDTO=new ScheduleDTO(s);
            list.add(scheduleDTO);
        }
        return list;
    }
//    @Cacheable(value = "scheduleForTeacherDTO", key = "#username", sync = true)
    // value la ten vung nho trong cache , key la ten con ben trong vung nho value
        @Override
        public List<ScheduleDTO> getScheduleForTeacher(String username) {
               TeacherEntity teacher=teacherRespository.findByUserName(username);
                     if(teacher ==null ) throw new ResourceNotFoundException("Giáo viên không tồn tại");
            List<ScheduleView> scheduleViews = scheduleRespository.findByTeacher(teacher.getId());
            if(scheduleViews.isEmpty())
                throw new ResourceNotFoundException("Không tồn tại lịch học");
            List<ScheduleDTO> list=new ArrayList<>();
            for(ScheduleView s:scheduleViews){
                ScheduleDTO scheduleDTO=new ScheduleDTO(s);
                list.add(scheduleDTO);
            }
            return list;

        }

    @Override
    public List<ScheduleDTO> findScheduleByDayOfWeekAndClass(String thu, Integer idClass) {
        List<ScheduleDTO> listSchedule_dto = new ArrayList<>();
        List<ScheduleView> schedule=scheduleRespository.findScheduleByThuAndIdClass(thu, idClass);
        if(schedule.isEmpty()){
            throw new ResourceNotFoundException("Không tồn tại lịch học");
        }

        for(ScheduleView sc:schedule){
            ScheduleDTO schedule_dto=new ScheduleDTO(sc);
            listSchedule_dto.add(schedule_dto);
        }
        return listSchedule_dto;
    }


}
