package com.sparta.tp.springrest;

import com.sparta.tp.springrest.controller.ActorController;
import com.sparta.tp.springrest.entities.ActorEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootTest
class SpringRestApplicationTests {

    @Autowired
    private ActorController controller;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(controller);
    }

    @Test
    void doTest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ActorEntity> response = restTemplate.getForEntity("http://localhhost:8080/actors/1", ActorEntity.class);
        System.out.println(response.getBody().getFirstName());
        System.out.println(response.getStatusCode());
        ActorEntity[] actors = restTemplate.getForObject("http://localhost:8080/actors", ActorEntity[].class);
        long count = Arrays.stream(actors).filter(actor -> actor.getFirstName().contains("a")). count();
    }

}
