package com.example.PPQ.Service;

import com.example.PPQ.Payload.Request.UsersRequest;
import com.example.PPQ.Payload.Request.changePasswordRequest;
import com.example.PPQ.Payload.Response.PageResponse;
import com.example.PPQ.Payload.Response.UserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    void setDefaultPassword(String username);
    PageResponse<UserDTO> getAllUsers(Integer page,Integer size);
    void deleteUsers(int id);
    void updateUsers(int id, UsersRequest usersRequest);
    void addUsers(UsersRequest usersRequest);
    List<UserDTO> getAllUserByRoleTeacher();
    void changePassword(changePasswordRequest changePasswordRequest);
    PageResponse<UserDTO> findUserByUsernameAndRole(String username, Integer idrole, Integer page,Integer size);
//    Users_response myInfo();
}
