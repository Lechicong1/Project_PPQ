package com.example.PPQ.Security;

import com.example.PPQ.Entity.Roles_Entity;
import com.example.PPQ.Entity.User_Entity;
import com.example.PPQ.Exception.ResourceNotFoundException;
import com.example.PPQ.respository.Roles_respository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UsersRepository usersRepo;
    @Autowired
    Roles_respository roles_respository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User_Entity user = usersRepo.findByUsername(username);
        Roles_Entity rolesEntity=roles_respository.findById(user.getIdRoles()).orElseThrow(()->new UsernameNotFoundException("Không tồn tại roles"));
        if(user == null) {
            throw new UsernameNotFoundException("Không tìm thấy username");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(rolesEntity.getRoleName())));
    }  // tao ra doi tuong User cua spring de xac thuc va phan quyen nguoi dung
}
