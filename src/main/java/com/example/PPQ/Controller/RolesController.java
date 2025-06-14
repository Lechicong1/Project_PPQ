package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.RolesRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Payload.Response.Roles_response;
import com.example.PPQ.Service.Role_Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('ADMIN')")
public class RolesController {
        @Autowired
        Role_Service role_service;
        @PostMapping
        public ResponseEntity<?> addRoles(@Valid @RequestBody RolesRequest role) {
            ResponseData responseData = new ResponseData();
            HttpStatus status ;
            System.out.println("rolename: "+role.getRoleName());
            System.out.println("description: "+role.getDescription());
            if(role_service.addRoles(role)) {
                responseData.setData("true");
                responseData.setMessage("Them role thanh cong");
                responseData.setSuccess(Boolean.TRUE);
                status = HttpStatus.CREATED;   //201
            }
            else{
                responseData.setData("false");
                responseData.setMessage("Them role that bai");
                responseData.setSuccess(Boolean.FALSE);
                status = HttpStatus.BAD_REQUEST;
            }
            return ResponseEntity.status(status).body(responseData);
        }
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateRoles(@PathVariable int id, @Valid @RequestBody RolesRequest role) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        if(role_service.updateRoles(id,role)) {
            responseData.setData("true");
            responseData.setMessage("sua role thanh cong");
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        }
        else{
            responseData.setData("false");
            responseData.setMessage("sua role that bai");
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}",produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> deleteRoles(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        if(role_service.deleteRoles(id)) {
            responseData.setData("true");
            responseData.setMessage("xoa role thanh cong");
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        }
        else{
            responseData.setData("false");
            responseData.setMessage("xoa role that bai");
            responseData.setSuccess(Boolean.FALSE);
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(responseData);
    }
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        List<Roles_response> ListRoles=role_service.getAllRoles();
        if(!ListRoles.isEmpty()) {
            responseData.setData(ListRoles);
            responseData.setSuccess(true);
            status = HttpStatus.OK;

        }
        else{

            responseData.setSuccess(false);
            status = HttpStatus.NOT_FOUND;

        }

        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(responseData);
    }

}
