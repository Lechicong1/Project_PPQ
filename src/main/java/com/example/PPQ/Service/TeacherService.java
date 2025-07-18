package com.example.PPQ.Service;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.TeacherRequest;
import com.example.PPQ.Payload.Response.TeacherDTO;
import com.example.PPQ.Service_Imp.TeacherServiceImp;
import com.example.PPQ.respository.ClassRespository;
import com.example.PPQ.respository.Roles_respository;
import com.example.PPQ.respository.TeacherRespository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
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
    public TeacherDTO myInfo() {
        var context = SecurityContextHolder.getContext();  // lay thong tin user dang dang nhap tai contextholder
        String username = context.getAuthentication().getName();  // lay ra username
        UserEntity users = usersRepository.findByUsername(username);
        TeacherEntity teacher = teacherRespository.findById(users.getId()).orElseThrow(() -> new ResourceNotFoundException("Giáo viên không tồn tại"));
        TeacherDTO teacher_dto = new TeacherDTO();
        String Url =baseUrl+  "/upload/teachers/";
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
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("upload/teachers");
            try{
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e){
                e.printStackTrace();
            }

            TeacherEntity teacher = new TeacherEntity();
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
            RolesEntity roles_teacher = rolesRespository.findByRoleName("TEACHER");
            if (roles_teacher == null) {
                throw new ResourceNotFoundException("Roles Teacher không tồn tại");
            }
            userEntity.setIdRoles(roles_teacher.getId());
            teacherRespository.save(teacher);
            usersRepository.save(userEntity);
    }

    @Override
    public void deleteTeacher(int id) {
            TeacherEntity teacherDelete=teacherRespository.findById(id).orElseThrow(()->new ResourceNotFoundException("Không tồn tại giáo viên để xóa"));
            // set role ở bảng user từ teacher thành user
            // tim role user
            RolesEntity roleUser=rolesRespository.findByRoleName("USER");
            if(roleUser==null) {
                throw new ResourceNotFoundException("Không tồn tại ROLE USER");
            }
            UserEntity user =usersRepository.findById(teacherDelete.getIdUsers()).orElseThrow(()->new ResourceNotFoundException("User không tồn tại"));
            user.setIdRoles(roleUser.getId());
            //cho idTeacher o bang class la null
            List<ClassesEntity> classesEntity=classRespository.findByIdTeachers(id);
            if(!classesEntity.isEmpty()) {
                for (ClassesEntity cls : classesEntity) {
                    cls.setIdTeachers(null);}
                    classRespository.saveAll(classesEntity);
            }
            // Lấy tên file ảnh
            String fileName = teacherDelete.getImagePath(); // ví dụ: "abc123.jpg"
            String fullPath = "upload/teachers/" + fileName;
            //xoa anh
            File file = new File(fullPath);
            if (file.exists()) {
                file.delete();
            }
            teacherRespository.deleteById(id);
            usersRepository.save(user);
    }
    @Override
    public void updateTeacher(int id, TeacherRequest teacherRequest, MultipartFile file) {
        TeacherEntity teacher = teacherRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Giáo viên không tồn tại"));


        if (teacherRequest.getEducationLevel() != null)
            teacher.setEducationLevel(teacherRequest.getEducationLevel());

        if (teacherRequest.getDescription() != null)
            teacher.setDescription(teacherRequest.getDescription());

        if (teacherRequest.getFullName() != null)
            teacher.setFullName(teacherRequest.getFullName());

        if (teacherRequest.getPhoneNumber() != null)
            teacher.setPhoneNumber(teacherRequest.getPhoneNumber());

        if (teacherRequest.getEmail() != null)
            teacher.setEmail(teacherRequest.getEmail());

        /* ---------- Xử lý ảnh ---------- */
        String imagePath = teacher.getImagePath();   // giữ nguyên mặc định

        if (file != null && !file.isEmpty()) {       // chỉ xử lý khi client gửi file mới
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("upload/teachers");

            try {
                if (Files.notExists(uploadPath)) {
                    Files.createDirectories(uploadPath);   // tạo thư mục nếu chưa có
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath,
                        StandardCopyOption.REPLACE_EXISTING);

                imagePath = fileName;                      // chỉ lưu tên file vào DB

            } catch (IOException ex) {
                // log và bọc lại nếu muốn báo lỗi cho client
                ex.printStackTrace();
                throw new RuntimeException("Không thể lưu tệp ảnh: " + ex.getMessage(), ex);
            }

        } else {
            // client không gửi file mới → convert path đầy đủ để trả ra API nếu cần
            imagePath = (imagePath != null)
                    ? "/upload/teachers/" + imagePath
                    : null;
        }

        teacher.setImagePath(imagePath);                   // gán giá trị cuối cùng
        teacherRespository.save(teacher);                  // lưu DB
    }

    @Override
    public List<TeacherDTO> getAllTeacher() {
        List<TeacherDTO> list=new ArrayList<>();
        List<TeacherEntity>  teacher=teacherRespository.findAll();
        if(teacher.isEmpty()){
            throw new ResourceNotFoundException("Hệ thống chưa có giáo viên nào ");
        }
        Set<Integer> listIdUsers = teacher.stream().map(TeacherEntity::getIdUsers).collect(Collectors.toSet());
        List<UserEntity> listUser = usersRepository.findAllByIdIn(listIdUsers);
        Map<Integer, UserEntity> mapUser = listUser.stream().collect(Collectors.toMap(UserEntity::getId, Function.identity()));
        for(TeacherEntity teacherEntity:teacher){
            String Url = baseUrl+"/upload/teachers/";
            TeacherDTO teacherResponse=new TeacherDTO();
            teacherResponse.setId(teacherEntity.getId());
            teacherResponse.setDescription(teacherEntity.getDescription());
            teacherResponse.setEducationLevel(teacherEntity.getEducationLevel());
            //tra ve username thay vi iduser
            UserEntity users=mapUser.get(teacherEntity.getIdUsers());
                  if(users==null)
                      throw new ResourceNotFoundException("Không tồn tại user");
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

//    @Override
//    public List<Teacher_response> getTeacherByName(String name) {
//        List<Teacher_Entity> teacher=teacherRespository.searchByName(name);
//
//        if(teacher.isEmpty()){
//            throw new ResourceNotFoundException("Hệ thống chưa có giáo viên nào");
//        }
//        String Url = baseUrl+"/upload/teachers/";
//        List<Teacher_response> teacherResponse= new ArrayList<>();
//        for(Teacher_Entity teacherEntity:teacher) {
//            Teacher_response respone= new Teacher_response();
//            respone.setId(teacherEntity.getId());
//            respone.setDescription(teacherEntity.getDescription());
//            respone.setEducationLevel(teacherEntity.getEducationLevel());
//            //tra ve username thay vi iduser
//            User_Entity users=usersRepository.findById(teacherEntity.getIdUsers()).orElseThrow(()-> new ResourceNotFoundException("Không tồn tại user"));
//            respone.setUserName(users.getUsername());
//            respone.setFullName(teacherEntity.getFullName());
//            respone.setPhoneNumber(teacherEntity.getPhoneNumber());
//            respone.setEmail(teacherEntity.getEmail());
//            respone.setImagePath(Url+teacherEntity.getImagePath());
////            respone.setStartDate(teacherEntity.getStartDate());
//            teacherResponse.add(respone);
//        }
//        return teacherResponse;
//    }
}
