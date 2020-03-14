package com.example.demo.Controller;

import com.example.demo.DB.DTO.UserDTO;
import com.example.demo.DB.Entity.User;
import com.example.demo.DB.EntityConvertDTO;
import com.example.demo.Service.UserService;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityConvertDTO entityConvertDTO;

    @PostMapping("/auth")
    public void auth(){}


    @PostMapping(value="/register")
    public String join(@RequestParam("userName") String userName, @RequestParam("userPassword") String userPassword){

        return userService.register(userName,userPassword);

    }



    @PostMapping(value="/login")
    public String login(@RequestParam("userName") String userName, @RequestParam("userPassword") String userPassword){

        return userService.login(userName,userPassword);
    }



    @GetMapping("/user/{token}")
    public UserDTO findUserByToken(@PathVariable("token") String token){

        return userService.findUserByToken(token);
    }

    @PatchMapping("/user/profile")
    public void userProfileUpdate(@RequestParam(value = "profile",required = false) MultipartFile multipartFile,@RequestParam("token") String token, @RequestParam(value = "profileName",defaultValue = "") String profileName){

       userService.userProfileUpdate(multipartFile, token, profileName);

    }


    @PostMapping("/user/profileName")
    public String getUserProfile(@RequestParam("token") String token){

       return userService.getUserProfile(token);
    }


}
