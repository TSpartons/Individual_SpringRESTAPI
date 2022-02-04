package com.sparta.tp.springrest.assemblers;

import com.sparta.tp.springrest.controller.ActorController;
import com.sparta.tp.springrest.entities.ActorEntity;
import com.sparta.tp.springrest.enums.Entities;
import com.sparta.tp.springrest.exceptions.EntityNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import javax.xml.bind.ValidationException;

import java.sql.Connection;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ActorModelAssembler implements RepresentationModelAssembler<ActorEntity,
        EntityModel<ActorEntity>> {

    public final Link link = Link.of("http://localhost:8080/actors");
    private Connection connection;

    @Override
    public EntityModel<ActorEntity> toModel(ActorEntity actor) {

        try {
            return EntityModel.of(
                    actor, //
                    linkTo(methodOn(ActorController.class).findActor(actor.getActorId())).withSelfRel(),
                    link.withRel("allActors")
            );
        } catch (ValidationException e) {
            throw new EntityNotFoundException(Entities.ACTOR);
        }
    }

    @Override
    public CollectionModel<EntityModel<ActorEntity>> toCollectionModel(Iterable<? extends ActorEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
