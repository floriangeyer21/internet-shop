package com.internet.shop.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, K> {

    T create(T t);

    T update(T t);

    Optional<T> getById(K k);

    List<T> getAll();

    boolean delete(K k);
}
