package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.UsersRequest;
import com.example.PPQ.Payload.Request.changePasswordRequest;
import com.example.PPQ.Payload.Response.PageResponse;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Payload.Response.UserDTO;
import com.example.PPQ.Service.UserService;
import com.example.PPQ.ServiceImp.UserServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    UserService userService;

//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getAllUsers(@RequestParam Integer page, @RequestParam Integer size) {
//        ResponseData responseData = new ResponseData();
//        HttpStatus status;
//        PageResponse<UserDTO> ListUsersPage = userService.getAllUsers(page - 1, size);
//        responseData.setData(ListUsersPage);
//        responseData.setSuccess(true);
//        status = HttpStatus.OK;
//        return ResponseEntity.status(status).body(responseData);
//    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER','ADMIN','USER')")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(Map.of(
                "username", jwt.getSubject(),
                "authorities", jwt.getClaimAsStringList("authorities")
        ));
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody changePasswordRequest changePasswordRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status;
        userService.changePassword(changePasswordRequest);
        responseData.setSuccess(true);
        status = HttpStatus.OK;
        responseData.setMessage("Đổi mật khẩu thành công");

        return ResponseEntity.status(status).body(responseData);
    }

    @GetMapping("/roleTeacher")
    public ResponseEntity<?> getAllUsersRoleTeacher() {
        ResponseData responseData = new ResponseData();
        HttpStatus status;
        List<UserDTO> ListUsers = userService.getAllUserByRoleTeacher();
        responseData.setData(ListUsers);
        responseData.setSuccess(true);
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @Valid @RequestBody UsersRequest usersRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status;
        userService.updateUsers(id, usersRequest);
        responseData.setSuccess(true);
        responseData.setData(usersRequest);
        status = HttpStatus.OK;
        responseData.setMessage("Sửa user thành công");
        return ResponseEntity.status(status).body(responseData);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UsersRequest usersRequest) {
        ResponseData responseData = new ResponseData();
        HttpStatus status;
        userService.addUsers(usersRequest);
        responseData.setSuccess(true);
        responseData.setData(usersRequest);
        status = HttpStatus.CREATED;
        responseData.setMessage("Thêm user thành công ");
        return ResponseEntity.status(status).body(responseData);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status;
        userService.deleteUsers(id);
        responseData.setSuccess(true);
        responseData.setMessage("Xóa user thành công");
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByUsernameAndRole(@RequestParam(required = false) String username,
                                                         @RequestParam(required = false) Integer role,
                                                         @RequestParam Integer page
                                                            , @RequestParam Integer size
    ) {
        ResponseData responseData = new ResponseData();
        HttpStatus status;
        PageResponse<UserDTO> listUserDTO = userService.findUserByUsernameAndRole(username, role, page-1, size);
        responseData.setData(listUserDTO);
        responseData.setSuccess(true);
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }


}
