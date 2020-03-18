package com.example.demo.DB;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class Greeting extends RepresentationModel<Greeting> {

    private final String content;

    private final int greetingId;

    @JsonCreator
    public Greeting(@JsonProperty("content") String content, @JsonProperty("greetingId") int greetingId){
        this.content = content;
        this.greetingId = greetingId;
    }



}



