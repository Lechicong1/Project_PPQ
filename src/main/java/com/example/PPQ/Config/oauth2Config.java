package com.example.PPQ.Config;

import com.example.PPQ.Entity.RolesEntity;
import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.Enums.Role;
import com.example.PPQ.respository.RoleRepository;
import com.example.PPQ.respository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class oauth2Config implements AuthenticationSuccessHandler {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    JwtCustom jwtCustom;
    @Value("${app.cors.allowed-origin}")
    String frontendUrl;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // Lấy thông tin người dùng từ Google
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauthToken.getPrincipal();

        String email = oauth2User.getAttribute("email");


        // Kiểm tra xem user đã tồn tại trong DB chưa
        UserEntity user = usersRepository.findByUsername(email);

        if (user == null) {
            // Nếu chưa có thì tạo mới
            user = new UserEntity();
            user.setUsername(email);
            // Gán quyền mặc định
            RolesEntity defaultRole = roleRepo.findByRoleName(Role.USER);
            user.setIdRoles(defaultRole.getId());
            usersRepository.save(user);
        }

        RolesEntity rolesEntity=roleRepo.findById(user.getIdRoles()).orElseThrow(()->new UsernameNotFoundException("Không tồn tại roles"));
        // Tạo danh sách quyền
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(rolesEntity.getRoleName().name())
        );

        // Tạo userDetail mới
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(), "", authorities);
        // Tạo authenticaton từ userdetail
        Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        // Tạo JWT từ authentication
        String jwt = jwtCustom.createToken(newAuth);
        System.out.println(jwt);
        Cookie cookie = new Cookie("oauth2_token", jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(false); //  nếu muốn FE đọc được thì phải để false
        response.addCookie(cookie);
            // Chuyển hướng đến FE biết để gọi API lấy JWT
        System.out.println(frontendUrl+"/oauth2_redirect.html");
        response.sendRedirect( frontendUrl+"/oauth2_redirect.html");

    }
}
