package com.sparta.tp.springrest.repositories;

import com.sparta.tp.springrest.entities.FilmCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmCategoryRepository  extends JpaRepository<FilmCategoryEntity, Integer> {
}
