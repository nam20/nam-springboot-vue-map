package com.example.demo.Service;

import oracle.net.ns.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256 {
    public static String encryptSHA256(String str){
        String sha = "";
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(byte b : byteData){
                sb.append(Integer.toString((b&0xff) + 0x100,16).substring(1));
            }
            sha = sb.toString();
            System.out.println("기존 패스워드: " + str);
            System.out.println("암호화 패스워드: "+sha);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return sha;
    }
}
