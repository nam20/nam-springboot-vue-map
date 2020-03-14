package com.example.demo.DB;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Grade {

    GOOD("5"),SOSO("3"),BAD("1");

    private String legacyCode;

    Grade(String legacyCode){
        this.legacyCode = legacyCode;
    }

    public static Grade ofLegacyCode(String legacyCode){
        return Arrays.stream(Grade.values())
                .filter(v -> v.getLegacyCode().equals(legacyCode))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException("legacyCode 없음"));
    }
}
