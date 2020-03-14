package com.example.demo.Service;

import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.DB.*;
import com.example.demo.DB.DTO.BoardDTO;
import com.example.demo.DB.DTO.CommentDTO;
import com.example.demo.DB.Entity.Board;
import com.example.demo.DB.Entity.Comment;
import com.example.demo.DB.Entity.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class BoardService {


    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityConvertDTO entityConvertDTO;

    @Autowired
    private CommentRepository commentRepository;


    public void boardWrtie(String token, Grade grade, String content, String placeId, String placeName, MultipartFile[] multipartFile){


        List<String> files = new ArrayList<>();

        if(multipartFile.length !=  0) {
            System.out.println(multipartFile.length);

            for (MultipartFile file : multipartFile) {
                System.out.println(file.getName());
                System.out.println(file.getOriginalFilename());
                String savePath = "C:/projects/demo-nam22/src/main/webapp/upload/";//request.getRealPath("/resources/upload").replaceAll("\\\\","/");
                String random = UUID.randomUUID().toString().replaceAll("-", "");
                File nfile = new File(savePath + random.substring(20) + file.getOriginalFilename());

                try {
                    if (!file.getOriginalFilename().equals("")) file.transferTo(nfile);
                } catch (Exception e) {
                    throw new IllegalArgumentException("100mb 이상 불가");
                }
                files.add(nfile.getName());
            }
        }

        Optional<User> user = userRepository.findByToken(token);
        if(user.isPresent()){
            Board board = Board.builder()
                    .boardContent(content)
                    .boardAvailable(true)
                    .grade(grade)
                    .placeName(placeName)
                    .placeId(placeId)
                    .user(user.get())
                    .files(files)
                    .build();

            boardRepository.save(board);
        }

    }


    public List<BoardDTO> allBoard(int page, int size){

        List<Board> boardList = boardRepository.findByBoardAvailable(true, PageRequest.of(page,size)).getContent();

        List<BoardDTO> boardDTOList = new ArrayList<>();
        boardList.forEach(board -> boardDTOList.add(entityConvertDTO.boardDTOBuilder(board,entityConvertDTO.userDTOBulider(board.getUser()))));

        return boardDTOList;
    }


    public List<BoardDTO> boardReviewByPlaceId(String placeId, int page, int size){
        List<BoardDTO> boardDTOList = new ArrayList<>();

        boardRepository.findByPlaceIdAndBoardAvailable(placeId,true, PageRequest.of(page,size)).getContent().forEach(board -> {
            boardDTOList.add(entityConvertDTO.boardDTOBuilder(board,entityConvertDTO.userDTOBulider(board.getUser())));
        });

//            //지역id로 찾은 boardList를 순회하며 DTOBuilder를 사용해 엔티티를 dto로 변환한 뒤 dtoList에 추가

        return boardDTOList;
    }

    public Map<String,Double> avgBoardGrade(String placeId){
        return boardRepository.boardAvgGrade(placeId).orElse(null);
    }

    public List<CommentDTO> getCommentsByBoardId(Optional<Board> boardOpt){
        List<CommentDTO> commentDTOList = new ArrayList<>();
        boardOpt.ifPresent(board -> {
            board.getComments().forEach(comment -> commentDTOList.add(entityConvertDTO.commentDTOBuilder(comment,entityConvertDTO.userDTOBulider(comment.getUser()))));
        });

        return commentDTOList;
    }

    public BoardDTO boardFindById(Long boardId){
        Optional<Board> boardOp = boardRepository.findById(boardId);

        return boardOp.map(value -> entityConvertDTO.boardDTOBuilder(value, entityConvertDTO.userDTOBulider(value.getUser()),getCommentsByBoardId(boardOp))).orElse(null);
        //id값으로 board 찾은 뒤 DTOBuilder 메소드로 엔티티를 dto로 변환하여 boardDTO를 구한다.
    }

    public List<CommentDTO> getComments(Long boardId){
        return getCommentsByBoardId(boardRepository.findById(boardId));
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



    public void boardUpdate(Grade grade, String content, Long boardId,MultipartFile[] multipartFile , String[] fileNames){

        String savePath = "C:/projects/demo-nam22/src/main/webapp/upload/";

        boardRepository.findById(boardId).ifPresent(board -> {

            List<String> files = board.getFiles();

            List<String> newFiles = new ArrayList<>(Arrays.asList(fileNames));

            System.out.println(newFiles.size());


            files.forEach(file->{
                if (!newFiles.contains(file)) {
                    File deleteFile = new File(savePath + file);
                    if (deleteFile.exists())  deleteFile.delete();
                }
            });

            files.removeIf(file->!newFiles.contains(file));

            if(multipartFile!=null) {

                for (MultipartFile file : multipartFile) {

                    //request.getRealPath("/resources/upload").replaceAll("\\\\","/");
                    String random = UUID.randomUUID().toString().replaceAll("-", "");
                    File nfile = new File(savePath + random.substring(20) + file.getOriginalFilename());

                    try {
                        if (!file.getOriginalFilename().equals("")) file.transferTo(nfile);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("500mb 이상 불가");
                    }
                    files.add(nfile.getName());
                }
            }


            board.update(content,grade,files);
            boardRepository.save(board);
        });
    }


    public void boardDelete(Long boardId){
        boardRepository.findById(boardId).ifPresent(board -> {
            board.setBoardAvailable(false);
            boardRepository.save(board);
        });
    }

    public Integer boardCount(){
        return boardRepository.boardCount().orElse(0);
    }


}
