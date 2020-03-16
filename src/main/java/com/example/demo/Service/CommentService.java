package com.example.demo.Service;

import com.example.demo.DB.DTO.CommentDTO;
import com.example.demo.DB.Entity.Board;
import com.example.demo.DB.Entity.Comment;
import com.example.demo.DB.Entity.User;
import com.example.demo.DB.EntityConvertDTO;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {


    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final EntityConvertDTO entityConvertDTO;

    @Autowired
    public CommentService(BoardRepository boardRepository, UserRepository userRepository, CommentRepository commentRepository, EntityConvertDTO entityConvertDTO) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.entityConvertDTO = entityConvertDTO;
    }


//    public List<CommentDTO> getCommentsByBoardId(Optional<Board> boardOpt){
//        List<CommentDTO> commentDTOList = new ArrayList<>();
//        boardOpt.ifPresent(board -> {
//            board.getComments().forEach(comment -> commentDTOList.add(entityConvertDTO.commentDTOBuilder(comment,entityConvertDTO.userDTOBulider(comment.getUser()))));
//        });
//
//        return commentDTOList;
//    }


    public List<CommentDTO> getComments(Long boardId){


        Optional<Board> boardOptional = boardRepository.findById(boardId);
        List<CommentDTO> commentDTOList = new ArrayList<>();

        boardOptional.ifPresent(board -> {
            board.getComments().forEach(comment -> commentDTOList.add(entityConvertDTO.commentDTOBuilder(comment,entityConvertDTO.userDTOBulider(comment.getUser()))));
        });

        commentDTOList.sort(new Comparator<CommentDTO>() {
            @Override
            public int compare(CommentDTO o1, CommentDTO o2) {

                return o2.getCreatedDate().compareTo(o1.getCreatedDate());
            }
        });

        return commentDTOList;
    }

    public void commentWrite(String commentContent,  String token, String boardId){

        Optional<User> userOptional = userRepository.findByToken(token);
        Optional<Board> boardOptional = boardRepository.findById(Long.parseLong(boardId));


        if(userOptional.isPresent() && boardOptional.isPresent()){
            Comment comment = Comment.builder()
                    .user(userOptional.get())
                    .board(boardOptional.get())
                    .commentContent(commentContent)
                    .build();

            commentRepository.save(comment);

        }
        else throw  new IllegalArgumentException("토큰 혹은 리뷰가 없습니다.");
    }


}
