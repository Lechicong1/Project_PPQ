package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.StudentRequest;
import com.example.PPQ.Payload.Response.StudentDTO;
import com.example.PPQ.Service_Imp.StudentServiceImp;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public void updateStudent(int id,StudentRequest student) {
        StudentEntity studentEntity=studentRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("User không tồn tại"));
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
            // xoa du lieu lien quan trong bang coursestudentclass truoc
            List<CourseStudentClassEntity> listCourseStudentClass = courseStudentClassRepository.findByIdStudent(id);
            // giam studentCurrent o bang class di 1 dua
            Set<Integer> listIdClass = listCourseStudentClass.stream().map(CourseStudentClassEntity::getIdClass).collect(Collectors.toSet());
            List<ClassesEntity> listClasses = classRespository.findAllByIdIn((listIdClass));
            Map<Integer, ClassesEntity> mapClass = listClasses.stream().collect(Collectors.toMap(ClassesEntity::getId, Function.identity()));
            for (CourseStudentClassEntity c : listCourseStudentClass) {
                ClassesEntity classes = mapClass.get(c.getIdClass());
                if (classes == null) {
                    throw new ResourceNotFoundException("Class không tồn tại");
                }
                classes.setCurrentStudents(classes.getCurrentStudents() - 1);

            }

            if (!listCourseStudentClass.isEmpty()) {
                courseStudentClassRepository.deleteAll(listCourseStudentClass);
            }

            studentRespository.deleteById(id);
            //set role cua user do ve user
            UserEntity userRoleStudent = usersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tồn tại user"));
            RolesEntity roleUser = rolesRespository.findByRoleName("USER");
            userRoleStudent.setIdRoles(roleUser.getId());
            usersRepository.save(userRoleStudent);

    }

    @Override
    public List<StudentDTO> getAllStudentByClass(int class_id) {
        List<CourseStudentClassEntity> student_class = courseStudentClassRepository.findByIdClass(class_id);

        Set<Integer> studentIds = student_class.stream()
                .map(CourseStudentClassEntity::getIdStudent)
                .collect(Collectors.toSet());

        List<StudentEntity> students = studentRespository.findAllByIdIn((studentIds));
        List<StudentDTO> listStudentDTO  = new ArrayList<>();
        for(StudentEntity s:students){
            StudentDTO std_dto=new StudentDTO();
            std_dto.setId(s.getId());
            std_dto.setFullName(s.getFullName());
            std_dto.setIdUsers(s.getIdUsers());
            std_dto.setPhoneNumber(s.getPhoneNumber());
            listStudentDTO.add(std_dto);
        }
        return listStudentDTO;
    }

    @Override
    public StudentDTO myInfo() {
        var context = SecurityContextHolder.getContext();  // lay thong tin user dang dang nhap tai contextholder
        String username = context.getAuthentication().getName();  // lay ra username
        UserEntity users = usersRepository.findByUsername(username);
       StudentEntity student = studentRespository.findById(users.getId()).orElseThrow(() -> new ResourceNotFoundException("Học sinh không tồn tại"));
        StudentDTO student_dto = new StudentDTO();
        student_dto.setId(student.getId());
        student_dto.setPhoneNumber(student.getPhoneNumber());
        student_dto.setFullName(student.getFullName());
        return student_dto;
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
