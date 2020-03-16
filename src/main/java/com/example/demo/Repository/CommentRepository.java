package com.example.demo.Repository;

import com.example.demo.DB.Entity.Board;
import com.example.demo.DB.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {


}
