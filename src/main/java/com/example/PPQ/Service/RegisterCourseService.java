package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.RegisterCourseRequest;
import com.example.PPQ.Payload.Response.CourseRegisterRespone;
import com.example.PPQ.Payload.Projection_Interface.CourseRegisterView;
import com.example.PPQ.Service_Imp.RegisterCourseServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegisterCourseService implements RegisterCourseServiceImp {
    @Autowired
    UsersRepository UsersRepository;
    @Autowired
    StudentRespository StudentRespository;
    @Autowired
    CourseRespository courseRespository;
    @Autowired
    CourseStudentClassRepository courseStudentRepository;
    @Autowired
    ClassRespository classRepository;
    @Autowired
    Roles_respository rolesRepository;
    @Override
    public boolean RegisterCourse(int id_course, RegisterCourseRequest RegisterCourseRequest) {
        // luu thong tin user dang ki vao bang student , luu id_student va id_course vao bang trung gian
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserEntity user= UsersRepository.findByUsername(username);
        if(user==null) {
            throw new ResourceNotFoundException("User không tồn tại");
        }
        // kiem tra xem student da ton tai chua (vi du user a muon dang ki them 1 khoa hoc thi khong can tao moi 1 student nua )
        StudentEntity student_Entity = StudentRespository.findByIdUsers(user.getId());
        //kiem tra xem lop hoc co ton tai hay khong
        ClassesEntity classes = classRepository.findById(RegisterCourseRequest.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
        // kiem tra xem khoa hoc co ton tai khong
        CourseEntity course =courseRespository.findById(id_course).orElseThrow(()->new ResourceNotFoundException("Khóa học không tồn tại "));
        CourseStudentClassEntity course_StudentEntity = new CourseStudentClassEntity();
        if(student_Entity==null){ // them thong tin vao bang student
            student_Entity=new StudentEntity();
            student_Entity.setFullName(RegisterCourseRequest.getFullName());
            student_Entity.setPhoneNumber(RegisterCourseRequest.getPhoneNumber());
            student_Entity.setIdUsers(user.getId());
            student_Entity.setId(user.getId());
        }
        // kiem tra xem dang ki khoa hoc co bi trung khong
        CourseStudentKey key=new CourseStudentKey(id_course, user.getId(),RegisterCourseRequest.getIdClass());
        if(courseStudentRepository.existsById(key)){
            throw new DuplicateResourceException("Đã đăng kí khóa học này rồi");
        }
        // luu thong tin vao bang chung
        course_StudentEntity.setIdCourse(id_course);
        course_StudentEntity.setIdStudent(user.getId());
        course_StudentEntity.setEnrollmentDate(LocalDateTime.now());
        course_StudentEntity.setIdClass(RegisterCourseRequest.getIdClass());


        try{
            StudentRespository.save(student_Entity);
            courseStudentRepository.save(course_StudentEntity);
            // sau khi dang ki khoa hoc thanh cong set role cua user do thanh student
            RolesEntity roleStudent=rolesRepository.findByRoleName("STUDENT");
            user.setIdRoles(roleStudent.getId());
            UsersRepository.save(user);
            return true;
        }
        catch(Exception e) {
            System.out.println("co loi khi dang ki khoa hoc ");
            return false;
        }

    }

    @Override
    public List<CourseRegisterRespone> getAllCourseRegister() {
        List<CourseRegisterView> courseRegisterViews = courseStudentRepository.findAllCourseRegister();
        List<CourseRegisterRespone> courseRegisterRespones = new ArrayList<>();
        for(CourseRegisterView c:courseRegisterViews){
            CourseRegisterRespone courseRegisterRespone = new CourseRegisterRespone();
            courseRegisterRespone.setEnrollmentDate(c.getEnrollmentDate());
            courseRegisterRespone.setNameStudent(c.getFullName());
            courseRegisterRespone.setNameCourse(c.getNameCourse());
            courseRegisterRespone.setNameClass(c.getNameClass());
            courseRegisterRespones.add(courseRegisterRespone);
        }
        return courseRegisterRespones;
    }

    @Override
    public List<CourseRegisterRespone> searchCourseRegister(Integer idCourse, String nameStudent, Integer idClass) {
        List<CourseRegisterView> courseRegisterViews = courseStudentRepository.searchCourseRegister(idCourse,nameStudent,idClass);
        List<CourseRegisterRespone> courseRegisterRespones = new ArrayList<>();
        for(CourseRegisterView c:courseRegisterViews){
            CourseRegisterRespone courseRegisterRespone = new CourseRegisterRespone();
            courseRegisterRespone.setEnrollmentDate(c.getEnrollmentDate());
            courseRegisterRespone.setNameStudent(c.getFullName());
            courseRegisterRespone.setNameCourse(c.getNameCourse());
            courseRegisterRespone.setNameClass(c.getNameClass());
            courseRegisterRespones.add(courseRegisterRespone);
        }
        return courseRegisterRespones;
    }

}
