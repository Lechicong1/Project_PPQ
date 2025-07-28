package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.ClassRequest;
import com.example.PPQ.Payload.Response.ClassDTO;
import com.example.PPQ.Payload.Projection_Interface.ClassView;
import com.example.PPQ.Service_Imp.ClassServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ClassService implements ClassServiceImp {
    @Autowired
    ClassRespository classRepository;
    @Autowired
    CourseRespository CourseRepository;
    @Autowired
    TeacherRespository TeacherRepository;
    @Autowired
    ScheduleRespository scheduleRespository;
    @Autowired
    CourseStudentClassRepository courseStudentClassRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    private CourseRespository courseRespository;

    @Override
    public List<ClassDTO> getAllClasses() {
        List<ClassDTO> list = new ArrayList<ClassDTO>();
        List<ClassView> listclassView=classRepository.findAllClass();
        for(ClassView c:listclassView){
            ClassDTO class_dto=new ClassDTO();
            class_dto.setId(c.getId());
            class_dto.setClassName(c.getNameClass());
            class_dto.setStatus(c.getStatus());
            class_dto.setCurrentStudents(c.getCurrentStudents());
            class_dto.setNameTeacher(c.getNameTeacher());
            class_dto.setNameCourse(c.getNameCourse());
            class_dto.setMaxStudents(c.getMaxStudents());
            class_dto.setRoadMap(c.getRoadMap());
            list.add(class_dto);
        }
        return list;
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
        List<ClassDTO> list = new ArrayList<ClassDTO>();
        for(ClassView c:classViews){
            ClassDTO class_dto=new ClassDTO();
            class_dto.setId(c.getId());
            class_dto.setClassName(c.getNameClass());
            class_dto.setStatus(c.getStatus());
            class_dto.setCurrentStudents(c.getCurrentStudents());
            class_dto.setNameTeacher(c.getNameTeacher());
            class_dto.setNameCourse(c.getNameCourse());
            class_dto.setMaxStudents(c.getMaxStudents());
            list.add(class_dto);
        }
        return list;
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
        ClassesEntity c=new ClassesEntity();
        c.setClassName(classRequest.getClassName());
        c.setIdCourses(classRequest.getIdCourses());
        c.setMaxStudents(classRequest.getMaxStudents());
        c.setIdTeachers(classRequest.getIdTeachers());
        c.setStatus(classRequest.getStatus());
        c.setRoadMap(classRequest.getRoadMap());
        classRepository.save(c);

    }

    @Override
    public void updateClass(int id, ClassRequest classRequest) {
        ClassesEntity classes=classRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
        if(classRequest.getClassName()!=null){
            classes.setClassName(classRequest.getClassName());
        }
        if(classRequest.getIdCourses()!=null){
            classes.setIdCourses(classRequest.getIdCourses());
        }
        if(classRequest.getMaxStudents()!=null){
            classes.setMaxStudents(classRequest.getMaxStudents());
        }
        if(classRequest.getIdTeachers()!=null){
            classes.setIdTeachers(classRequest.getIdTeachers());
        }
       if(classRequest.getStatus()!=null){
           classes.setStatus(classRequest.getStatus());
       }
       if(classRequest.getRoadMap()!=null){
           classes.setRoadMap(classRequest.getRoadMap());
       }
       classRepository.save(classes);
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
        List<ClassDTO> listClassResponse=new ArrayList<>();
        List<ClassesEntity > listClassEntity=classRepository.findByIdCourses(idCourse);
        for(ClassesEntity c:listClassEntity){
            ClassDTO classresponse=new ClassDTO();
            classresponse.setId(c.getId());
            classresponse.setClassName(c.getClassName());
            listClassResponse.add(classresponse);
        }
        return listClassResponse;
    }
}
