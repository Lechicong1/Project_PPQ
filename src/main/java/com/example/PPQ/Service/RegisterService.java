package com.example.PPQ.Service;

import com.example.PPQ.Entity.Roles_Entity;
import com.example.PPQ.Entity.User_Entity;
import com.example.PPQ.Exception.BusinessLogicException;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.InvalidInputException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.registerRequest;
import com.example.PPQ.Service_Imp.RegisterImp;
import com.example.PPQ.respository.Roles_respository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class RegisterService implements RegisterImp {
    @Autowired
    UsersRepository users_repository;
    @Autowired
    Roles_respository roles_repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean register(registerRequest request) {
        System.out.println(request.getUsername());
        System.out.println(request.getPassword());
        System.out.println(request.getConfirmPassword());
        User_Entity user = new User_Entity();
        Roles_Entity role = roles_repository.findByRoleName("USER");
        if(role==null)
            throw new ResourceNotFoundException("Roles USER không tồn tại");
        if(users_repository.existsByUsername(request.getUsername())){
            throw new DuplicateResourceException("Người dùng đã tồn tại");
        }
        if (request.getPassword().equals(request.getConfirmPassword())) {
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setIdRoles(role.getId());
            try {
                users_repository.save(user);
                return true;
            } catch (Exception e) {
                System.out.println("co loi khi dang ki " + e.getMessage());
                return false;
            }
        }
        else{
            throw new InvalidInputException("Mật khẩu xác nhận không trùng với mật khẩu trước đó ");
        }


}
}


