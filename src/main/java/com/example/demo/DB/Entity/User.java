package com.example.demo.DB.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 20, unique = true)
    private String userId;

    @Column(length = 20/*, unique = true*/)
    private String userName;

    @Column(length = 100)
    private String userPassword;

    private String userProfile;

    private String token;

    private String salt;

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<Board>();

    @Builder
    public User(String userId, String userName,String userPassword, String salt, String userProfile, String token, List<Board> boards){


        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.salt = salt;
        this.userProfile = userProfile;
        this.token = token;
        this.boards = boards;
    }

}
