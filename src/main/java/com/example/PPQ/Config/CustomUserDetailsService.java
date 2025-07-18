package com.example.PPQ.Config;

import com.example.PPQ.Entity.RolesEntity;
import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.respository.Roles_respository;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = usersRepo.findByUsername(username);
        RolesEntity rolesEntity=roles_respository.findById(user.getIdRoles()).orElseThrow(()->new UsernameNotFoundException("Không tồn tại roles"));
        if(user == null) {
            throw new UsernameNotFoundException("Không tìm thấy username");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(rolesEntity.getRoleName())));
    } // tao ra doi tuong User cua spring de xac thuc va phan quyen nguoi dung
}
