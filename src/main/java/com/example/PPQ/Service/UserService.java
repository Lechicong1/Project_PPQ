package com.example.PPQ.Service;

import com.example.PPQ.Entity.Roles_Entity;
import com.example.PPQ.Entity.Teacher_Entity;
import com.example.PPQ.Entity.User_Entity;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.UsersRequest;
import com.example.PPQ.Payload.Response.Users_response;
import com.example.PPQ.Service_Imp.UserServiceImp;
import com.example.PPQ.respository.Roles_respository;
import com.example.PPQ.respository.TeacherRespository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
    public class UserService implements UserServiceImp {
    @Autowired
    UsersRepository users_repository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    Roles_respository roles_repository;
    @Autowired
    TeacherRespository teachers_repository;
    @Override
    public List<Users_response> getAllUsers() {
        List<User_Entity> users = users_repository.getAllUsersBasic();
        List<Users_response> list = new ArrayList<>();
        for (User_Entity user : users) {
            Users_response userResponse = new Users_response();
            userResponse.setUserId(user.getId());
            userResponse.setUserName(user.getUsername());
            Roles_Entity rolesEntity=roles_repository.findById(user.getIdRoles()).orElseThrow(()->new ResourceNotFoundException("Không tồn tại role"));
            //tra ve ten role thay vi id
            userResponse.setRoleName(rolesEntity.getRoleName());
            list.add(userResponse);
        }
        return list;
    }

    @Override
    public boolean deleteUsers(int id) {
        if(!users_repository.existsById(id)){
            throw new ResourceNotFoundException("User không tồn tại");
        }
        try{
            users_repository.deleteById(id);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi xoa user " + e.getMessage());
            return false;
        }

    }

    @Override
    public boolean updateUsers(int id,UsersRequest usersRequest) {
        User_Entity user = users_repository.findById(id).orElseThrow(()->new ResourceNotFoundException("User không tồn tại"));
        try{
           if(usersRequest.getPassWord()!=null){
               user.setPassword(passwordEncoder.encode(usersRequest.getPassWord()));
           }

           if(usersRequest.getIdRoles()!=null){
               user.setIdRoles(usersRequest.getIdRoles());
           }

            users_repository.save(user);
            return true;
        }
        catch(Exception e){
            System.out.println("co loi khi sua user " + e.getMessage());
            return false;
        }

    }

    @Override
    public boolean addUsers(UsersRequest usersRequest) {
        User_Entity user = new User_Entity();
        user.setIdRoles(usersRequest.getIdRoles());
        user.setUsername(usersRequest.getUserName());
        user.setPassword(passwordEncoder.encode(usersRequest.getPassWord()));
        try{
            users_repository.save(user);
            return true;
        }
        catch(Exception e){
            return false;
        }

    }

    @Override
    public List<Users_response> getAllUserByRoleTeacher() {
        Roles_Entity role_teacher=roles_repository.findByRoleName("TEACHER");
        if(role_teacher==null){
            throw new ResourceNotFoundException("Role Teacher không tồn tại");
        }
        List<User_Entity> ListUserEntity= users_repository.findByIdRoles(role_teacher.getId());

        List<Users_response> list = new ArrayList<>();
        for (User_Entity user : ListUserEntity) {
            // tim xem id_user da co trong bảng teacher chưa (nếu có rồi sẽ k hiện lên combobox nữa để tránh 2 giáo viên chung 1 id_user)
            Teacher_Entity teacher=teachers_repository.findByIdUsers(user.getId());
            if(teacher!=null) continue;
            Users_response userResponse = new Users_response();
            userResponse.setUserId(user.getId());
            userResponse.setUserName(user.getUsername());
            list.add(userResponse);
        }
        if(list.isEmpty()){
            throw new ResourceNotFoundException("Không tồn tại user có role giáo viên ");
        }
        return list;
    }

//    @Override
//    public Users_response myInfo() {
//        var context= SecurityContextHolder.getContext();  // lay thong tin user dang dang nhap tai contextholder
//        String username=context.getAuthentication().getName();  // lay ra username
//        User_Entity users=users_repository.findByUsername(username);
//        Users_response  userDTO = new Users_response();
//        userDTO.setId(users.getId());
//        userDTO.setUser_name(users.getUsername());
//        userDTO.setIdRoles(users.getIdRoles());
//        return userDTO;
//    }

}
