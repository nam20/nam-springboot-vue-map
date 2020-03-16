package com.example.demo.Controller;

import com.example.demo.DB.DTO.CommentDTO;
import com.example.demo.DB.Entity.Board;
import com.example.demo.DB.Entity.Comment;
import com.example.demo.DB.EntityConvertDTO;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CommentController {


    private final CommentService commentService;

    private final CommentRepository commentRepository;

    private final EntityConvertDTO entityConvertDTO;

    private  final BoardRepository boardRepository;

    @Autowired
    public CommentController(CommentService commentService, CommentRepository commentRepository, EntityConvertDTO entityConvertDTO, BoardRepository boardRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.entityConvertDTO = entityConvertDTO;
        this.boardRepository = boardRepository;
    }


    @GetMapping("/comments/{boardId}")
    public List<CommentDTO> getComments(@PathVariable("boardId") Long boardId){

        return commentService.getComments(boardId);
    }

    @PostMapping("/comment")
    public void commentWrite(/*@RequestParam("comment") String commentContent, @RequestParam("token") String token, @RequestParam("boardId") String boardId,*/@RequestBody Map<String,String> payload){

        commentService.commentWrite(payload.get("comment"), payload.get("token"), payload.get("boardId"));

    }






}
