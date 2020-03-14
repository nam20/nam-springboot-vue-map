package com.example.demo.Service;

public interface JwtUtil {
    String createToken(String userName);
    boolean verifyToken(String givenToken);
}
