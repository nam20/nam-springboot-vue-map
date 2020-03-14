package com.example.demo.DB.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String userName;



    private String userProfile;

    private String token;


    @Builder
    public UserDTO( Long id, String userName, String userProfile,String token){

        this.id = id;
        this.userName = userName;
        this.token = token;
        this.userProfile = userProfile;


    }


}
