package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.LoginRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Security.JwtCustom;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/login")

    public class LoginController {
    //    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    //
    //    public LoginController(AuthenticationManagerBuilder authenticationManagerBuilder) {
    //        this.authenticationManagerBuilder = authenticationManagerBuilder;
    //    }
    //    luong hoat dong :
    //    nguoi dung gui tk mk -> he thong se tu tk mk request tao ra UsernamePasswordAuthenticationToken
    //                    ->di vao trong ham authenticate cua authenticationManager de xa thuc nguoi dung
    //                    ->goi den ham loadUserByUsername ->tra ve doi tuong UserDetail
        //                ->so sanh userdetails voi UsernamePasswordAuthenticationToken
        @Autowired
         AuthenticationManager authenticationManager;
        @Autowired
        JwtCustom jwtCustom;
        @PostMapping
        public ResponseEntity<?> login( @Valid  @RequestBody LoginRequest loginRequest) {
            HttpStatus status = HttpStatus.OK;
            System.out.println(loginRequest.getUserName());
            System.out.println(loginRequest.getPassword());
            // Tạo đối tượng phản hồi
            ResponseData responseData = new ResponseData();
            try {
                // Tạo đối tượng xác thực từ username và password
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());

                // Xác thực người dùng
                Authentication authentication = authenticationManager.authenticate(authenticationToken);

                // Lưu thông tin xác thực vào SecurityContext
                if (authentication.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String token = jwtCustom.createToken(authentication);
                    responseData.setSuccess(true);
                    responseData.setMessage("Login successful");
                    responseData.setData(token);
                }

            } catch (AuthenticationException ex) {
                // Trường hợp xác thực thất bại
                responseData.setSuccess(false);
                responseData.setMessage("Invalid username or password");
                responseData.setData(null);
                status=HttpStatus.UNAUTHORIZED;
            }
            return  ResponseEntity.status(status).body(responseData);
        }
}


