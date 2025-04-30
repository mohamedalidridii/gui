package services;

import entities.Seller;
import entities.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public final class ServiceSeller extends ServiceUser{

    private PreparedStatement insertStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;
    private Statement readAll;
    ResultSet generatedKeys;


    public ServiceSeller() {
        try{
        insertStmt=conn.prepareStatement("INSERT INTO user VALUES (null, ?, ?, ?, ? , ? , ? , ?, ? ,?,?,?)",Statement.RETURN_GENERATED_KEYS);
        updateStmt=conn.prepareStatement("UPDATE user SET name=?, lastname=?, number=? ,age=?,email=?,sex=?,location=?,passwordHashed=?,type=? WHERE id=?");
        deleteStmt=conn.prepareStatement("DELETE FROM user WHERE id=?");
        readAll=conn.createStatement();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }








    @Override
    public boolean addUser(User t) throws SQLException {
        insertStmt.setString(2,t.getPrename());
        insertStmt.setString(1,t.getName());
        insertStmt.setString(3,t.getEmail());
        insertStmt.setInt(4,t.getAge());
        insertStmt.setInt(5,t.getNumber());
        insertStmt.setString(6,t.getGender());
        insertStmt.setString(7,t.getPasswordHashed());
        insertStmt.setTimestamp(8, Timestamp.valueOf(t.getCreatedat()));
        insertStmt.setTimestamp(9,Timestamp.valueOf(t.getLastLogin()));
        insertStmt.setString(10,t.getLocation());
        insertStmt.setString(11,t.getRole().toString());



        insertStmt.executeUpdate();
        generatedKeys=insertStmt.getGeneratedKeys();

        if(generatedKeys.next()){
            int id=generatedKeys.getInt(1);
            PreparedStatement insertSeller= conn.prepareStatement("INSERT INTO seller VALUES (? ,?)");
            insertSeller.setInt(1,id);
            insertSeller.setString(2,((Seller)t).getStoreName());
            return insertSeller.executeUpdate()==1;
        }
        return false;

    }


    /*
    public boolean updateUser(User s) throws SQLException {
        updateStmt.setInt(10,s.getId());
        updateStmt.setString(1,s.getName());
        updateStmt.setString(2,s.getPrename());
        updateStmt.setInt(3,s.getNumber());
        updateStmt.setInt(4,s.getAge());
        updateStmt.setString(5,s.getEmail());
        updateStmt.setString(6,s.getGender());
        updateStmt.setString(7,s.getLocation());
        updateStmt.setString(8,s.getPasswordHashed());
        updateStmt.setString(9,s.getRole().toString().toString());

        PreparedStatement updateSeller=conn.prepareStatement("UPDATE seller SET storeName=? WHERE id=?");
        updateSeller.setString(1,((Seller)s).getStoreName());
        updateSeller.setInt(2,s.getId());


        return updateStmt.executeUpdate() ==1 && updateSeller.executeUpdate()==1;
    }*/

    @Override
    public boolean deleteUser(int id) throws SQLException {
        deleteStmt.setInt(1,id);
        return deleteStmt.executeUpdate()==1;
    }

    public ArrayList<User> reaadUser()throws SQLException {
        ArrayList<User> sellersList = new ArrayList<>();

        String query="SELECT * FROM user u\n" +
                "JOIN seller s ON u.id = s.id";

        try (ResultSet rs=readAll.executeQuery(query)){
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("name");
                String prename=rs.getString("lastname");
                int number=rs.getInt("number");
                int age=rs.getInt("age");
                String gender=rs.getString("sex");
                String email=rs.getString("email");
                String password=rs.getString("passwordHashed");
                LocalDateTime createdat=rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime lastlogin=rs.getTimestamp("lastlogin").toLocalDateTime();
                String location=rs.getString("location");

                String storeName=rs.getString("storeName");

                sellersList.add(new Seller( id,  name,  age,  gender,  email,  number,  prename,   password,   createdat,  lastlogin,  location,storeName));
            }


        }
        return sellersList;
    }
}
