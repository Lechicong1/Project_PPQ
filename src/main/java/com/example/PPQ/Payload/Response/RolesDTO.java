package com.example.PPQ.Payload.Response;

import com.example.PPQ.Entity.RolesEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolesDTO {
    private int id;
    private String roleName;
    private String description;

    public RolesDTO(){}
    public RolesDTO(RolesEntity e){
        this.id = e.getId();
        this.roleName = e.getRoleName();
        this.description = e.getDescription();

    }

}
