package com.example.demo.DB.Entity;

import com.example.demo.DB.Grade;
import com.example.demo.DB.GradeAttributeConverter;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;



    private String placeId;

    private String placeName;

    @Column(length = 2048)
    private String boardContent;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime lastModifiedTime;

    //@Enumerated(EnumType.STRING)
    @Convert(converter = GradeAttributeConverter.class)
    private Grade grade;

    @Column(length = 1)
    private Boolean boardAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user){
        this.user = user;
        user.getBoards().add(this);
    }

    public void update(String boardContent, Grade grade, List<String> files){
        this.boardContent = boardContent;
        this.grade = grade;
        this.files = files;
    }



    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "BOARD_FILES",joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "files")
    private List<String> files = new ArrayList<>();

//
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "board")
//    private List<BoardFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

     @Builder
     public Board( String boardContent, Grade grade, String placeId, String placeName,
                  Boolean boardAvailable, User user, List<String> files, List<Comment> comments){

         Assert.notNull(user,"user must not null");


         this.boardContent = boardContent;
         this.grade = grade;
         this.placeId = placeId;
         this.placeName = placeName;
         this.boardAvailable = boardAvailable;
         this.user = user;
//         user.getBoards().add(this);
         this.files = files;
         this.comments = comments;


     }

}
