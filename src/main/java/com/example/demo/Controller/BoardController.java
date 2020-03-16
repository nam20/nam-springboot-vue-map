package com.example.demo.Controller;

import com.example.demo.DB.*;
import com.example.demo.DB.DTO.BoardDTO;
import com.example.demo.DB.Entity.Board;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@RestController
public class BoardController {







    private final BoardService boardService;

    private final BoardRepository boardRepository;

    @Autowired
    public BoardController(BoardService boardService, BoardRepository boardRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }


    @PostMapping(value = "/board", consumes = "multipart/form-data")
    public void boardWrtie(@RequestParam("token") String token, @RequestParam("grade") Grade grade, @RequestParam("content")
            String content, @RequestParam("placeId") String placeId, @RequestParam("placeName") String placeName, @RequestParam(value = "files",required = false,defaultValue = "[]") MultipartFile[] multipartFile){

        boardService.boardWrtie(token, grade, content, placeId, placeName, multipartFile);

    }

    @GetMapping("/allBoard/{page}/{size}")
    public Map<String,Object> allBoard(@PathVariable("page") int page,@PathVariable("size") int size){

       return boardService.allBoard(page,size);

    }





    @RequestMapping(value = "/naverSearch/{search}",method = RequestMethod.GET, produces = "application/text;charset=utf-8")
    public String naverSearch(@PathVariable String search){

        String clientId = "dJeVRfUwEf1HVzLEDzow";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "G4CkxupJ7k";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(search, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/local.json?query="+ text; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

                      System.out.println(response.toString());


            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";


    }





    @GetMapping(value="/boards/place/{placeId}/{page}/{size}")
    public Map<String,Object> placeBoardView(@PathVariable("placeId") String placeId,@PathVariable("page") int page,@PathVariable("size") int size){

        return boardService.boardByPlaceId(placeId, page, size);

    }

    @GetMapping("/boards/user/{userName}/{page}/{size}")
    public Map<String,Object> userIdBoard(@PathVariable("userName") String userName,@PathVariable("page") int page,@PathVariable("size") int size){
        return boardService.boardByUserId(userName,page,size);
    }

    @GetMapping("/boardCount/gradeAvg/{placeId}")
    public Map<String,Double> avgBoardGrade(@PathVariable("placeId") String placeId){

        // 리뷰 평점의 평균, count 구하기
        return boardService.avgBoardGrade(placeId);
    }





    @GetMapping("/board/{boardId}")   // 보드 id값으로 단 한개 가져오기
    public BoardDTO boardFindById(@PathVariable("boardId") Long boardId){

        return boardService.boardFindById(boardId);

    }



    @PatchMapping("/board")
    public void boardUpdate(@RequestParam("grade") Grade grade,@RequestParam("content") String content, @RequestParam("boardId") Long boardId
        ,@RequestParam(value = "files",required = false) MultipartFile[] multipartFile , @RequestParam(value = "fileNames",required = false,defaultValue = "[]") String[] fileNames){

        boardService.boardUpdate(grade, content, boardId, multipartFile, fileNames);

    }

    @PatchMapping("/board/{boardId}")
    public void booardDelete(@PathVariable("boardId") Long boardId){
        boardService.boardDelete(boardId);
    }

    @GetMapping("/boardCount")
    public Integer boardCount(){
        return boardService.boardCount();
    }








}
