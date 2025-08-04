package com.example.PPQ.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolesRequest {
    private int id;
    @NotBlank(message = "Tên Role không được để trống")
    private String roleName;
    private String description;


}
