package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.StudentRequest;
import com.example.PPQ.Payload.Response.CourseRegisterRespone;
import com.example.PPQ.Payload.Response.Student_response;
import com.example.PPQ.Payload.Response.Teacher_response;
import com.example.PPQ.Service_Imp.StudentServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService implements StudentServiceImp {
    @Autowired
    StudentRespository studentRespository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    Roles_respository rolesRespository;
    @Autowired
    CourseStudentClassRepository courseStudentClassRepository;
    @Autowired
    ClassRespository classRespository;
    @Override
    public List<Student_response> getAllStudents() {
        List<Student_response> student_dto = new ArrayList<>();
        List<Student_Entity> student=studentRespository.findAll();
        if(student.isEmpty()) {
            throw new ResourceNotFoundException("Hệ thống chưa tồn tại học sinh ");
        }
        for(Student_Entity s:student){
            Student_response std_dto=new Student_response();
            std_dto.setId(s.getId());
            std_dto.setFullName(s.getFullName());
            std_dto.setPhoneNumber(s.getPhoneNumber());
            student_dto.add(std_dto);
        }

        return student_dto;
    }

    @Override
    public Student_response getStudentById(int id) {
        Student_response std_dto=new Student_response();
        Student_Entity studentEntity=studentRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Học sinh không tồn tại"));
        std_dto.setFullName(studentEntity.getFullName());
        std_dto.setIdUsers(studentEntity.getIdUsers());
        std_dto.setPhoneNumber(studentEntity.getPhoneNumber());
        return std_dto;
    }

//    @Override
//    public boolean addStudent(StudentRequest student) {
//        // check xem co ton tai user khong ( co user moi co student)
//        User_Entity userEntity=usersRepository.findById(student.getIdUsers()).orElseThrow(()->new ResourceNotFoundException("User không tồn tại"));
//        Student_Entity check=new Student_Entity();
//        // kiem tra xem da ton tai hoc sinh chua(1 idUsers chi co 1 idStudent)
//        check=studentRespository.findByIdUsers(student.getIdUsers());
//        if(check!=null){
//            throw new DuplicateResourceException("Học sinh đã tồn tại");
//
//        }
//        Student_Entity studentEntity=new Student_Entity();
//        studentEntity.setFullName(student.getfullName());
//        studentEntity.setIdUsers(student.getIdUsers());
//        studentEntity.setPhoneNumber(student.getPhoneNumber());
//        try{
//            studentRespository.save(studentEntity);
//            return true;
//        }
//        catch(Exception e){
//            System.out.println("co loi khi them student " + e.getMessage());
//            return false;
//        }
//
//    }

    @Override
    public boolean updateStudent(int id,StudentRequest student) {
        Student_Entity studentEntity=studentRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("User không tồn tại"));
       if(student.getfullName()!=null){
           studentEntity.setFullName(student.getfullName());
       }
        if(student.getPhoneNumber()!=null){
            studentEntity.setPhoneNumber(student.getPhoneNumber());
        }
        try{
            studentRespository.save(studentEntity);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi sua student " + e.getMessage());
            return false;
        }

    }

    @Override
    public boolean deleteStudent(int id) {
       if(!studentRespository.existsById(id)){
           throw new ResourceNotFoundException("Không thể xóa !Học sinh không tồn tại");
       }
       // xoa du lieu lien quan trong bang coursestudentclass truoc
        List<CourseStudentClassEntity> listcourse =courseStudentClassRepository.findByIdStudent(id);
       // giam studentCurrent o bang class di 1 dua
        for(CourseStudentClassEntity c : listcourse){
            ClassesEntity classes= classRespository.findById(c.getIdClass()).orElseThrow(()->new ResourceNotFoundException("Không tồn tại user"));
            classes.setCurrentStudents(classes.getCurrentStudents()-1);

        }

       if(!listcourse.isEmpty()){
           courseStudentClassRepository.deleteAll(listcourse);
       }
        try{
            studentRespository.deleteById(id);
            //set role cua user do ve user
            User_Entity userRoleStudent =usersRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tồn tại user"));
            Roles_Entity roleUser=rolesRespository.findByRoleName("USER");
            userRoleStudent.setIdRoles(roleUser.getId());
            usersRepository.save(userRoleStudent);
            // giam student cua lop do di 1 dua

            return true;

        }
        catch(Exception e){
            System.out.println("co loi khi xoa student " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Student_response> getAllStudentByClass(int class_id) {
        List<CourseStudentClassEntity> student_class = courseStudentClassRepository.findByIdClass(class_id);

        Set<Integer> studentIds = student_class.stream()
                .map(CourseStudentClassEntity::getIdStudent)
                .collect(Collectors.toSet());

        List<Student_Entity> students = studentRespository.findAllById(studentIds);

        return students.stream().map(student -> {
            Student_response dto = new Student_response();
            dto.setIdUsers(student.getIdUsers());
            dto.setFullName(student.getFullName());
            dto.setPhoneNumber(student.getPhoneNumber());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Student_response myInfo() {
        var context = SecurityContextHolder.getContext();  // lay thong tin user dang dang nhap tai contextholder
        String username = context.getAuthentication().getName();  // lay ra username
        User_Entity users = usersRepository.findByUsername(username);
       Student_Entity student = studentRespository.findById(users.getId()).orElseThrow(() -> new ResourceNotFoundException("Học sinh không tồn tại"));
        Student_response student_dto = new Student_response();
        student_dto.setId(student.getId());
        student_dto.setPhoneNumber(student.getPhoneNumber());
        student_dto.setFullName(student.getFullName());
        return student_dto;
    }


    @Override
    public List<Student_response> searchBynameAndPhoneNumber(String name,String phoneNumber) {
        List<Student_response> student_dto = new ArrayList<>();
        List<Student_Entity> student=studentRespository.searchByNameAndPhoneNumber(name,phoneNumber);
        if(student.isEmpty()){
            throw new ResourceNotFoundException("Hệ thống chưa tồn tại học sinh "); }
        for(Student_Entity s:student){
            Student_response std_dto=new Student_response();
            std_dto.setFullName(s.getFullName());
            std_dto.setId(s.getId());
            std_dto.setPhoneNumber(s.getPhoneNumber());
            student_dto.add(std_dto);
        }

        return student_dto;

    }
}
