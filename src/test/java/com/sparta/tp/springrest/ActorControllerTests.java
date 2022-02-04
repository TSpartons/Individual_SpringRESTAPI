package com.sparta.tp.springrest;

import static org.hamcrest.Matchers.containsString;


import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ActorControllerTests {

    @Autowired
    private MockMvc mocker;

    @Test
    public void mockerIsPresent(){
        Assertions.assertNotNull(mocker);
    }

    @Test
    @DisplayName("Return a firstname")
    public void actorsShouldReturnAFirstname() throws Exception{
            this.mocker.perform(get("/actors")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Quaffle")));}

    @Test
    @DisplayName("Discard those without given firstname")
    public void actorsWithoutFirstNameShouldBeDiscarded() throws Exception{
        this.mocker.perform(get("/actors?firstname=NICK")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.not(containsString("Quaffle"))))
                .andExpect(content().string(containsString("NICK")));
    }

    @Test
    @DisplayName("Discard actors without given lastname")
    public void actorsWithoutLastNameShouldBeDiscarded() throws  Exception{
        this.mocker.perform(get("/actors?lastname=DEGENERES")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.not(containsString("STALLONE"))))
                .andExpect(content().string(containsString("DEGENERES")));
    }

    @Test
    @DisplayName("Discard actors without given first and last name")
    public void actorsWithoutBothParamsShouldBeDiscarded() throws Exception {
        this.mocker.perform(get("/actors?lastname=DEGENERES&firstname=NICK")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("NICK")))
                .andExpect(content().string(containsString("DEGENERES")))
                .andExpect(content().string(CoreMatchers.not(containsString("STALLONE"))));
    }

    @Test
    @DisplayName("/actors/{id} return actor with id")
    public void onlyActorWithIdShouldBeReturned() throws Exception {
        this.mocker.perform(get("/actors/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Quaffle")))
                .andExpect(content().string(CoreMatchers.not(containsString("NICK"))));
    }

    @Test
    @DisplayName("Add a new actor")
    public void addAnActor() throws Exception {
        this.mocker.perform(MockMvcRequestBuilders.post("/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\": \"Tifa\", \"lastName\": \"Lockhart\" , \"lastUpdate\": \"2006-02-15T04:34:33.000+00:00\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Update an actor")
    public void updateAnActor() throws Exception {
        this.mocker.perform(put("/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"actorId\":\"6\", \"firstName\": \"Tifa\", \"lastName\": \"Lockhart\" , \"lastUpdate\": \"2006-02-15T04:34:33.000+00:00\"}"));

        this.mocker.perform(get("/actors/3")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Tifa")));
    }

    @Test
    @DisplayName("Delete updated actor")
    public void deleteActor() throws Exception {
        this.mocker.perform(MockMvcRequestBuilders.delete("/actors/204"));
    }
}
