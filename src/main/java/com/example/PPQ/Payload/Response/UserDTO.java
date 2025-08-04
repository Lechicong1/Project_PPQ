package com.example.PPQ.Payload.Response;

import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.Payload.Projection_Interface.UserView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String userName;
    private String roleName;
    private Integer userId;

    public UserDTO() {
    }
    public UserDTO(UserView userEntity) {
        this.userId = userEntity.getId();
        this.userName = userEntity.getUserName();
        this.roleName=userEntity.getRoleName();
    }
}
