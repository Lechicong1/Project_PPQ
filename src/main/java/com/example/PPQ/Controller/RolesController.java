package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.RolesRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Payload.Response.RolesDTO;
import com.example.PPQ.Service.RolesService;
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
        RolesService role_service;
        @PostMapping
        public ResponseEntity<?> addRoles(@Valid @RequestBody RolesRequest role) {
            ResponseData responseData = new ResponseData();
            HttpStatus status ;
            System.out.println("rolename: "+role.getRoleName());
            System.out.println("description: "+role.getDescription());
            role_service.addRoles(role);
            responseData.setMessage("Thêm Roles thành công");
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.CREATED;   //201
            return ResponseEntity.status(status).body(responseData);
        }
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateRoles(@PathVariable int id, @Valid @RequestBody RolesRequest role) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        role_service.updateRoles(id,role);
        responseData.setMessage("Sửa Roles thành công");
        responseData.setSuccess(Boolean.TRUE);
        status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @DeleteMapping(value = "/{id}",produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> deleteRoles(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        role_service.deleteRoles(id);
            responseData.setMessage("Xóa Roles thành công");
            responseData.setSuccess(Boolean.TRUE);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).body(responseData);
    }
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        ResponseData responseData = new ResponseData();
        HttpStatus status ;
        List<RolesDTO> ListRoles=role_service.getAllRoles();
            responseData.setData(ListRoles);
            responseData.setSuccess(true);
            status = HttpStatus.OK;
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(responseData);
    }

}
