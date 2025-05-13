package DAO;

import java.util.List;
import java.util.Optional;

public interface IGenericDAO <T>{
    List<T> getAll();
    Optional<T> getById(int id);
    boolean add(T t);
    boolean update(T t);
    boolean delete(int id);
}
