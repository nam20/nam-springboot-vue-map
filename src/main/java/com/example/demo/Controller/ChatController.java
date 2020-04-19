package com.example.demo.Controller;

import com.example.demo.DB.DTO.CommentDTO;
import com.example.demo.DB.Entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(CommentDTO commentDTO){
        messagingTemplate.convertAndSend("","");
    }

}
