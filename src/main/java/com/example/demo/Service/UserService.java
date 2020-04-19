package com.example.demo.Service;

import com.example.demo.DB.DTO.UserDTO;
import com.example.demo.DB.Entity.User;
import com.example.demo.DB.EntityConvertDTO;
import com.example.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Optional;
import java.util.UUID;


@Transactional
@Service
public class UserService {




    private final EntityConvertDTO entityConvertDTO;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;


    public UserService(EntityConvertDTO entityConvertDTO,UserRepository userRepository, JwtUtil jwtUtil){
        this.entityConvertDTO = entityConvertDTO;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String register(String userId,String userName, String userPassword){
        if(userRepository.findByUserId(userId).isPresent()) return "중복된 아이디입니다."; //throw new IllegalArgumentException("중복된 유저");
        String salt = Sha256.generateSalt();
        User user = User.builder()
                .userId(userId)
                .userName(userName)
                .salt(salt)
                .userPassword(Sha256.encryptSHA256(userPassword,salt))
                .token(jwtUtil.createToken(userName))
                .build();

//        user.setUserName(userName);
//        user.setUserPassword(userPassword);
//        user.setToken(jwtUtil.createToken(userName));

        userRepository.save(user);
        return user.getToken();

    }

    public String login(String userId, String userPassword){
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if(!optionalUser.isPresent()) return "없는 아이디입니다.";
        User user = optionalUser.get();

        if(! user.getUserPassword().equals(Sha256.encryptSHA256(userPassword,user.getSalt()))) return "비밀번호가 틀렸습니다.";


            user.setToken(jwtUtil.createToken(userId));
            userRepository.save(user);

        return user.getToken();
    }



    public void userProfileUpdate(MultipartFile multipartFile, String token){

        String savePath = "/var/lib/tomcat9/webapps/ROOT/upload/";

        userRepository.findByToken(token).ifPresent(user -> {



            if(multipartFile != null){

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

            }else{

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
        return userRepository.findByToken(token).map(entityConvertDTO::userDTOBulider).orElse(null);
    }


    public String getUserProfile(String token){

        Optional<User> userOptional = userRepository.findByToken(token);

        if(userOptional.isPresent()){

            return userOptional.get().getUserProfile();
        }

        return "";
    }

}
