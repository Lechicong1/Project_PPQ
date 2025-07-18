package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.RolesRequest;
import com.example.PPQ.Payload.Response.RolesDTO;

import java.util.List;

public interface RoleService {
    public void addRoles(RolesRequest roles_request);
    public void deleteRoles(int id);
    public void updateRoles(int id,RolesRequest RolesRequest);
    public List<RolesDTO> getAllRoles();
}
