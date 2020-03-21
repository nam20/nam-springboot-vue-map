package com.example.demo.Service;

import com.example.demo.Repository.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Component
public class JwtUtilImpl implements JwtUtil {

    @Autowired
    UserRepository userRepository;

    private String Key = "skagmlwns123";

    private Logger logger = LoggerFactory.getLogger(JwtUtilImpl.class);

    @Override
    public String createToken(String userName) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Key);
        java.security.Key signingKey = new SecretKeySpec(apiKeySecretBytes,signatureAlgorithm.getJcaName());


        Map<String,Object> headers = new HashMap<>();
        headers.put("typ","JWT");
        headers.put("alg","HS256");

        Map<String,Object> payloads = new HashMap<>();

        Date now = new Date();
        now.setTime(now.getTime() + 1000 * 60 * 60 * 3);
        payloads.put("userName",userName);

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setExpiration(now)
                .signWith(SignatureAlgorithm.HS256,signingKey)
                .compact();
    }

    @Override
    public boolean verifyToken(String token) {
        System.out.println("jwt");
        if(token.equals("Bearer null") || token.equals("Bearer") || token.equals("")){
                logger.info("토큰 없음");
                return false;
        }
        try {

            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(Key)).parseClaimsJws(token.substring(7)).getBody();

                return true;
        }catch (ExpiredJwtException e){
                logger.info("토큰 만료");
                return false;//throw new UnauthorizedException();
        }catch (JwtException e){
                logger.info("토큰 변조");
                return false;
        }

    }
}
