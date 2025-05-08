package services;

import entities.Product;
import utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduct implements Iservice<Product> {
    Connection connection;
    public ServiceProduct() {
        connection= Database.getInstance().getConnection();
    }
    @Override
    public void ajouter(Product product) throws SQLException {
    }

    @Override
    public void supprimer(Product product) throws SQLException {

    }

    @Override
    public void modifier(Product product) throws SQLException {


    }

    @Override
    public List<Product> afficher() throws SQLException {
        return List.of();
    }

}
