package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.RolesEntity;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.RolesRequest;
import com.example.PPQ.Payload.Response.RolesDTO;
import com.example.PPQ.Service.RoleService;
import com.example.PPQ.respository.RoleRepository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolesServiceImp implements RoleService {
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    UsersRepository usersRepository;
    @Override
    public void addRoles(RolesRequest RolesRequest) {
        RolesEntity roles = new RolesEntity(RolesRequest);
        roleRepo.save(roles);
    }

    @Override
    public void deleteRoles(int id) {
        if (!roleRepo.existsById(id)) {
              throw new ResourceNotFoundException("Role không tồn tại ");
        }
        // vi roles_id la khoa ngoai trong bang user nen can xoa trong bang user truoc moi xoa duoc trong bang role
        usersRepository.removeUserRole(id);
        roleRepo.deleteById(id);
    }

    @Override
    public void updateRoles(int id,RolesRequest RolesRequest) {
        RolesEntity roles = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
            if(RolesRequest.getRoleName()!=null) roles.setRoleName(RolesRequest.getRoleName());
            if(RolesRequest.getDescription()!=null) roles.setDescription(RolesRequest.getDescription());

        roleRepo.save(roles);
    }

    @Override
    public List<RolesDTO> getAllRoles() {
        List<RolesEntity> roles = roleRepo.findAll();
        if(roles.isEmpty())
            throw new ResourceNotFoundException("Role không tồn tại");

        return roles.stream()
                .map(roleEntity -> new RolesDTO(roleEntity))
                .collect(Collectors.toList());
        }

    }

