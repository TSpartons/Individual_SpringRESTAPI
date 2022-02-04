package com.sparta.tp.springrest.assemblers;

import com.sparta.tp.springrest.controller.FilmController;
import com.sparta.tp.springrest.entities.FilmEntity;
import com.sparta.tp.springrest.enums.Entities;
import com.sparta.tp.springrest.exceptions.EntityNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FilmModelAssembler implements RepresentationModelAssembler<FilmEntity,
        EntityModel<FilmEntity>> {

    public final Link link = Link.of("http://localhost:8080/films");

    @Override
    public EntityModel<FilmEntity> toModel(FilmEntity film) {

        try {
            return EntityModel.of(
                    film, //
                    linkTo(methodOn(FilmController.class).findFilm(film.getFilmId())).withSelfRel(),
                    link.withRel("allFilms")
            );
        } catch (ValidationException e) {
            throw new EntityNotFoundException(Entities.FILM);
        }
    }

    @Override
    public CollectionModel<EntityModel<FilmEntity>> toCollectionModel(Iterable<? extends FilmEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

}
