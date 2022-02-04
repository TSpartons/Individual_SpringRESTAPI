package com.sparta.tp.springrest;

import com.sparta.tp.springrest.entities.ActorEntity;
import com.sparta.tp.springrest.repositories.ActorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;

//Just test the database
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Override Controller
public class RESTIntTests {

    @Autowired
    private ActorRepository repository;

    @Test
    void doTest() {
        List<ActorEntity> all = repository.findAll();
        Assertions.assertEquals(201, all.size());
    }
}
