package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.ClassRequest;
import com.example.PPQ.Payload.Response.ClassDTO;
import com.example.PPQ.Payload.Projection_Interface.ClassView;
import com.example.PPQ.Service.ClassService;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassServiceImp implements ClassService {
    @Autowired
    ClassRepository classRepository;
    @Autowired
    com.example.PPQ.respository.CourseRepository CourseRepository;
    @Autowired
    com.example.PPQ.respository.TeacherRepository TeacherRepository;
    @Autowired
    ScheduleRepository scheduleRespository;
    @Autowired
    CourseStudentClassRepository courseStudentClassRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    private CourseRepository courseRespository;

    @Override
    public List<ClassDTO> getAllClasses() {

        List<ClassView> listclassView=classRepository.findAllClass();
        return listclassView.stream()
                .map(c->new ClassDTO(c))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassDTO> getClassByIdTeacher() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        TeacherEntity teacher=TeacherRepository.findByUserName(username);
         if(teacher==null) throw  new ResourceNotFoundException("Giáo viên không tồn tại");
        List<ClassView> classViews=classRepository.findByIdTeachers(teacher.getId());
        if(classViews.isEmpty())
            throw new ResourceNotFoundException("Giáo viên chưa có lớp học nào");

        return classViews.stream()
                .map(c->new ClassDTO(c))
                .collect(Collectors.toList());
    }

        @Override
        public void addClasses(ClassRequest classRequest) {

            // kiem tra xem co ton tai idCourse khong
            CourseEntity course_entity=CourseRepository.findById(classRequest.getIdCourses()).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
            //Kiem tra ton tai id_teacher k
            TeacherEntity teacherEntity=TeacherRepository.findById(classRequest.getIdTeachers()).orElseThrow(()->new RuntimeException("Giáo viên không tồn tại"));
            //kiem tra xem lop da ton tai chua
            ClassesEntity classes=classRepository.findByClassName(classRequest.getClassName());
            if(classes!=null){
                throw new DuplicateResourceException("Đã tồn tại lớp học này rồi");
            }
            ClassesEntity c=new ClassesEntity(classRequest);
            classRepository.save(c);

        }

        @Override
        public void updateClass(int id, ClassRequest req) {

            ClassesEntity entity=classRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
            if (req.getClassName() != null) entity.setClassName(req.getClassName());
            if (req.getIdCourses() != null) entity.setIdCourses(req.getIdCourses());
            if (req.getMaxStudents() != null) entity.setMaxStudents(req.getMaxStudents());
            if (req.getIdTeachers() != null) entity.setIdTeachers(req.getIdTeachers());
            if (req.getStatus() != null) entity.setStatus(req.getStatus());
            if (req.getRoadMap() != null) entity.setRoadMap(req.getRoadMap());
           classRepository.save(entity);
        }

    @Override
    public void deleteClass(int id) {

        //xoa schedule truoc vi id_class dang la khoa ngoai o bang schedule
        if(!classRepository.existsById(id)){
            throw new ResourceNotFoundException("Lớp học không tồn tại");
        }
        scheduleRespository.deleteByIdClass(id);
        //xoa idclass o bang coursestudentclass
        courseStudentClassRepository.deleteCourseStudentClassByIdClass(id);
        classRepository.deleteById(id);
    }

    @Override
    public List<ClassDTO> getClassByCourse(int idCourse) {
        List<ClassesEntity > listClassEntity=classRepository.findByIdCourses(idCourse);
        return listClassEntity.stream().map(c->new ClassDTO(c)).collect(Collectors.toList());
    }

}
