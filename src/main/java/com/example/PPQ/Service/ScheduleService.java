package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.BusinessLogicException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.ScheduleRequest;
import com.example.PPQ.Payload.Response.ScheduleDTO;
import com.example.PPQ.Service_Imp.ScheduleServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        List<ScheduleEntity> schedule=scheduleRespository.findAll();
        Set<Integer> ListClassId = schedule.stream().map(ScheduleEntity::getIdClass).collect(Collectors.toSet());
        List<ClassesEntity> listClasses=classRespository.findAllByIdIn(ListClassId);
        Map<Integer,ClassesEntity> mapClasses = listClasses.stream().collect(Collectors.toMap(ClassesEntity::getId, Function.identity()));
        if(schedule.isEmpty())
            throw new ResourceNotFoundException("Không tồn tại lịch học");
        for(ScheduleEntity sc:schedule){
            ScheduleDTO schedule_dto=new ScheduleDTO();
            schedule_dto.setIdClass(sc.getIdClass());
            schedule_dto.setId(sc.getId());
            schedule_dto.setStartTime(sc.getStartTime());
            schedule_dto.setEndTime(sc.getEndTime());
            ClassesEntity classes =mapClasses.get(schedule_dto.getIdClass());
            if(classes==null)
                throw new ResourceNotFoundException("Lớp học không tồn tại ");
            schedule_dto.setNameClass(classes.getClassName());
            schedule_dto.setThu(sc.getThu());
            schedule_dto.setNameRoom(sc.getNameRoom());
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
    @Cacheable(value = "scheduleForStudent", key = "#username", sync = true)
    @Override
    public List<ScheduleDTO> getScheduleForStudent(String username) {

        UserEntity user=UsersRepository.findByUsername(username);
        if(user==null){
            throw new ResourceNotFoundException("Không tồn tại User");
        }
        StudentEntity student = studentRespository.findById(user.getId()).orElseThrow(()->new ResourceNotFoundException("Học sinh không tồn tại")) ;
        List<CourseStudentClassEntity> courseStudentClassEntities=courseStudentClassRepository.findByIdStudent(student.getId());
        if(courseStudentClassEntities.isEmpty()){
            throw new ResourceNotFoundException("Khóa học này chưa có sinh viên đăng kí ");
        }
        List<Integer> listIdClass = courseStudentClassEntities.stream().map(CourseStudentClassEntity::getIdClass).collect(Collectors.toList());
        List<ScheduleEntity> AllSchedule=scheduleRespository.findByIdClassIn(listIdClass);
        List<ClassesEntity> listClasses=classRespository.findAllById(listIdClass);
        Map<Integer,ClassesEntity> mapClasses = listClasses.stream().collect(Collectors.toMap(ClassesEntity::getId, Function.identity()));
        List<Integer> listIdcourse =listClasses.stream().map(ClassesEntity::getIdCourses).collect(Collectors.toList());
        List<CourseEntity> listCourses = courseRespository.findAllById(listIdcourse);
        Map<Integer, CourseEntity> mapCourses = listCourses.stream()
                .collect(Collectors.toMap(CourseEntity::getId, Function.identity()));
        List<ScheduleDTO> listSchedule_dto=new ArrayList<>();
        for(ScheduleEntity y : AllSchedule){
            ScheduleDTO schedule_dto=new ScheduleDTO();
            schedule_dto.setIdClass(y.getIdClass());
            schedule_dto.setStartTime(y.getStartTime());
            schedule_dto.setEndTime(y.getEndTime());
            ClassesEntity classesEntity=mapClasses.get(schedule_dto.getIdClass());
            if (classesEntity == null) {
                throw new ResourceNotFoundException("Lớp học không tồn tại ");
            }
            schedule_dto.setNameClass(classesEntity.getClassName());
            schedule_dto.setThu(y.getThu());
            schedule_dto.setNameRoom(y.getNameRoom());
            schedule_dto.setId(y.getId());
            CourseEntity courseEntity =mapCourses.get(classesEntity.getIdCourses());
            if (courseEntity == null) {
                throw new ResourceNotFoundException("Khóa học không tồn tại ");
            }
            schedule_dto.setNameCourse(courseEntity.getNameCourse());

            listSchedule_dto.add(schedule_dto);
        }
        return listSchedule_dto;
    }
    @Cacheable(value = "scheduleForTeacherDTO", key = "#username", sync = true)

    // value la ten vung nho trong cache , key la ten con ben trong vung nho value
        @Override
        public List<ScheduleDTO> getScheduleForTeacher(String username) {
            List<ScheduleDTO> listSchedule_dto=new ArrayList<>();
               UserEntity user=UsersRepository.findByUsername(username);
               if(user==null){
                   throw new ResourceNotFoundException("Không tồn tại User");
               }
               TeacherEntity teacher=teacherRespository.findById(user.getId()).orElseThrow(()->new ResourceNotFoundException("Giáo viên không tồn tại"));
               List<ClassesEntity> listClasses=classRespository.findByIdTeachers(teacher.getId());
               if(listClasses==null){
                   throw new ResourceNotFoundException("không tìm thấy lớp có giáo viên " +teacher.getFullName() +" dạy");
               }
               List<Integer> listClassId =listClasses.stream().map(ClassesEntity::getId).toList();
               // tim tat ca lich hoc co classid trong listClassId
               List<ScheduleEntity> listSchedule = scheduleRespository.findByIdClassIn(listClassId);
               Map<Integer,ClassesEntity> mapClasses = listClasses.stream().collect(Collectors.toMap(ClassesEntity::getId, Function.identity()));
               // lay cac idcourse tu class
                List<Integer> listCourseId = listClasses.stream().map(ClassesEntity::getIdCourses).toList();
                List<CourseEntity> listCourses = courseRespository.findAllById(listCourseId);
                 Map<Integer, CourseEntity> mapCourses = listCourses.stream()
                .collect(Collectors.toMap(CourseEntity::getId, Function.identity()));

            for(ScheduleEntity y : listSchedule) {
                   ScheduleDTO schedule_dto = new ScheduleDTO();
                   schedule_dto.setIdClass(y.getIdClass());
                   schedule_dto.setStartTime(y.getStartTime());
                   schedule_dto.setEndTime(y.getEndTime());
                   ClassesEntity classes = mapClasses.get(y.getIdClass());
                   if (classes == null) {
                      throw new ResourceNotFoundException("Lớp học không tồn tại ");
                              }
                   schedule_dto.setNameClass(classes.getClassName());

                   schedule_dto.setThu(y.getThu());
                   schedule_dto.setNameRoom(y.getNameRoom());
                   schedule_dto.setId(y.getId());
                  CourseEntity course = mapCourses.get(classes.getIdCourses());
                  if (course == null) {
                      throw new ResourceNotFoundException("Khóa học không tồn tại ");
                              }
                  schedule_dto.setNameCourse(course.getNameCourse());

            listSchedule_dto.add(schedule_dto);
               }
            return listSchedule_dto;
        }

    @Override
    public List<ScheduleDTO> findScheduleByDayOfWeekAndClass(String thu, Integer idClass) {
        List<ScheduleDTO> listSchedule_dto = new ArrayList<>();
        List<ScheduleEntity> schedule=scheduleRespository.findScheduleByThuAndIdClass(thu, idClass);
        if(schedule.isEmpty()){
            throw new ResourceNotFoundException("Không tồn tại lịch học");
        }
        Set<Integer> ListClassId = schedule.stream().map(ScheduleEntity::getIdClass).collect(Collectors.toSet());
        List<ClassesEntity> listClasses=classRespository.findAllByIdIn(ListClassId);
        Map<Integer,ClassesEntity> mapClasses = listClasses.stream().collect(Collectors.toMap(ClassesEntity::getId, Function.identity()));

        for(ScheduleEntity sc:schedule){
            ScheduleDTO schedule_dto=new ScheduleDTO();
            schedule_dto.setIdClass(sc.getIdClass());
            schedule_dto.setId(sc.getId());
            schedule_dto.setStartTime(sc.getStartTime());
            schedule_dto.setEndTime(sc.getEndTime());
            ClassesEntity classes =mapClasses.get(schedule_dto.getIdClass());
            if(classes==null)
                throw new ResourceNotFoundException("Lớp học không tồn tại ");
            schedule_dto.setNameClass(classes.getClassName());
            schedule_dto.setThu(sc.getThu());
            schedule_dto.setNameRoom(sc.getNameRoom());
            listSchedule_dto.add(schedule_dto);
        }
        return listSchedule_dto;
    }


}
