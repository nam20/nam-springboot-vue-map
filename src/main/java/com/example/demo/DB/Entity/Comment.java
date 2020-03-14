package com.example.demo.DB.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void setBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }
    @Column(length = 500)
    private String commentContent;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public Comment(User user, Board board, String commentContent, LocalDateTime createdDate){

        //Assert.notNull(board,"board must not null");

        this.user = user;
        this.board = board;
        this.createdDate = createdDate;

        this.commentContent = commentContent;


    }


}
