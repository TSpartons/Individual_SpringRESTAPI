package com.sparta.tp.springrest.repositories;

import com.sparta.tp.springrest.entities.FilmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<FilmEntity, Integer> {

}
