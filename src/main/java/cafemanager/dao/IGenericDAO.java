package cafemanager.dao;

import java.util.List;

public interface IGenericDAO<T> {
    boolean insert(T entity) throws Exception;
    boolean update(T entity) throws Exception;
    boolean delete(int id) throws Exception;
    T findById(int id) throws Exception;
    List<T> findAll() throws Exception;
}
