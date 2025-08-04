package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.RegisterCourseRequest;
import com.example.PPQ.Payload.Response.CourseRegisterRespone;
import com.example.PPQ.Payload.Projection_Interface.CourseRegisterView;
import com.example.PPQ.Service.RegisterCourseService;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterCourseServiceImp implements RegisterCourseService {
    @Autowired
    UsersRepository UsersRepository;
    @Autowired
    StudentRepository StudentRespository;
    @Autowired
    CourseRepository courseRespository;
    @Autowired
    CourseStudentClassRepository courseStudentRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    RoleRepository rolesRepository;


    @Override
    public List<CourseRegisterRespone> getAllCourseRegister() {
        List<CourseRegisterView> courseRegisterViews = courseStudentRepository.findAllCourseRegister();

        return courseRegisterViews.stream()
                .map(courseRegisterView -> new CourseRegisterRespone(courseRegisterView))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseRegisterRespone> searchCourseRegister(Integer idCourse, String nameStudent, Integer idClass) {
        List<CourseRegisterView> courseRegisterViews = courseStudentRepository.searchCourseRegister(idCourse,nameStudent,idClass);
        return courseRegisterViews.stream()
                .map(courseRegisterView -> new CourseRegisterRespone(courseRegisterView))
                .collect(Collectors.toList());
    }

}
