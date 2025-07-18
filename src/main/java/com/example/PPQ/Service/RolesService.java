package com.example.PPQ.Service;

import com.example.PPQ.Entity.RolesEntity;
import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.RolesRequest;
import com.example.PPQ.Payload.Response.RolesDTO;
import com.example.PPQ.Service_Imp.RoleService;
import com.example.PPQ.respository.Roles_respository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class RolesService implements RoleService {
    @Autowired
    Roles_respository respository;
    @Autowired
    UsersRepository usersRepository;
    @Override
    public void addRoles(RolesRequest RolesRequest) {
        RolesEntity roles = new RolesEntity();
        roles.setRoleName(RolesRequest.getRoleName());
        roles.setDescription(RolesRequest.getDescription());
        respository.save(roles);
    }

    @Override
    public void deleteRoles(int id) {
        if (!respository.existsById(id)) {
              throw new ResourceNotFoundException("Role không tồn tại ");
        }
        // vi roles_id la khoa ngoai trong bang user nen can xoa trong bang user truoc moi xoa duoc trong bang role
        List<UserEntity> users=usersRepository.findByIdRoles(id);
        if(!users.isEmpty()) {
          for(UserEntity user:users) {
              user.setIdRoles(null);
          }
          usersRepository.saveAll(users);
        }
        respository.deleteById(id);

    }

    @Override
    public void updateRoles(int id,RolesRequest RolesRequest) {
        RolesEntity roles = respository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

            if(RolesRequest.getRoleName()!=null){
                roles.setRoleName(RolesRequest.getRoleName());
            }
            if(RolesRequest.getDescription()!=null){
                roles.setDescription(RolesRequest.getDescription());
            }
            respository.save(roles);
    }

    @Override
    public List<RolesDTO> getAllRoles() {
        List<RolesEntity> roles = respository.findAll();
        if(roles.isEmpty())
            throw new ResourceNotFoundException("Role không tồn tại");
        List<RolesDTO> roles_response = new ArrayList<RolesDTO>();
        for(RolesEntity role : roles) {
            RolesDTO role_dto = new RolesDTO();
            role_dto.setRoleName(role.getRoleName());
            role_dto.setDescription(role.getDescription());
            role_dto.setId(role.getId());
            roles_response.add(role_dto);
        }
        return roles_response;
        }

    }

