package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.RolesRequest;
import com.example.PPQ.Payload.Response.Roles_response;

import java.util.List;

public interface RoleService {
    public boolean addRoles(RolesRequest roles_request);
    public boolean deleteRoles(int id);
    public boolean updateRoles(int id,RolesRequest RolesRequest);
    public List<Roles_response> getAllRoles();
}
