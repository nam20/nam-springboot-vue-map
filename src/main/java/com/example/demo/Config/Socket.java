package com.example.demo.Config;
import com.example.demo.DB.DTO.CommentDTO;
import com.example.demo.DB.Entity.Comment;
import com.example.demo.DB.Entity.User;
import com.example.demo.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


@Component
@ServerEndpoint("/websocket/{id}")
public class Socket {

    @Autowired
    private UserService userService;



    private static Logger log = LoggerFactory.getLogger(Socket.class);
    public static Set<Socket> listeners = new CopyOnWriteArraySet<>();
    private Session session;  //ddd
    private static HashMap<String, List<Session>> messageUserlist = new HashMap<>();

    private static int onlineCount = 0;

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id ){
        System.out.println(session.getId());
        System.out.println(id);
        log.info("onOpen called()...");
        addOnlineCount();
        //this.session = session;

        List<Session> sessionList;


        if(messageUserlist.containsKey(id)){
            sessionList = messageUserlist.get(id);
            sessionList.add(session);
        }else{
            sessionList = new ArrayList<>();
            sessionList.add(session);
            messageUserlist.put(id,sessionList);
            //sessionList.remove(session);
        }

        System.out.println(messageUserlist);


        //listeners.add(this);
    }

    @OnClose
    public void onClose(Session session,@PathParam("id") String id) {

        log.info("onClose called()...");
        subOnlineCount();

        if(messageUserlist.containsKey(id)) {
            messageUserlist.get(id).remove(session);

            if(messageUserlist.get(id).isEmpty()) messageUserlist.remove(id);

        }
        System.out.println(messageUserlist);
        //listeners.remove(this);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("id") String id) {
        log.info("onMessage called()...");


        //broadcast(message);
//        CommentDTO commentDTO = CommentDTO.builder()
//                .user(userService.findUserByToken(payload[0]))
//                .commentContent(message)
//                .createdDate(LocalDateTime.now())
//                .build();
//        CommentDTO commentDTO = new CommentDTO();


        for(Session session : messageUserlist.get(id)){
            try{
               //session.getBasicRemote().sendObject(commentDTO);
                session.getBasicRemote().sendText(message + "," + LocalDateTime.now().toString());
            }catch(IOException e){
                log.error("Caught exception while sending message to Session " + session.getId());
            }
        }


    }

    public static void broadcast(String message) {


        for (Socket listener : listeners) {
            listener.sendMessage(message);
        }
    }

    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("Caught exception while sending message to Session " + this.session.getId(), e.getMessage(), e);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("onError called()...");
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        Socket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        Socket.onlineCount--;
    }
}
