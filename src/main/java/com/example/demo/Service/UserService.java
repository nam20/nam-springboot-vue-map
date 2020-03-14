package com.example.demo.Service;

import com.example.demo.DB.DTO.UserDTO;
import com.example.demo.DB.Entity.User;
import com.example.demo.DB.EntityConvertDTO;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {




    @Autowired
    private  EntityConvertDTO entityConvertDTO;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(String userName, String userPassword){
        if(userRepository.findByUserName(userName).isPresent()) return "중복된 아이디입니다."; //throw new IllegalArgumentException("중복된 유저");

        User user = User.builder()
                .userName(userName)
                .userPassword(Sha256.encryptSHA256(userPassword))
                .token(jwtUtil.createToken(userName))
                .build();

//        user.setUserName(userName);
//        user.setUserPassword(userPassword);
//        user.setToken(jwtUtil.createToken(userName));

        userRepository.save(user);
        return user.getToken();

    }

    public String login(String userName, String userPassword){
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        if(!optionalUser.isPresent()) return "없는 아이디입니다.";
        User user = optionalUser.get();

        if(! user.getUserPassword().equals(Sha256.encryptSHA256(userPassword))) return "비밀번호가 틀렸습니다.";


            user.setToken(jwtUtil.createToken(userName));
            userRepository.save(user);

        return user.getToken();
    }



    public void userProfileUpdate(MultipartFile multipartFile, String token, String profileName){

        String savePath = "C:/projects/demo-nam22/src/main/webapp/upload/";

        userRepository.findByToken(token).ifPresent(user -> {



            if(multipartFile != null){
                System.out.println(multipartFile);
                String random = UUID.randomUUID().toString().replaceAll("-", "");
                File nfile = new File(savePath + random.substring(20) + multipartFile.getOriginalFilename());

                try {
                    if (!multipartFile.getOriginalFilename().equals("")) multipartFile.transferTo(nfile);
                } catch (Exception e) {
                    throw new IllegalArgumentException("100mb 이상 불가");
                }

                if(user.getUserProfile()!=null){
                    File deleteFile = new File(savePath + user.getUserProfile());
                    if(deleteFile.exists()) deleteFile.delete();
                }

                user.setUserProfile(nfile.getName());
                userRepository.save(user);

            }else if(profileName.equals("")){

                if(user.getUserProfile()!=null){
                    File deleteFile = new File(savePath + user.getUserProfile());
                    if(deleteFile.exists()) deleteFile.delete();
                }

                user.setUserProfile(null);
                userRepository.save(user);
            }
        });
    }

    public UserDTO findUserByToken(String token){
        return userRepository.findByToken(token).map(user -> entityConvertDTO.userDTOBulider(user)).orElse(null);
    }


    public String getUserProfile(@RequestParam("token") String token){

        String userProfile = "";

        Optional<User> userOptional = userRepository.findByToken(token);

        if(userOptional.isPresent()){
            userProfile = entityConvertDTO.userDTOBulider(userOptional.get()).getUserProfile();
        }

//        userRepository.findByToken(token).ifPresent(user -> {
//             entityConvertDTO.userDTOBulider(user).getUserProfile();
//         });

        return userProfile;
    }

}
