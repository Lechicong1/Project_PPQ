package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.NotBlank;

public class RolesRequest {
    private int id;
    @NotBlank(message = "Tên Role không được để trống")
    private String roleName;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
