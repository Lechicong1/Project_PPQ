package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Enums.Role;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ForbiddenException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.TeacherRequest;
import com.example.PPQ.Payload.Response.TeacherDTO;
import com.example.PPQ.Service.FileStorageService;
import com.example.PPQ.Service.TeacherService;
import com.example.PPQ.respository.ClassRepository;
import com.example.PPQ.respository.RoleRepository;
import com.example.PPQ.respository.TeacherRepository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Transactional
public class TeacherServiceImp implements TeacherService {

    @Autowired
    TeacherRepository teacherRespository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    ClassRepository classRespository;
    @Autowired
    RoleRepository rolesRespository;

    @Autowired
    FileStorageService fileStorageService;
    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public TeacherDTO myInfo() {
        var context = SecurityContextHolder.getContext();  // lay thong tin user dang dang nhap tai contextholder
        String username = context.getAuthentication().getName();  // lay ra username
        TeacherEntity teacherEntity = teacherRespository.findByUserName(username);
        TeacherDTO teacher_dto = new TeacherDTO(teacherEntity);
        String Url = baseUrl + "/upload/teachers/";
        teacher_dto.setImagePath(Url + teacherEntity.getImagePath());
        return teacher_dto;
    }

    @Override
    public void addTeacher(TeacherRequest teacherRequest, MultipartFile file) {
        // check xem co ton tai user khong ( co user moi co teacher)
        UserEntity userEntity = usersRepository.findById(teacherRequest.getIdUsers()).orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        TeacherEntity check = new TeacherEntity();
        // kiem tra xem da ton tai giao vien chua(1 idUsers chi co 1 idTeacher)
        check = teacherRespository.findByIdUsers(teacherRequest.getIdUsers());
        if (check != null) {
            throw new DuplicateResourceException("Đã tồn tại giáo viên");
        }
        // Lưu ảnh vào thư mục
        String fileName = fileStorageService.saveFile(file, "teachers");
        TeacherEntity teacher = new TeacherEntity(teacherRequest);
        teacher.setImagePath(fileName);
        teacherRespository.save(teacher);
        usersRepository.setRolesUsers(Role.TEACHER, userEntity.getId());
    }
    @Override
    public void deleteTeacher(int idTeacher) {
        TeacherEntity teacherDelete = teacherRespository.findById(idTeacher).orElseThrow(() -> new ResourceNotFoundException("Không tồn tại giáo viên để xóa"));
        // cap nhat role cua teacher ve thanh user trong bang user
        usersRepository.setRolesUsers(Role.USER, idTeacher);
        classRespository.setIdTeacherNull(idTeacher);
        // Lấy tên file ảnh
        String fileName = teacherDelete.getImagePath(); // ví dụ: "abc123.jpg"
        //xoa anh khoi server
        fileStorageService.deleteFile("teachers",fileName);
        teacherRespository.deleteById(idTeacher);

    }

    @Override
    public void updateTeacher(int id, TeacherRequest teacherRequest, MultipartFile file) {
        TeacherEntity teacher = teacherRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Giáo viên không tồn tại"));
        String userNameCur = SecurityContextHolder.getContext().getAuthentication().getName();
        String roleUserCur = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority();

        UserEntity userChange = usersRepository.findById(teacher.getIdUsers()).orElseThrow( ()-> new ResourceNotFoundException("User không tồn tại"));
        String userNameChange = userChange.getUsername();
        RolesEntity roleAdmin = rolesRespository.findByRoleName(Role.ADMIN);
        if (!userNameCur.equals(userNameChange) && !roleUserCur.equals(roleAdmin.getRoleName()))
            throw new ForbiddenException("Bạn không được quyền chỉnh sửa giáo viên này ");
        if (teacherRequest.getEducationLevel() != null) teacher.setEducationLevel(teacherRequest.getEducationLevel());
        if (teacherRequest.getDescription() != null) teacher.setDescription(teacherRequest.getDescription());
        if (teacherRequest.getFullName() != null) teacher.setFullName(teacherRequest.getFullName());
        if (teacherRequest.getPhoneNumber() != null) teacher.setPhoneNumber(teacherRequest.getPhoneNumber());
        if (teacherRequest.getEmail() != null) teacher.setEmail(teacherRequest.getEmail());

        String imagePath = teacher.getImagePath();   // giữ nguyên mặc định

        if (file != null && !file.isEmpty()) {       // chỉ xử lý khi client gửi file mới
            String fileName = fileStorageService.saveFile(file, "teachers");
            imagePath = fileName;
        }
        if(imagePath!=null) teacher.setImagePath(imagePath);
        teacherRespository.save(teacher);                  // lưu DB
    }

    @Override
    public List<TeacherDTO> getAllTeacher() {
        List<TeacherDTO> list = new ArrayList<>();
        List<TeacherEntity> teacher = teacherRespository.findAll();
        if (teacher.isEmpty()) throw new ResourceNotFoundException("Hệ thống chưa có giáo viên nào ");

        for (TeacherEntity teacherEntity : teacher) {
            String Url = baseUrl + "/upload/teachers/";
            TeacherDTO teacherResponse = new TeacherDTO(teacherEntity);
            teacherResponse.setImagePath(Url + teacherEntity.getImagePath());
            list.add(teacherResponse);
        }
        return list;
    }

}
