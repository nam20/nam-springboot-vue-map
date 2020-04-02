package com.example.demo.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Sha256 {
    public static String generateSalt(){
        Random random = new Random();

        byte[] salt = new byte[8];
        random.nextBytes(salt);
        StringBuffer sb = new StringBuffer();

        for(byte b : salt){
            sb.append(String.format("%02x",b));
        }
        return sb.toString();
    }


    public static String encryptSHA256(String str, String salt){
        String sha = "";
        try{
            byte[] passwordArr = str.getBytes();
            byte[] saltArr = salt.getBytes();
            byte[] bytes = new byte[passwordArr.length + saltArr.length];

            System.arraycopy(passwordArr,0,bytes,0, passwordArr.length);
            System.arraycopy(saltArr, 0, bytes, passwordArr.length, saltArr.length);

            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(bytes);
            byte[] byteData = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(byte b : byteData){
                sb.append(Integer.toString((b&0xff) + 0x100,16).substring(1));
            }
            sha = sb.toString();

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return sha;
    }
}
