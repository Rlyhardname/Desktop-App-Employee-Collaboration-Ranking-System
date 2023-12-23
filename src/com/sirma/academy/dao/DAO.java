package com.sirma.academy.dao;

import java.util.List;
import java.util.Optional;

/**
 *
 * @param <T> Class object
 * @param <S> Class object Id
 *
 */
public interface DAO<T,S> {
    Optional<T> findBy(T object);
    Optional<T> findById(S id);

    List<T> findAll();

    int save(T object);

    int saveAll(List<T> objects);

    void update(Long id, T object);

    void updateAll(List<T> object1, String object2);

    int deleteById(S id);

    void deleteAll(List<T> objects);

    boolean existsById(S id);
}
