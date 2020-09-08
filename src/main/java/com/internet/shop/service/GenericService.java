package com.internet.shop.service;

import java.util.List;

public interface GenericService<T, K> {

    T create(T t);

    T update(T t);

    T getById(K k);

    List<T> getAll();

    boolean delete(K k);
}
