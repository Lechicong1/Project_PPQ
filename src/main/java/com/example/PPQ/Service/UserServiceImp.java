package com.example.PPQ.Service;

import com.example.PPQ.Payload.Request.UsersRequest;
import com.example.PPQ.Payload.Request.changePasswordRequest;
import com.example.PPQ.Payload.Response.UserDTO;

import java.util.List;

public interface UserServiceImp {
    void setDefaultPassword(String username);
    List<UserDTO> getAllUsers();
    void deleteUsers(int id);
    void updateUsers(int id, UsersRequest usersRequest);
    void addUsers(UsersRequest usersRequest);
    List<UserDTO> getAllUserByRoleTeacher();
    void changePassword(changePasswordRequest changePasswordRequest);
    List<UserDTO> findUserByUsernameAndRole(String username,Integer idrole);
//    Users_response myInfo();
}
