package com.example.PPQ.ServiceImp;

import com.example.PPQ.Entity.RolesEntity;
import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.Exception.DuplicateResourceException;
import com.example.PPQ.Exception.InvalidInputException;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.Payload.Request.registerRequest;
import com.example.PPQ.respository.RoleRepository;
import com.example.PPQ.respository.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class RegisterServiceImp implements com.example.PPQ.Service.RegisterService {
    @Autowired
    UsersRepository users_repository;
    @Autowired
    RoleRepository roles_repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void register( @Valid registerRequest request) {
        UserEntity user = new UserEntity();
        RolesEntity roleDefault = roles_repository.findByRoleName("USER");
        if(roleDefault==null)
            throw new ResourceNotFoundException("Roles USER không tồn tại");
        if(users_repository.existsByUsername(request.getUsername())){
            throw new DuplicateResourceException("Người dùng đã tồn tại");
        }
        if (request.getPassword().equals(request.getConfirmPassword())) {
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setIdRoles(roleDefault.getId());
            try{
                users_repository.save(user);
            }
            catch (DataIntegrityViolationException e){
                throw new DuplicateResourceException("Người dùng đã tồn tại");
            }

        }
        else{
            throw new InvalidInputException("Mật khẩu xác nhận không trùng với mật khẩu trước đó ");
        }


}
}


