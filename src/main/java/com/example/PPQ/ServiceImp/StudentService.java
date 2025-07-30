package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.ForbiddenException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Projection_Interface.StudentCoreView;
import com.example.PPQ.Payload.Request.StudentRequest;
import com.example.PPQ.Payload.Response.StudentDTO;
import com.example.PPQ.Service.StudentServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
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
    public List<StudentDTO> getAllStudents() {
        List<StudentDTO> student_dto = new ArrayList<>();
        List<StudentEntity> student=studentRespository.findAll();
        if(student.isEmpty()) {
            throw new ResourceNotFoundException("Hệ thống chưa tồn tại học sinh ");
        }
        for(StudentEntity s:student){
            StudentDTO std_dto=new StudentDTO();
            std_dto.setId(s.getId());
            std_dto.setFullName(s.getFullName());
            std_dto.setPhoneNumber(s.getPhoneNumber());
            student_dto.add(std_dto);
        }
        return student_dto;
    }

    @Override
    public StudentDTO getStudentById(int id) {
        StudentDTO std_dto=new StudentDTO();
        StudentEntity studentEntity=studentRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Học sinh không tồn tại"));
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
    public void updateStudent(int id,StudentRequest student) {
        StudentEntity studentEntity=studentRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("User không tồn tại"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String userNameRequest = usersRepository.findUserNameById(id);
        if(!username.equals(userNameRequest)) {
            throw new ForbiddenException("Bạn không được quyền chỉnh sửa học sinh này ");
        }
        if(student.getfullName()!=null){
           studentEntity.setFullName(student.getfullName());
       }
        if(student.getPhoneNumber()!=null){
            studentEntity.setPhoneNumber(student.getPhoneNumber());
        }
            studentRespository.save(studentEntity);
    }
    @Transactional
    @Override
    public void deleteStudent(int id) {
            if (!studentRespository.existsById(id)) {
                throw new ResourceNotFoundException("Không thể xóa !Học sinh không tồn tại");
            }
             // giảm studentCur ở bảng class đi 1 người
            studentRespository.decreaseCurrentStudent(id);
            // xóa student ở bảng trung gian
            courseStudentClassRepository.deleteCourseStudentClassByIdStudent(id);
            studentRespository.deleteById(id);

           //set role từ student xuống user
            usersRepository.setRolesUsers("USER",id);


    }

    @Override
    public List<StudentDTO> getAllStudentByClass(int class_id) {
        List<StudentCoreView> students = studentRespository.findStudentByClassId(class_id);
        if(students.isEmpty()) {
            throw new ResourceNotFoundException("Học sinh không tồn tại");
        }
        List<StudentDTO> listStudentDTO  = new ArrayList<>();
        for(StudentCoreView s:students){
            StudentDTO std_dto=new StudentDTO();
            std_dto.setId(s.getId());
            std_dto.setFullName(s.getFullName());
            std_dto.setPhoneNumber(s.getPhoneNumber());
            Float score1 = s.getScore1() == null ? 0f : s.getScore1();
            Float score2 = s.getScore2() == null ? 0f : s.getScore2();
            Float score3 = s.getScore3() == null ? 0f : s.getScore3();
            Integer absent = s.getAbsentDays() == null ? 0 : s.getAbsentDays();
            Integer attented = s.getAttentedDay() == null ? 0 : s.getAttentedDay();
            Float scoreHomework = s.getScoreHomework() == null ? 0f : s.getScoreHomework();
            std_dto.setScore1(score1);
            std_dto.setScore2(score2);
            std_dto.setScore3(score3);
            std_dto.setScoreHomework(scoreHomework);
            std_dto.setAbsentDays(absent);
            std_dto.setAttentedDay(attented);
            Float totalScore = ( score1 + score2 + 2*score3 + scoreHomework) / 5f;
            std_dto.setTotalScore(totalScore);
            if(totalScore < 6  || absent > 4  ){
                std_dto.setResult("fail");
            }
            else{
                std_dto.setResult("pass");
            }
            listStudentDTO.add(std_dto);
        }
        return listStudentDTO;
    }

    @Override
    public StudentDTO myInfo() {
        var context = SecurityContextHolder.getContext();  // lay thong tin user dang dang nhap tai contextholder
        String username = context.getAuthentication().getName();  // lay ra username
       StudentEntity s = studentRespository.findByUserName(username);
        if(s==null){
            throw new ResourceNotFoundException("Học sinh không tồn tại");
        }
       StudentDTO std_dto = new StudentDTO();
        std_dto.setId(s.getId());
        std_dto.setPhoneNumber(s.getPhoneNumber());
        std_dto.setFullName(s.getFullName());

        return std_dto;
    }


    @Override
    public List<StudentDTO> searchBynameAndPhoneNumber(String name, String phoneNumber) {
        List<StudentDTO> student_dto = new ArrayList<>();
        List<StudentEntity> student=studentRespository.searchByNameAndPhoneNumber(name,phoneNumber);
        if(student.isEmpty()){
            throw new ResourceNotFoundException("Hệ thống chưa tồn tại học sinh "); }
        for(StudentEntity s:student){
            StudentDTO std_dto=new StudentDTO();
            std_dto.setFullName(s.getFullName());
            std_dto.setId(s.getId());
            std_dto.setPhoneNumber(s.getPhoneNumber());
            student_dto.add(std_dto);
        }

        return student_dto;

    }
}
