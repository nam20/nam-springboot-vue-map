package com.example.demo.DB.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {

    private Long id;

    private UserDTO user;

    private String commentContent;

    private LocalDateTime createdDate;

    @Builder
    public CommentDTO(Long id, UserDTO user, String commentContent,LocalDateTime createdDate){

        this.id = id;
        this.user = user;
        this.createdDate = createdDate;
        this.commentContent = commentContent;


    }
}
