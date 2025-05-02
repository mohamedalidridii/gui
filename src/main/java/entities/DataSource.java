package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private Connection conn;

    private static  String URL ="jdbc:mysql://localhost:3306/gui";
    private static  String USER ="root";
    private static  String PASS ="";


    private static DataSource ds;



    public DataSource() {
        try{
            conn= DriverManager.getConnection(URL,USER,PASS);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());;
        }
    }

    public Connection getConnection(){
        return conn;
    }
    public static DataSource getInstance(){
        if(ds == null){
            ds = new DataSource();
        }
        return ds;
    }


}
