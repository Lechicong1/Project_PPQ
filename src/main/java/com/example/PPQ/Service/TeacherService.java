package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.TeacherRequest;
import com.example.PPQ.Payload.Response.Teacher_response;
import com.example.PPQ.Payload.Response.Users_response;
import com.example.PPQ.Service_Imp.TeacherServiceImp;
import com.example.PPQ.respository.ClassRespository;
import com.example.PPQ.respository.Roles_respository;
import com.example.PPQ.respository.TeacherRespository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TeacherService implements TeacherServiceImp {
    @Autowired
    TeacherRespository teacherRespository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    ClassRespository classRespository;
    @Autowired
    Roles_respository rolesRespository;
    @Value("${app.base-url}")
    private String baseUrl;
    @Override
    public Teacher_response myInfo() {
        var context = SecurityContextHolder.getContext();  // lay thong tin user dang dang nhap tai contextholder
        String username = context.getAuthentication().getName();  // lay ra username
        User_Entity users = usersRepository.findByUsername(username);
        Teacher_Entity teacher = teacherRespository.findById(users.getId()).orElseThrow(() -> new ResourceNotFoundException("Giáo viên không tồn tại"));
        Teacher_response teacher_dto = new Teacher_response();
        String Url =baseUrl+ "/upload/teachers/";
        System.out.println(Url+teacher.getImagePath());
        teacher_dto.setId(teacher.getId());
        teacher_dto.setPhoneNumber(teacher.getPhoneNumber());
        teacher_dto.setFullName(teacher.getFullName());
        teacher_dto.setEducationLevel(teacher.getEducationLevel());
        teacher_dto.setDescription(teacher.getDescription());
        teacher_dto.setEmail(teacher.getEmail());
//        teacher_dto.setStartDate(teacher.getStartDate());
        teacher_dto.setImagePath(Url+teacher.getImagePath());
        return teacher_dto;
    }

    @Override
    public boolean addTeacher(TeacherRequest teacherRequest, MultipartFile file) {
        // check xem co ton tai user khong ( co user moi co teacher)
        User_Entity userEntity = usersRepository.findById(teacherRequest.getIdUsers()).orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        Teacher_Entity check = new Teacher_Entity();
        // kiem tra xem da ton tai giao vien chua(1 idUsers chi co 1 idTeacher)
        check = teacherRespository.findByIdUsers(teacherRequest.getIdUsers());
        if (check != null) {
            throw new DuplicateResourceException("Đã tồn tại giáo viên");
        }
        try {
            // Lưu ảnh vào thư mục
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("upload/teachers");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            Teacher_Entity teacher = new Teacher_Entity();
            teacher.setId(teacherRequest.getIdUsers());
            teacher.setEducationLevel(teacherRequest.getEducationLevel());
            teacher.setIdUsers(teacherRequest.getIdUsers());
            teacher.setDescription(teacherRequest.getDescription());
            teacher.setFullName(teacherRequest.getFullName());
            teacher.setPhoneNumber(teacherRequest.getPhoneNumber());
            teacher.setImagePath(fileName);
            teacher.setEmail(teacherRequest.getEmail());
//            teacher.setStartDate(teacherRequest.getStartDate());
            // sua lai role cua user do thanh Teacher
            // tim idRole teacher
            Roles_Entity roles_teacher = rolesRespository.findByRoleName("TEACHER");
            if (roles_teacher == null) {
                throw new ResourceNotFoundException("Roles Teacher không tồn tại");
            }
            userEntity.setIdRoles(roles_teacher.getId());

            teacherRespository.save(teacher);
            usersRepository.save(userEntity);
            return true;
        }
        catch (Exception e) {
            System.out.println("co loi khi them teacher" + e.getMessage());
            return false;
        }


    }



    @Override
    public boolean deleteTeacher(int id) {
        Teacher_Entity teacherDelete=teacherRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tồn tại giáo viên để xóa"));
        // set role ở bảng user từ teacher thành user
        // tim role user
        Roles_Entity roleUser=rolesRespository.findByRoleName("USER");
        User_Entity user =usersRepository.findById(teacherDelete.getIdUsers()).orElseThrow(()->new ResourceNotFoundException("User không tồn tại"));
        user.setIdRoles(roleUser.getId());
        //cho idTeacher o bang class la null
        List<ClassesEntity> classesEntity=classRespository.findByIdTeachers(id);
        System.out.println("test size class " + classesEntity.size());
        if(!classesEntity.isEmpty()) {
            try{
                for (ClassesEntity cls : classesEntity) {
                    cls.setIdTeachers(null);
                }
                classRespository.saveAll(classesEntity);  }
            catch (Exception e) {
                System.out.println("co loi khi xoa class " + e.getMessage());
            }

        }
        // Lấy tên file ảnh
        String fileName = teacherDelete.getImagePath(); // ví dụ: "abc123.jpg"
        String fullPath = "upload/teachers/" + fileName;
        //xoa anh
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
        try{
            teacherRespository.deleteById(id);
            usersRepository.save(user);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi xoa teacher");
            return false;
        }
    }
    @Override
    public boolean updateTeacher(int id, TeacherRequest teacherRequest, MultipartFile file) {
        try {
            Teacher_Entity teacher = teacherRespository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Giáo viên không tồn tại"));

            // Cập nhật các trường từ teacherRequest
            if (teacherRequest.getEducationLevel() != null) {
                teacher.setEducationLevel(teacherRequest.getEducationLevel());
            }
            if (teacherRequest.getDescription() != null) {
                teacher.setDescription(teacherRequest.getDescription());
            }
            if (teacherRequest.getFullName() != null) {
                teacher.setFullName(teacherRequest.getFullName());
            }
            if (teacherRequest.getPhoneNumber() != null) {
                teacher.setPhoneNumber(teacherRequest.getPhoneNumber());
            }
            if (teacherRequest.getEmail() != null) {
                teacher.setEmail(teacherRequest.getEmail());
            }

            // Xử lý ảnh
            String imagePath = teacher.getImagePath(); // Giữ imagePath cũ làm mặc định
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("upload/teachers");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = fileName; //ten file anh
                teacher.setImagePath(imagePath);
            } else {
                // Giữ đường dẫn đầy đủ nếu có
                imagePath = teacher.getImagePath() != null ? "/upload/teachers/" + teacher.getImagePath() : null;
            }

            teacherRespository.save(teacher);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi sửa teacher: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<Teacher_response> getAllTeacher() {
        List<Teacher_response> list=new ArrayList<>();
        List<Teacher_Entity >  teacher=teacherRespository.findAll();
        if(teacher.isEmpty()){
            throw new ResourceNotFoundException("Hệ thống chưa có giáo viên nào ");
        }
        for(Teacher_Entity teacherEntity:teacher){
            String Url = baseUrl+"/upload/teachers/";
            Teacher_response teacherResponse=new Teacher_response();
            teacherResponse.setId(teacherEntity.getId());
            teacherResponse.setDescription(teacherEntity.getDescription());
            teacherResponse.setEducationLevel(teacherEntity.getEducationLevel());
            //tra ve username thay vi iduser
            User_Entity users=usersRepository.findById(teacherEntity.getIdUsers()).orElseThrow(()-> new ResourceNotFoundException("Không tồn tại user"));
            teacherResponse.setUserName(users.getUsername());
            teacherResponse.setFullName(teacherEntity.getFullName());
            teacherResponse.setPhoneNumber(teacherEntity.getPhoneNumber());
            teacherResponse.setEmail(teacherEntity.getEmail());
            teacherResponse.setImagePath(Url+teacherEntity.getImagePath());
//            teacherResponse.setStartDate(teacherEntity.getStartDate());
            list.add(teacherResponse);
        }
        return list;
    }

    @Override
    public List<Teacher_response> getTeacherByName(String name) {
        List<Teacher_Entity> teacher=teacherRespository.searchByName(name);
        if(teacher.isEmpty()){
            throw new ResourceNotFoundException("Hệ thống chưa có giáo viên nào");
        }
        String Url = baseUrl+"/upload/teachers/";
        List<Teacher_response> teacherResponse= new ArrayList<>();
        for(Teacher_Entity teacherEntity:teacher) {
            Teacher_response respone= new Teacher_response();
            respone.setId(teacherEntity.getId());
            respone.setDescription(teacherEntity.getDescription());
            respone.setEducationLevel(teacherEntity.getEducationLevel());
            //tra ve username thay vi iduser
            User_Entity users=usersRepository.findById(teacherEntity.getIdUsers()).orElseThrow(()-> new ResourceNotFoundException("Không tồn tại user"));
            respone.setUserName(users.getUsername());
            respone.setFullName(teacherEntity.getFullName());
            respone.setPhoneNumber(teacherEntity.getPhoneNumber());
            respone.setEmail(teacherEntity.getEmail());
            respone.setImagePath(Url+teacherEntity.getImagePath());
//            respone.setStartDate(teacherEntity.getStartDate());
            teacherResponse.add(respone);
        }
        return teacherResponse;
    }
}
