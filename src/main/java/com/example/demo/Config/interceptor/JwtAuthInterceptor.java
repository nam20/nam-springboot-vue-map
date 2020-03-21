package com.example.demo.Config.interceptor;

import com.example.demo.Service.JwtUtil;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        System.out.println("인터셉터");
        if(request.getMethod().equals("GET")) return true;

        if(jwtUtil.verifyToken(token)){

            return true;

        }else{
            response.setContentType("application/json");

            response.setCharacterEncoding("UTF-8");

            response.getWriter().write("{\"Auth\":\"FAIL\"}");

            return false;
        }


    }


//    private void verifyToken(String givenToken, String membersToken){
//        if(!givenToken.equals(membersToken)) throw new IllegalArgumentException("토큰 불일치");
//        jwtUtil.verifyToken(givenToken);
//    }
}


