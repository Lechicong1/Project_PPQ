package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Request.LoginRequest;
import com.example.PPQ.Payload.Response.ResponseData;
import com.example.PPQ.Config.JwtCustom;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
    public class AuthController {
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

            @PostMapping("/login")
            public ResponseEntity<?> login(@Valid  @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
                HttpStatus status = HttpStatus.OK;

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
                        // Tạo cookie chứa JWT
                        Cookie jwtCookie = new Cookie("access_token", token);
                        jwtCookie.setHttpOnly(true); // Bảo vệ khỏi JavaScript (XSS)
                        jwtCookie.setSecure(false); // Bật nếu dùng HTTPS
                        jwtCookie.setPath("/"); // Toàn hệ thống
                        jwtCookie.setMaxAge(30 * 60); // 1 giờ (tuỳ thời gian sống của token)

                        // Gửi cookie về client
                        response.addCookie(jwtCookie);
                        responseData.setSuccess(true);
                        responseData.setMessage("Login successful");
                        responseData.setData(jwtCookie);
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
        @PostMapping("/logout")
        public ResponseEntity<?> logout(HttpServletResponse response) {
            ResponseData responseData = new ResponseData();
            HttpStatus status = HttpStatus.OK;
            try{
                Cookie cookie = new Cookie("access_token", "");
                cookie.setMaxAge(0); // Xoá ngay
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setPath("/");
                response.addCookie(cookie);
                responseData.setSuccess(true);
                responseData.setMessage("Logout successful");
                System.out.println("dang xuat thanh cong ");
            }
            catch(Exception e){
                responseData.setSuccess(false);
                responseData.setMessage("Logout fail");
            }

            return ResponseEntity.status(status).body(responseData);
        }
}


