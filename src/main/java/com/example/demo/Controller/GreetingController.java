package com.example.demo.Controller;

import com.example.demo.DB.Greeting;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/test")
public class GreetingController {

    private static final String TEMPLATE = "Hello, %s!";

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name",defaultValue = "world") String name){
        Greeting greeting = new Greeting(String.format(TEMPLATE,name),1);
        greeting.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());

        Link job = linkTo(GreetingController.class).slash(greeting.getGreetingId()).withRel("job");
        Link home = linkTo(GreetingController.class).slash(greeting.getGreetingId()).withRel("home");
        Link heejun = linkTo(methodOn(GreetingController.class).heejun()).withRel("heejun");
        Link nam = linkTo(GreetingController.class).slash("nam").withRel("nam");

        greeting.add(job);
        greeting.add(heejun);
        greeting.add(nam);
        greeting.add(home);


        System.out.println(greeting);

        return greeting;
    }

    @RequestMapping("/heejun")
    public HttpEntity<String> heejun(){
         return new ResponseEntity<>("heejun3", HttpStatus.OK);
    }


}
