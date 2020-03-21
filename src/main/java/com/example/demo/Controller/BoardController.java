package com.example.demo.Controller;

import com.example.demo.DB.*;
import com.example.demo.DB.DTO.BoardDTO;
import com.example.demo.DB.Entity.Board;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Service.BoardService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/board")
public class BoardController {







    private final BoardService boardService;

    private final BoardRepository boardRepository;

    @Autowired
    public BoardController(BoardService boardService, BoardRepository boardRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }


    @PostMapping(value = "", consumes = "multipart/form-data")
    public void boardWrtie(@RequestParam("token") String token, @RequestParam("grade") Grade grade, @RequestParam("content")
            String content, @RequestParam("placeId") String placeId, @RequestParam("placeName") String placeName, @RequestParam(value = "files",required = false,defaultValue = "[]") MultipartFile[] multipartFile){

        boardService.boardWrtie(token, grade, content, placeId, placeName, multipartFile);

    }

    @GetMapping("")
    public Map<String,Object> allBoard(@RequestParam("page") int page,@RequestParam("limit") int limit){

       return boardService.allBoard(page,limit);

    }




    @GetMapping(value="/place/{placeId}")
    public Map<String,Object> placeBoardView(@PathVariable("placeId") String placeId,@RequestParam("page") int page,@RequestParam("limit") int limit){

        return boardService.boardByPlaceId(placeId, page, limit);

    }

    @GetMapping("/user/{userName}")
    public Map<String,Object> userIdBoard(@PathVariable("userName") String userName,@RequestParam("page") int page,@RequestParam("limit") int limit){
        return boardService.boardByUserId(userName,page,limit);
    }

    @GetMapping("/gradeAvg/{placeId}")
    public Map<String,Double> avgBoardGrade(@PathVariable("placeId") String placeId){

        // 리뷰 평점의 평균 구하기
        return boardService.avgBoardGrade(placeId);
    }





    @GetMapping("/{boardId}")   // 보드 id값으로 단 한개 가져오기
    public BoardDTO boardFindById(@PathVariable("boardId") Long boardId){

        return  boardService.boardFindById(boardId);

    }

    @GetMapping("/{boardId}/{userId}")   // 보드 id값으로 단 한개 가져오기
    public BoardDTO boardFind(@PathVariable("boardId") Long boardId,@PathVariable("userId") Long userId){
        BoardDTO boardDTO = boardService.boardFindById(boardId);
        boardDTO.add(linkTo(methodOn(BoardController.class).boardFind(boardId,userId)).withSelfRel());




        if(userId.equals(boardDTO.getUser().getId())){
            boardDTO.add(linkTo(BoardController.class).slash(boardId).withRel("delete"));
            boardDTO.add(linkTo(BoardController.class).slash(boardId).withRel("update"));
        }


        return boardDTO;

    }



    @PatchMapping("/{boardId}")
    public void boardUpdate(@RequestParam("grade") Grade grade,@RequestParam("content") String content, @PathVariable("boardId") Long boardId
        ,@RequestParam(value = "files",required = false) MultipartFile[] multipartFile , @RequestParam(value = "fileNames",required = false,defaultValue = "[]") String[] fileNames){

        boardService.boardUpdate(grade, content, boardId, multipartFile, fileNames);

    }

    @DeleteMapping("/{boardId}")
    public void boardDelete(@PathVariable("boardId") Long boardId){
        boardService.boardDelete(boardId);
    }

//    @GetMapping("/boardCount")
//    public Integer boardCount(){
//        return boardService.boardCount();
//    }








}
