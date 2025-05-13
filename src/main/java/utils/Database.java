package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    final String URl="jdbc:mysql://localhost:3306/3b11";
    final String USERNAME="root";
    final String PASSWORD="";
    Connection connection;

    static Database instance;
    private Database(){
        try {
            connection= DriverManager.getConnection(URl,USERNAME,PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public  static Database getInstance(){
        if(instance==null)
            instance=new Database();
        return instance;

    }

    public Connection getConnection() {
        return connection;
    }
}

