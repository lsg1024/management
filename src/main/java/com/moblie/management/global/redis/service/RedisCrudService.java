package com.moblie.management.global.redis.service;

import org.apache.poi.ss.formula.functions.T;
import org.eclipse.angus.mail.imap.protocol.ID;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class RedisCrudService<T, ID> {

    protected final CrudRepository<T, ID> repository;

    protected RedisCrudService(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    public void create(T entity) {
        repository.save(entity);
    }

    public Optional<T> get(ID id) {
        return repository.findById(id);
    }

    public boolean exist(ID id) {
        return repository.existsById(id);
    }

    public void delete(ID id) {
        repository.deleteById(id);
    }

}
