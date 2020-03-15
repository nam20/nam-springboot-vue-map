package com.example.demo.Repository;

import com.example.demo.DB.Entity.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BoardRepository  extends JpaRepository<Board,Long> {

    Optional<Board> findById(Long s);

    List<Board> findByPlaceId(String place);

    Page<Board> findByPlaceId(String place, Pageable pageable);

    @Query(value = "select avg(b.grade) avg,count(*) count from board b where b.place_id = ?1 and b.board_available = true",nativeQuery = true)
    Optional<Map<String,Double>> boardAvgGrade(String placeId);

    @Query(value = "select count(*) from board b where b.board_available = true", nativeQuery = true)
    Optional<Integer> boardCount();

    Page<Board> findByPlaceIdAndBoardAvailable(String placeId, Boolean boardAvailable , Pageable pageable);

    Page<Board> findByBoardAvailable(Boolean boardAvailable, Pageable pageable);




}
