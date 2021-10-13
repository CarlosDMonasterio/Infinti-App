package org.ih.dao;

/**
 * Interface for the persistent layer
 *
 * @author Hector Plahar
 */
public interface Repository<T extends DatabaseModel> {

    T get(long id);

    T create(T model);

    T update(T object);

    void delete(T t);
}
