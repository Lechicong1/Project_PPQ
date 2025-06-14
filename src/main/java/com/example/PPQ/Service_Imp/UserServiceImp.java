package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.UsersRequest;
import com.example.PPQ.Payload.Response.Users_response;

import java.util.List;

public interface UserServiceImp {
    List<Users_response> getAllUsers();
    boolean deleteUsers(int id);
    boolean updateUsers(int id, UsersRequest usersRequest);
    boolean addUsers(UsersRequest usersRequest);
    List<Users_response> getAllUserByRoleTeacher();
//    Users_response myInfo();
}
