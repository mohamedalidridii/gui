package entities;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public abstract class ServiceUser {

    protected Connection conn=DataSource.getInstance().getConnection();



    public abstract boolean addUser(User user) throws SQLException;
    public abstract boolean updateUser(User user) throws SQLException;


    public abstract boolean deleteUser(User user) throws SQLException;
    public abstract ArrayList<User> reaadUser()throws SQLException;

}

