package com.example.PPQ.Entity;

import com.example.PPQ.Payload.Request.RolesRequest;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Roles")
public class RolesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String roleName;
    private String Description;
    public RolesEntity() {
    }
    public RolesEntity(RolesRequest req){
        this.roleName = req.getRoleName();
        this.Description = req.getDescription();
    }
}
