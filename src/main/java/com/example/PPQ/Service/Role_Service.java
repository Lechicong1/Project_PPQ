package com.example.PPQ.Service;

import com.example.PPQ.Entity.Roles_Entity;
import com.example.PPQ.Entity.User_Entity;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.RolesRequest;
import com.example.PPQ.Payload.Response.Roles_response;
import com.example.PPQ.Service_Imp.RoleService;
import com.example.PPQ.respository.Roles_respository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class Role_Service implements RoleService {
    @Autowired
    Roles_respository respository;
    @Autowired
    UsersRepository usersRepository;
    @Override
    public boolean addRoles(RolesRequest RolesRequest) {
        Roles_Entity roles = new Roles_Entity();
        roles.setRoleName(RolesRequest.getRoleName());
        roles.setDescription(RolesRequest.getDescription());
        try{
            respository.save(roles);
            return true;
        }
        catch(Exception e) {
            System.out.println("co loi khi them role " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRoles(int id) {
        if (!respository.existsById(id)) {
              throw new ResourceNotFoundException("Role không tồn tại ");
        }
        // vi roles_id la khoa ngoai trong bang user nen can xoa trong bang user truoc moi xoa duoc trong bang role
        List<User_Entity> users=usersRepository.findByIdRoles(id);
        if(!users.isEmpty()) {
           usersRepository.deleteAll(users);
        }
        try{
            respository.deleteById(id);
            return true;
        }
        catch(Exception e) {
            System.out.println("co loi khi xoa " + e.getMessage());
            return false;
        }

    }

    @Override
    public boolean updateRoles(int id,RolesRequest RolesRequest) {
        Roles_Entity roles = respository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        try {
            if(RolesRequest.getRoleName()!=null){
                roles.setRoleName(RolesRequest.getRoleName());
            }
            if(RolesRequest.getDescription()!=null){
                roles.setDescription(RolesRequest.getDescription());
            }
            respository.save(roles);
            return true;
        }
        catch(Exception e) {
            System.out.println("co loi khi sua role  " + e.getMessage());
            return false;

        }
    }

    @Override
    public List<Roles_response> getAllRoles() {
        List<Roles_Entity> roles = respository.findAll();
        if(roles.isEmpty())
            throw new ResourceNotFoundException("Role không tồn tại");
        List<Roles_response> roles_response = new ArrayList<Roles_response>();
        for(Roles_Entity role : roles) {
            Roles_response role_dto = new Roles_response();
            role_dto.setRoleName(role.getRoleName());
            role_dto.setDescription(role.getDescription());
            role_dto.setId(role.getId());
            roles_response.add(role_dto);
        }

        return roles_response;
        }

    }

