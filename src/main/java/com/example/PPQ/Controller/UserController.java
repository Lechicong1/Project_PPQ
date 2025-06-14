package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.UsersRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Payload.Response.Users_response;
import com.example.PPQ.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping(produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(){
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        List<Users_response> ListUsers=userService.getAllUsers();
        if(!ListUsers.isEmpty()) {
            responseData.setData(ListUsers);
            responseData.setSuccess(true);
            status = HttpStatus.OK;

        }
        else{
            responseData.setData(null);
            responseData.setSuccess(false);
            status = HttpStatus.NOT_FOUND;

        }
        return ResponseEntity.status(status).body(responseData);
    }
    @GetMapping("/roleTeacher")
    public ResponseEntity<?> getAllUsersRoleTeacher(){
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        List<Users_response> ListUsers=userService.getAllUserByRoleTeacher() ;
        if(!ListUsers.isEmpty()) {
            responseData.setData(ListUsers);
            responseData.setSuccess(true);
            status = HttpStatus.OK;
        }
        else{
            responseData.setData(null);
            responseData.setSuccess(false);
            status = HttpStatus.NOT_FOUND;

        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id ,@Valid @RequestBody UsersRequest usersRequest){
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        if(userService.updateUsers(id,usersRequest)) {
            responseData.setSuccess(true);
            responseData.setData(usersRequest);
            status = HttpStatus.OK;
            responseData.setMessage("sua user thanh cong");
        }
        else{
            responseData.setSuccess(false);
            responseData.setMessage("sua user that bai ");
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UsersRequest usersRequest){
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        if(userService.addUsers(usersRequest)) {
            responseData.setSuccess(true);
            responseData.setData(usersRequest);
            status = HttpStatus.CREATED;
            responseData.setMessage("Thêm user thành công  ");

        }
        else{
            responseData.setSuccess(false);
            responseData.setMessage("Thêm User thất bại");
            status = HttpStatus.NOT_FOUND;

        }
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        if(userService.deleteUsers(id)){
            responseData.setSuccess(true);
            responseData.setMessage("xoa user thanh cong");
            status = HttpStatus.OK;
        }
        else{
            responseData.setSuccess(false);
            responseData.setMessage("xoa user that bai ");
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
//    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
//    @GetMapping("/myInfo")
//    public ResponseEntity<?> getMyInfo(){
//        ResponseData responseData = new ResponseData();
//        HttpStatus status ;
//        Users_response usersResponse=userService.myInfo();
//        if(usersResponse!=null){
//            responseData.setData(usersResponse);
//            responseData.setSuccess(true);
//            status = HttpStatus.OK;
//        }
//        else{
//            responseData.setSuccess(false);
//            responseData.setSuccess(false);
//            status = HttpStatus.NOT_FOUND;
//        }
//        return ResponseEntity.status(status).body(responseData);
//    }

}
