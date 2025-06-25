package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.BusinessLogicException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.ScheduleRequest;
import com.example.PPQ.Payload.Response.Schedule_response;
import com.example.PPQ.Service_Imp.ScheduleServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
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
    public List<Schedule_response> gettAllSchedule() {
        List<Schedule_response> listSchedule_dto = new ArrayList<>();
        List<Schedule_Entity> schedule=scheduleRespository.findAll();
        if(schedule.isEmpty())
            throw new ResourceNotFoundException("Không tồn tại lịch học");
        for(Schedule_Entity sc:schedule){
            Schedule_response schedule_dto=new Schedule_response();
            schedule_dto.setIdClass(sc.getIdClass());
            schedule_dto.setId(sc.getId());
            schedule_dto.setStartTime(sc.getStartTime());
            schedule_dto.setEndTime(sc.getEndTime());
            ClassesEntity classes = classRespository.findById(sc.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại "));
            schedule_dto.setNameClass(classes.getClassName());
            schedule_dto.setThu(sc.getThu());
            schedule_dto.setNameRoom(sc.getNameRoom());
            listSchedule_dto.add(schedule_dto);
        }
        return listSchedule_dto;
    }

    @Override
    public Schedule_response getScheduleById(int id) {
        Schedule_Entity schedule=scheduleRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tồn tại lịch học"));
            Schedule_response schedule_dto=new Schedule_response();
            schedule_dto.setStartTime(schedule.getStartTime());
            schedule_dto.setEndTime(schedule.getEndTime());
           ClassesEntity classes = classRespository.findById(schedule.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại "));
            schedule_dto.setNameClass(classes.getClassName());
            schedule_dto.setThu(schedule.getThu());
            schedule_dto.setNameRoom(schedule.getNameRoom());
        return schedule_dto;
    }

    @Override
    public boolean addSchedule(ScheduleRequest scheduleRequest) {
        // kiem tra xem co ton tai id_class khong
        ClassesEntity classes=classRespository.findById(scheduleRequest.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
        Schedule_Entity scheduleEntity=new Schedule_Entity();
        // dam bao 1 lop khong bi trung gio hoc (1 lop cung gio hoc khong the hoc 2 phong)
        Schedule_Entity classConflict=scheduleRespository.findByIdClassAndThuAndStartTime(scheduleRequest.getIdClass(), scheduleRequest.getThu(),scheduleRequest.getStartTime());
        if(classConflict!=null){
            throw new BusinessLogicException("Lớp học đã tồn tại lịch học này rồi");
        }
        // dam bao 1 phong hoc k bi trung gio hoc ( 2 lop cung lich hoc khong the hoc cung 1 phong)
        Schedule_Entity roomConflict=scheduleRespository.findByNameRoomAndThuAndStartTime(scheduleRequest.getNameRoom(),scheduleRequest.getThu(),scheduleRequest.getStartTime());
        if(roomConflict!=null){
            throw new RuntimeException("Phòng học đã tồn tại lịch học này rồi");
        }
        scheduleEntity.setThu(scheduleRequest.getThu());
        scheduleEntity.setNameRoom(scheduleRequest.getNameRoom());
        scheduleEntity.setStartTime(scheduleRequest.getStartTime());
        scheduleEntity.setEndTime(scheduleRequest.getEndTime());
        scheduleEntity.setIdClass(scheduleRequest.getIdClass());
        try{
            scheduleRespository.save(scheduleEntity);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi them schedule "+ e.getMessage());
            return false;
        }

    }


    @Override
    public boolean updateSchedule(int id, ScheduleRequest scheduleRequest) {
        Schedule_Entity scheduleEntitycheck=scheduleRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tìm thấy lịch học")) ;
        // kiem tra xem co ton tai id_class khong
        ClassesEntity classes=classRespository.findById(scheduleRequest.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
        Schedule_Entity scheduleEntity=new Schedule_Entity();
        // dam bao 1 lop khong bi trung gio hoc (1 lop cung gio hoc khong the hoc 2 phong)
        Schedule_Entity classConflict=scheduleRespository.findByIdClassAndThuAndStartTime(scheduleRequest.getIdClass(), scheduleRequest.getThu(),scheduleRequest.getStartTime());
        if(classConflict!=null){
            throw new BusinessLogicException("Lớp học đã tồn tại lịch học này rồi");
        }
        // dam bao 1 phong hoc k bi trung gio hoc ( 2 lop cung lich hoc khong the hoc cung 1 phong)
        Schedule_Entity roomConflict=scheduleRespository.findByNameRoomAndThuAndStartTime(scheduleRequest.getNameRoom(),scheduleRequest.getThu(),scheduleRequest.getStartTime());
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
        try{
            scheduleRespository.save(scheduleEntity);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi sua schedule "+ e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteSchedule(int id) {
        if(!scheduleRespository.existsById(id)){
            throw new ResourceNotFoundException("Lịch học không tồn tại") ;
        }
        try{
            scheduleRespository.deleteById(id);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi xoa schedule "+ e.getMessage());
            return false;
        }
    }

    @Override
    public List<Schedule_response> getScheduleForStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        User_Entity user=UsersRepository.findByUsername(username);
        if(user==null){
            throw new ResourceNotFoundException("Không tồn tại User");
        }
        Student_Entity student = studentRespository.findById(user.getId()).orElseThrow(()->new ResourceNotFoundException("Học sinh không tồn tại")) ;
        List<CourseStudentClassEntity> courseStudentClassEntities=courseStudentClassRepository.findByIdStudent(student.getId());
        if(courseStudentClassEntities==null){
            throw new ResourceNotFoundException("Khóa học này chưa có sinh viên đăng kí ");
        }
        List<Schedule_Entity> AllSchedule=new ArrayList<>();
        for(CourseStudentClassEntity x : courseStudentClassEntities){
            List<Schedule_Entity> scheduleEntity=scheduleRespository.findByIdClass(x.getIdClass());
            AllSchedule.addAll(scheduleEntity);
        }
        List<Schedule_response> listSchedule_dto=new ArrayList<>();
        for(Schedule_Entity y : AllSchedule){
            Schedule_response schedule_dto=new Schedule_response();
            schedule_dto.setIdClass(y.getIdClass());
            schedule_dto.setStartTime(y.getStartTime());
            schedule_dto.setEndTime(y.getEndTime());
            ClassesEntity classes = classRespository.findById(y.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại "));
            schedule_dto.setNameClass(classes.getClassName());
            schedule_dto.setThu(y.getThu());
            schedule_dto.setNameRoom(y.getNameRoom());
            schedule_dto.setId(y.getId());
            CourseEntity course_teacher = courseRespository.findById(classes.getIdCourses()).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
            schedule_dto.setNameCourse(course_teacher.getNameCourse()); 
            listSchedule_dto.add(schedule_dto);
        }
        return listSchedule_dto;
    }

        @Override
        public List<Schedule_response> getScheduleForTeacher() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username=auth.getName();
            User_Entity user=UsersRepository.findByUsername(username);
            if(user==null){
                throw new ResourceNotFoundException("Không tồn tại User");
            }
            Teacher_Entity teacher=teacherRespository.findById(user.getId()).orElseThrow(()->new ResourceNotFoundException("Giáo viên không tồn tại"));
            List<ClassesEntity> class_entity=classRespository.findByIdTeachers(teacher.getId());
             // tim ten mon hoc ung voi teacher
                                                            
    
            if(class_entity==null){
                throw new ResourceNotFoundException("không tìm thấy lớp có giáo viên " +teacher.getFullName() +" dạy");
            }
            List<Schedule_Entity> listSchedule = new ArrayList<>();
            for(ClassesEntity x : class_entity){
                List<Schedule_Entity > schedule = scheduleRespository.findByIdClass(x.getId());
                listSchedule.addAll(schedule);
            }
            List<Schedule_response> listSchedule_dto=new ArrayList<>();
            for(Schedule_Entity y : listSchedule){
                Schedule_response schedule_dto=new Schedule_response();
                schedule_dto.setIdClass(y.getIdClass());
                schedule_dto.setStartTime(y.getStartTime());
                schedule_dto.setEndTime(y.getEndTime());
                ClassesEntity classes = classRespository.findById(y.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại "));
                schedule_dto.setNameClass(classes.getClassName());
                schedule_dto.setThu(y.getThu());
                schedule_dto.setNameRoom(y.getNameRoom());
                schedule_dto.setId(y.getId());
                CourseEntity course_teacher = courseRespository.findById(classes.getIdCourses()).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
                schedule_dto.setNameCourse(course_teacher.getNameCourse());
                listSchedule_dto.add(schedule_dto);

            }
            return listSchedule_dto;
        }
}
