package com.example.demo.DB.DTO;

import com.example.demo.DB.Grade;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class BoardDTO extends RepresentationModel<BoardDTO> {



    private Long id;


    private String placeId;

    private String placeName;


    private String boardContent;


    private Grade grade;


    private Boolean boardAvailable;


    private UserDTO user;


    private List<String> files = new ArrayList<>();


    private List<CommentDTO> comments = new ArrayList<>();

    private LocalDateTime createdTime;

    private LocalDateTime lastModifiedTime;

    @Builder @JsonCreator
    public BoardDTO(Long id, String boardContent, Grade grade, String placeId, String placeName,
                    Boolean boardAvailable, UserDTO user, List<String> files, List<CommentDTO> comments, LocalDateTime createdTime, LocalDateTime lastModifiedTime){

        Assert.notNull(user,"user must not null");



        this.id = id;

        this.boardContent = boardContent;
        this.grade = grade;
        this.placeId = placeId;
        this.placeName = placeName;
        this.boardAvailable = boardAvailable;
        this.user = user;
        this.files = files;
        this.comments = comments;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;

    }



}
