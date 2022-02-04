package com.sparta.tp.springrest.exceptions;

import com.sparta.tp.springrest.enums.Entities;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Integer id, Entities entity) {
        super("Could not find" + entity.getEntity() + ": " + id);
    }

    public EntityNotFoundException(Entities entity) {
        super("Could not iterate through " + entity.getEntity() + "s");
    }

    public EntityNotFoundException(Integer id) {
        super("Cannot delete entity from original database");
    }
}
