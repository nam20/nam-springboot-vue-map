package com.example.demo.DB;

import com.example.demo.DB.DTO.BoardDTO;
import com.example.demo.DB.DTO.CommentDTO;
import com.example.demo.DB.DTO.UserDTO;
import com.example.demo.DB.Entity.Board;
import com.example.demo.DB.Entity.Comment;
import com.example.demo.DB.Entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityConvertDTO {

    public UserDTO userDTOBulider(User user){

        return  UserDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .token(user.getToken())
                .userProfile(user.getUserProfile())

                .build();
    }

    public CommentDTO commentDTOBuilder(Comment comment, UserDTO userDTO){
        return CommentDTO.builder()
                .id(comment.getId())
                .commentContent(comment.getCommentContent())
                .createdDate(comment.getCreatedDate())
                .user(userDTO)
                .build();
    }

    public BoardDTO boardDTOBuilder(Board board, UserDTO userDTO, List<CommentDTO> commentDTOS){

        return  BoardDTO.builder()
                .id(board.getId())
                .boardAvailable(board.getBoardAvailable())
                .boardContent(board.getBoardContent())
                .createdTime(board.getCreatedTime())
                .lastModifiedTime(board.getLastModifiedTime())
                .placeId(board.getPlaceId())
                .placeName((board.getPlaceName()))
                .files(board.getFiles())
                .grade(board.getGrade())
                .user(userDTO)
                .comments(commentDTOS)
                .build();
    }

    public BoardDTO boardDTOBuilder(Board board, UserDTO userDTO){

        return  BoardDTO.builder()
                .id(board.getId())
                .boardAvailable(board.getBoardAvailable())
                .boardContent(board.getBoardContent())
                .createdTime(board.getCreatedTime())
                .lastModifiedTime(board.getLastModifiedTime())
                .files(board.getFiles())
                .placeId(board.getPlaceId())
                .placeName((board.getPlaceName()))
                .grade(board.getGrade())
                .user(userDTO)
                .build();
    }





}
