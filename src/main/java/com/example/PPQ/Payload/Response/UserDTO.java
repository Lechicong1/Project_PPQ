package com.example.PPQ.Payload.Response;

public class UserDTO {
    private String userName;
    private String roleName;
    private Integer userId;
    public String getRoleName() {
        return roleName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
