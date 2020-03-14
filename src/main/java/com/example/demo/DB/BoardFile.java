package com.example.demo.DB;

import com.example.demo.DB.Entity.Board;
import lombok.Data;

import javax.persistence.*;


@Data
public class BoardFile {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;
}
