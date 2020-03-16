package com.example.demo.DB.Entity;

import com.example.demo.DB.Entity.Board;
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

    @Column(length = 20/*, unique = true*/)
    private String userName;


    private String userPassword;

    private String userProfile;

    private String token;

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<Board>();

    @Builder
    public User(String userName,String userPassword, String userProfile, String token, List<Board> boards){



        this.userName = userName;
        this.userPassword = userPassword;
        this.userProfile = userProfile;
        this.token = token;
        this.boards = boards;
    }

}
