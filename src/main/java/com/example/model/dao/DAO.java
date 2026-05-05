package com.example.model.dao;


public interface DAO<T> {
    T save(T t) throws Exception;

    boolean delete(int t) throws Exception;


}

