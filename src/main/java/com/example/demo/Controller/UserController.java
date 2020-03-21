package com.example.demo.Controller;

import com.example.demo.DB.DTO.UserDTO;
import com.example.demo.DB.EntityConvertDTO;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.image.ImageWatched;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;


    private final UserRepository userRepository;


    private final EntityConvertDTO entityConvertDTO;

    @Autowired
    public UserController(UserService userService,UserRepository userRepository,EntityConvertDTO entityConvertDTO){
        this.userService = userService;
        this.userRepository = userRepository;
        this.entityConvertDTO = entityConvertDTO;
    }




    @PostMapping("/auth")
    public void auth(){}


    @PostMapping(value="/register")
    public String join(@RequestBody Map<String, String> payload){

        return userService.register(payload.get("userId"),payload.get("userName"),payload.get("userPassword"));

    }



    @PostMapping(value="/login")
    public String login(@RequestBody Map<String, String> payload){

        System.out.println(payload);

        //return "";
        return userService.login(payload.get("userId"),payload.get("userPassword"));
    }



    @PostMapping("")
    public UserDTO findUserByToken(@RequestHeader("Authorization") String token){
        System.out.println(token);
        UserDTO userDTO =  userService.findUserByToken(token.substring(7));
//        if(userDTO != null){
//            userDTO.add(linkTo(BoardController.class).withRel("create"));
//        }else{
//            userDTO = new UserDTO();
////            userDTO.add(linkTo(UserController.class).slash("join").withRel("join"));
////            userDTO.add(linkTo(UserController.class).slash("login").withRel("login"));
//        }
//        new userDTO

        return userDTO;
    }

    @PatchMapping("/profile")
    public void userProfileUpdate(@RequestParam(value = "profile",required = false) MultipartFile multipartFile,@RequestParam("token") String token /*@RequestParam(value = "profileName",defaultValue = "") String profileName*/){

       userService.userProfileUpdate(multipartFile, token);

    }


    @PostMapping("/profile")
    public String getUserProfile(/*@RequestParam("token") String token,*/ @RequestBody Map<String,String> payload){
        System.out.println(payload);
       return userService.getUserProfile(payload.get("token"));
    }


}
