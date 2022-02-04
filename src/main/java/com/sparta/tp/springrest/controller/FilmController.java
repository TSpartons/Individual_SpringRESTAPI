package com.sparta.tp.springrest.controller;

import com.sparta.tp.springrest.assemblers.FilmModelAssembler;
import com.sparta.tp.springrest.entities.CategoryEntity;
import com.sparta.tp.springrest.entities.FilmCategoryEntity;
import com.sparta.tp.springrest.entities.FilmEntity;
import com.sparta.tp.springrest.enums.Entities;
import com.sparta.tp.springrest.enums.FilmParams;
import com.sparta.tp.springrest.exceptions.EntityNotFoundException;
import com.sparta.tp.springrest.repositories.CategoryRepository;
import com.sparta.tp.springrest.repositories.FilmCategoryRepository;
import com.sparta.tp.springrest.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FilmController {

    private FilmRepository filmRepository;
    private FilmCategoryRepository filmCategoryRepository;
    private CategoryRepository categoryRepository;
    private final FilmModelAssembler assembler;

    @Autowired
    public FilmController(FilmRepository repository, FilmModelAssembler assembler,
                          CategoryRepository categoryRepository, FilmCategoryRepository filmCategoryRepository) {
        this.filmRepository = repository;
        this.assembler = assembler;
        this.categoryRepository = categoryRepository;
        this.filmCategoryRepository = filmCategoryRepository;
    }

    @GetMapping("/films")
    public CollectionModel<EntityModel<FilmEntity>> getAllFilms (
            @RequestParam(required = false, value = "title") String title,
            @RequestParam(required = false, value = "description") String description,
            @RequestParam(required = false, value = "minlength") Long minLength,
            @RequestParam(required = false, value = "maxlength") Long maxLength,
            @RequestParam(required = false, value = "rating") String rating
    ) throws ValidationException {

        List<EntityModel<FilmEntity>> films;

        if(title == null && description == null && minLength == null && maxLength == null && rating == null) {
            films = filmRepository.findAll().stream() //
                    .map(assembler::toModel) //
                    .collect(Collectors.toList());

            return CollectionModel.of(films);
        }

        List<FilmEntity> foundFilms = new ArrayList<>();

        foundFilms = title != null ? findBy(FilmParams.TITLE,title,foundFilms) : foundFilms;
        foundFilms = description != null ? findBy(FilmParams.DESCRIPTION,title,foundFilms) : foundFilms;
        foundFilms = minLength != null ? findBy(FilmParams.MINLENGTH,minLength,foundFilms) : foundFilms;
        foundFilms = maxLength != null ? findBy(FilmParams.MAXLENGTH,maxLength,foundFilms) : foundFilms;
        foundFilms = rating != null ? findBy(FilmParams.RATING,rating,foundFilms) : foundFilms;

        films = foundFilms.stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(films, assembler.link);

    }

    /**
     * Adds films to an empty film entity list or removes films if the list already contains films
     */
    public List<FilmEntity> findBy(FilmParams params, Object value, List<FilmEntity> currentlyFound) throws ValidationException{
        if(currentlyFound.isEmpty())
                switch (params){
                    case TITLE -> { for (FilmEntity film : filmRepository.findAll()) if(film.getTitle().contains((String)value)) currentlyFound.add(film);break;}
                    case DESCRIPTION -> { for (FilmEntity film : filmRepository.findAll())if (film.getDescription().contains((String)value)) currentlyFound.add(film);break;}
                    case MINLENGTH -> {for (FilmEntity film : filmRepository.findAll())if (film.getLength()>(long)value) currentlyFound.add(film); break;}
                    case MAXLENGTH -> {for (FilmEntity film : filmRepository.findAll())if (film.getLength()<(long)value) currentlyFound.add(film); break;}
                    case RATING -> {for (FilmEntity film : filmRepository.findAll())if (film.getRating().contains((String)value)) currentlyFound.add(film); break;}
                }
        else {
            switch (params) {
                case DESCRIPTION -> {currentlyFound.removeIf(film -> !film.getDescription().contains((String)value)); break;}
                case MINLENGTH -> {currentlyFound.removeIf(film -> film.getLength()<(long)value); break;}
                case MAXLENGTH -> {currentlyFound.removeIf(film -> film.getLength()>(long)value); break;}
                case RATING -> {currentlyFound.removeIf(film -> !film.getRating().contains((String)value)); break;}
            }
        }
        return currentlyFound;
    }

    @GetMapping("films/{id}")
    public EntityModel<FilmEntity> findFilm(@PathVariable Integer id) throws ValidationException {

        FilmEntity film = filmRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Entities.FILM));

        return assembler.toModel(film);
    }

    public FilmEntity filmForCategory(Integer id) throws ValidationException{
        FilmEntity film = filmRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Entities.FILM));
        return film;
    }

    @PostMapping("/films")
    ResponseEntity<?> addFilm(@RequestBody FilmEntity film) {

        EntityModel<FilmEntity> model = assembler.toModel(filmRepository.save(film));

        return  ResponseEntity //
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(model);
    }

//    @PostMapping("/films")
//    public FilmEntity addFilm(@RequestBody FilmEntity film) {
//        return repository.save(film);
//    }

}
