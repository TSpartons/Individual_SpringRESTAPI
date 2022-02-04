package com.sparta.tp.springrest.controller;

import com.sparta.tp.springrest.entities.ActorEntity;
import com.sparta.tp.springrest.enums.Entities;
import com.sparta.tp.springrest.exceptions.EntityNotFoundException;
import com.sparta.tp.springrest.assemblers.ActorModelAssembler;
import com.sparta.tp.springrest.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class ActorController {

    private ActorRepository repository;
    private final ActorModelAssembler assembler;

    @Autowired
    public ActorController(ActorRepository repository, ActorModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

//    @GetMapping("/actors")
//    public List<ActorEntity> getAllActors () {
//        return repository.findAll();
//    }

    @GetMapping("/actors")
    public CollectionModel<EntityModel<ActorEntity>> getAllActors(
            @RequestParam(required = false, value = "firstname") String firstname,
            @RequestParam(required = false, value = "lastname") String lastname) {

        List<EntityModel<ActorEntity>> actors;

        if (firstname == null && lastname == null) {
            actors = repository.findAll().stream() //
                    .map(assembler::toModel) //
                    .collect(Collectors.toList());
            return CollectionModel.of(actors, assembler.link);
        }

        List<ActorEntity> foundActors = new ArrayList<>();

        if (firstname != null) {
            for (ActorEntity actor : repository.findAll()) {
                if (actor.getFirstName().contains(firstname))
                    foundActors.add(actor);
            }

        }

        if(lastname != null) {

            if(foundActors.isEmpty()) {
                for (ActorEntity actor : repository.findAll()) {
                    if (actor.getLastName().contains(lastname))
                        foundActors.add(actor);
                }
            }
            else {
                foundActors.removeIf(actor -> !actor.getLastName().contains(lastname));
            }

        }

        actors = foundActors.stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(actors, assembler.link);
    }

//        @GetMapping("/actors")
//    @ResponseBody
//    public List<ActorEntity> findActorsByName(@RequestParam(required = false, value = "name") String name) {
//        if(name == null)
//            return repository.findAll();
//        else {
//            List<ActorEntity> foundActors = new ArrayList<>();
//            for (ActorEntity actor: repository.findAll()
//            ) { if(actor.getFirstName().contains(name))
//                foundActors.add(actor);
//
//            }
//            return foundActors;
//        }
//    }

    @GetMapping("/actors/{id}")
    public EntityModel<ActorEntity> findActor(@PathVariable Integer id)  throws ValidationException {

        ActorEntity actorEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Entities.ACTOR));

        return assembler.toModel(actorEntity);
    }

    @PostMapping("/actors")
    ResponseEntity<?> addActor(@RequestBody ActorEntity actor) throws ValidationException {
        EntityModel<ActorEntity> entityModel = assembler.toModel(repository.save(actor));
            return ResponseEntity //
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                    .body(entityModel);
    }

    @PutMapping("/actors")
    public ResponseEntity<ActorEntity> updateActor(@RequestBody ActorEntity actor) {
        if(repository.findById(actor.getActorId()).isPresent())
            return new ResponseEntity<>(repository.save(actor), HttpStatus.OK);
        else
            return new ResponseEntity<>(actor, HttpStatus.BAD_REQUEST);
    }

    //---------------------------HANDLING OLDER CLIENTS-------------------------------------------//

//    @PutMapping("/employees/{id}")
//    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//
//        Employee updatedEmployee = repository.findById(id) //
//                .map(employee -> {
//                    employee.setName(newEmployee.getName());
//                    employee.setRole(newEmployee.getRole());
//                    return repository.save(employee);
//                }) //
//                .orElseGet(() -> {
//                    newEmployee.setId(id);
//                    return repository.save(newEmployee);
//                });
//
//        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);
//
//        return ResponseEntity //
//                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
//                .body(entityModel);
//    }


    @PutMapping("/actors/{id}")
    public ActorEntity replaceActor(@RequestBody ActorEntity newActor, @PathVariable Integer id) {

        return repository.findById(id)
                .map(actor -> {
                    actor.setFirstName(newActor.getFirstName());
                    actor.setLastName(newActor.getLastName());
                    return repository.save(actor);
                })
                .orElseGet(() -> {
                    newActor.setActorId(id);
                    return repository.save(newActor);
                });
    }

    //Will not delete from original database
    @DeleteMapping("/actors/{id}")
    public void deleteActor(@PathVariable("id") Integer id) throws ValidationException {
        if(id < 200)
           throw new EntityNotFoundException(id);

        repository.deleteById(id);
    }
}
