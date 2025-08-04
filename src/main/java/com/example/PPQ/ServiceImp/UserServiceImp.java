package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.*;
import com.example.PPQ.Exception.BusinessLogicException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Projection_Interface.UserView;
import com.example.PPQ.Payload.Request.UsersRequest;
import com.example.PPQ.Payload.Request.changePasswordRequest;
import com.example.PPQ.Payload.Response.UserDTO;
import com.example.PPQ.Service.UserService;
import com.example.PPQ.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserServiceImp implements UserService {
    @Autowired
    UsersRepository users_repository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roles_repository;
    @Autowired
    TeacherRepository teacherRepo;
    @Autowired
    StudentRepository studentRepo;
    @Autowired
    CourseStudentClassRepository courseStudentClassRepo;

    @Override
    public void setDefaultPassword(String username) {
        UserEntity user = users_repository.findByUsername(username);
        user.setPassword(passwordEncoder.encode("Lechicong"));
        users_repository.save(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserView> listUsers = users_repository.getAllUsersBasic();
        return listUsers.stream()
                .map(user -> new UserDTO(user))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUsers(int id) {
        if (!users_repository.existsById(id)) {
            throw new ResourceNotFoundException("User không tồn tại");
        }
        // xoa iduser o bang student truoc
        StudentEntity student = studentRepo.findByIdUsers(id);
        if(student!=null) {
            courseStudentClassRepo.deleteCourseStudentClassByIdStudent(student.getId());
            studentRepo.deleteById(student.getId());
        }
        teacherRepo.deleteTeacherByIdUsers(id);
        users_repository.deleteById(id);

    }
    @Transactional
    @Override
    public void updateUsers(int id,UsersRequest usersRequest) {
        UserEntity user = users_repository.findById(id).orElseThrow(()->new ResourceNotFoundException("User không tồn tại"));
            if(usersRequest.getPassWord()!=null)  user.setPassword(passwordEncoder.encode(usersRequest.getPassWord()));
            if(usersRequest.getIdRoles()!=null)   user.setIdRoles(usersRequest.getIdRoles());
            users_repository.save(user);
    }
    @Transactional
    @Override
    public void addUsers(UsersRequest usersRequest) {
        UserEntity user = new UserEntity();
        user.setIdRoles(usersRequest.getIdRoles());
        user.setUsername(usersRequest.getUserName());
        user.setPassword(passwordEncoder.encode(usersRequest.getPassWord()));
        users_repository.save(user);
    }

    @Override
    public List<UserDTO> getAllUserByRoleTeacher() {
        RolesEntity role_teacher=roles_repository.findByRoleName("TEACHER");
        if(role_teacher==null){
            throw new ResourceNotFoundException("Role Teacher không tồn tại");
        }
        List<UserEntity> usersWithTeacherRole= users_repository.findByIdRoles(role_teacher.getId());
        if(usersWithTeacherRole.isEmpty()){
            throw new ResourceNotFoundException("Không tồn tại user có role giáo viên ");
        }
        Set<Integer> idUser = usersWithTeacherRole.stream().map(UserEntity::getId).collect(Collectors.toSet());
        List<TeacherEntity> listTeacher = teacherRepo.findAllByIdUsersIn((idUser));
        Map<Integer, TeacherEntity> mapTeacher = listTeacher.stream().collect(Collectors.toMap(TeacherEntity::getIdUsers, Function.identity()));
        List<UserDTO> list = new ArrayList<>();
        for (UserEntity user : usersWithTeacherRole) {
            // tim xem id_user da co trong bảng teacher chưa (nếu có rồi sẽ k hiện lên combobox nữa để tránh 2 giáo viên chung 1 id_user)
            TeacherEntity teacher=mapTeacher.get(user.getId());
            if(teacher!=null) continue;
            UserDTO userResponse = new UserDTO();
            userResponse.setUserId(user.getId());
            userResponse.setUserName(user.getUsername());
            list.add(userResponse);
        }
        return list;
    }
    @Transactional
    @Override
    public void changePassword(changePasswordRequest changePasswordRequest) {
        var context = SecurityContextHolder.getContext();  // lay thong tin user dang dang nhap tai contextholder
        String username = context.getAuthentication().getName();  // lay ra username
        UserEntity users = users_repository.findByUsername(username);
        if(users==null){
            throw new ResourceNotFoundException("User không tồn tại");
        }
        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(), users.getPassword())){
            throw new BusinessLogicException("Mật khẩu cũ không đúng ");}
        if(passwordEncoder.matches(changePasswordRequest.getNewPassword(),users.getPassword() )){
            throw new BusinessLogicException("Mật khẩu mới không được trùng với mật khẩu cũ ");}
        if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())){
            throw new BusinessLogicException("Mật khẩu xác nhận không khớp với mật khẩu mới");
        }
        users.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        users_repository.save(users);
    }
    @Override

    public List<UserDTO> findUserByUsernameAndRole(String username,Integer idRole) {
        List<UserView> listuser = users_repository.findByUsernameAndRoles(username,idRole);
        return listuser.stream()
                .map(c-> new UserDTO(c))
                .collect(Collectors.toList());
    }



}