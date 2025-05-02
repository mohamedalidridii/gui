package services;

import entities.Traveller;
import entities.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public final class serviceTraveller extends ServiceUser {

    private PreparedStatement insertStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;
    private Statement readstmt;
    ResultSet generatedKeys=null;


    public serviceTraveller() {
        try{
            insertStmt = conn.prepareStatement("INSERT INTO user VALUES (null, ?, ?, ?, ? , ? , ? , ?, ? ,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            updateStmt=conn.prepareStatement("UPDATE user SET name=?, lastname=?, number=? ,age=?,email=?,sex=?,location=? WHERE id=?");
            deleteStmt=conn.prepareStatement("DELETE FROM user WHERE id=?");
            readstmt=conn.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
    }

    public boolean addUser(User t ) throws SQLException {


        insertStmt.setString(2,t.getPrename());
        insertStmt.setString(1,t.getName());
        insertStmt.setString(3,t.getEmail());
        insertStmt.setInt(4,t.getAge());
        insertStmt.setInt(5,t.getNumber());
        insertStmt.setString(6,t.getGender());
        insertStmt.setString(7,t.getPasswordHashed());
        insertStmt.setTimestamp(8,Timestamp.valueOf(t.getCreatedat()));
        insertStmt.setTimestamp(9,Timestamp.valueOf(t.getLastLogin()));
        insertStmt.setString(10,t.getLocation());
        insertStmt.setString(11,t.getRole().toString());


        insertStmt.executeUpdate();

        generatedKeys=insertStmt.getGeneratedKeys();


        if(generatedKeys.next()) {
            int userId = generatedKeys.getInt(1);
            String insertTraveller = "INSERT INTO traveller VALUES (?,?)";
            PreparedStatement travellerStmt = conn.prepareStatement(insertTraveller);
            travellerStmt.setInt(1, userId);
            travellerStmt.setString(2,((Traveller) t).getPassportNumber());
            return travellerStmt.executeUpdate()==1;
        }
        else{
            throw new SQLException("Failed to get id");
        }
    }

    /*
    @Override


    public boolean updateUser(User t) throws SQLException{
        //
        //           updateStmt=conn.prepareStatement("UPDATE user SET name=?, prename=?, number=? ,age=?,email=?,sexe=? WHERE id=?");
        updateStmt.setInt(8,t.getId());
        updateStmt.setString(1,t.getName());
        updateStmt.setString(2,t.getPrename());
        updateStmt.setInt(3,t.getNumber());
        updateStmt.setInt(4,t.getAge());
        updateStmt.setString(5,t.getEmail());
        updateStmt.setString(6,t.getGender());
        updateStmt.setString(7,t.getLocation());

         PreparedStatement updatetraveller=conn.prepareStatement("UPDATE traveller SET passport=? WHERE id=?");
         updatetraveller.setString(1,((Traveller)t).getPassportNumber());
         updatetraveller.setInt(2,t.getId());



        return updateStmt.executeUpdate() ==1 && updatetraveller.executeUpdate()==1;


    }*/

    public  boolean deleteUser(int id) throws SQLException{
        deleteStmt.setInt(1,id);
        return deleteStmt.executeUpdate() ==1;
    }

    public  ArrayList<User> reaadUser()throws SQLException{
        ArrayList<User> travellerlist=new ArrayList<>();
        String query="SELECT * FROM user u\n" +
                "JOIN traveller t ON u.id = t.id";
        try( ResultSet rs=readstmt.executeQuery(query)) {
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
            String passport=rs.getString("passport");

            travellerlist.add(new Traveller( id,  name,  age,  gender,  email,  number,  prename,   password,   createdat,  lastlogin,  location,  passport));

            }
            return travellerlist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

