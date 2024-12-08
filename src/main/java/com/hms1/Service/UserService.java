package com.hms1.Service;

import com.hms1.Entity.User;

import com.hms1.Repositry.UserRepository;
import com.hms1.payload.LoginDto;
import com.hms1.payload.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.web.servlet.UndertowServletWebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    User mapToEntity(UserDto dto){
        User user = modelMapper.map(dto, User.class);
        return user;
    }
    UserDto mapToDto(User user){
        UserDto dto = modelMapper.map(user, UserDto.class);
        return dto;
    }

    public ResponseEntity<?> addUser(UserDto dto) {
        User user = mapToEntity(dto);
        Optional<User> opUsername = userRepository.findByUsername(user.getUsername());
       if (opUsername.isPresent()){
           return new ResponseEntity<>("Username already exists",HttpStatus.CONFLICT);


       }
        Optional<User> opEmail = userRepository.findByEmail(user.getEmail());
        if (opEmail.isPresent()){

            return new ResponseEntity<>("Email Already Exists",HttpStatus.CONFLICT);

        }
        Optional<User> opPassword = userRepository.findByPassword(user.getPassword());
        if (opPassword.isPresent()){
            return new ResponseEntity<>("Password Already Exists",HttpStatus.CONFLICT);
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        User save = userRepository.save(user);
        UserDto userDto = mapToDto(save);
        return new ResponseEntity<>(userDto,HttpStatus.OK);

    }

    public boolean VerifyLogin(LoginDto loginDto){
        Optional<User> opUsername = userRepository.findByUsername(loginDto.getUsername());
   if (opUsername.isPresent()){
       User user = opUsername.get();
       if(BCrypt.checkpw(loginDto.getPassword(),user.getPassword())){
           return true;
       }
   }else {
       return false;
   }
        return false;
    }

}

