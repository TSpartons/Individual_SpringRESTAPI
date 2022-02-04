package com.sparta.tp.springrest.repositories;

import com.sparta.tp.springrest.entities.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<ActorEntity, Integer> {

}
