package com.hms1.Controller;

import com.hms1.Service.UserService;
import com.hms1.payload.LoginDto;
import com.hms1.payload.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?>CreateUser(
            @RequestBody UserDto dto
            )
    {
        ResponseEntity<?> userDto = userService.addUser(dto);
        return new ResponseEntity<>( userDto, HttpStatus.CREATED);
    }

//    @GetMapping("/message")
//    public String getMessage(){
//        return "Hello";
//    }
    @PostMapping("/login")
    public String Verfiylogin(
            @RequestBody LoginDto loginDto
    ){
        boolean verifyLogin = userService.VerifyLogin(loginDto);
        if (verifyLogin){
            return "Login Success";
        }else {
            return "Invalid Credentials";
        }
    }

}
