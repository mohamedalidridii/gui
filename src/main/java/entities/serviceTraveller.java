package entities;

import javax.security.sasl.SaslException;
import java.sql.*;
import java.util.ArrayList;

public final class serviceTraveller extends ServiceUser {

    private PreparedStatement insertStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;
    private Statement readstmt;
    ResultSet generatedKeys=null;


    public serviceTraveller() {
        try{
            insertStmt = conn.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ? , ? , ?)", Statement.RETURN_GENERATED_KEYS);
            updateStmt=conn.prepareStatement("UPDATE user SET name=?, prename=?, number=? ,age=?,email=?,sex=? WHERE id=?");
            deleteStmt=conn.prepareStatement("DELETE FROM user WHERE id=?");
            readstmt=conn.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
    }

    public boolean addUser(User t ) throws SQLException {

        insertStmt.setInt(1,t.getId());
        insertStmt.setString(3,t.getPrename());
        insertStmt.setString(2,t.getName());
        insertStmt.setString(4,t.getEmail());
        insertStmt.setInt(5,t.getAge());
        insertStmt.setInt(6,t.getNumber());
        insertStmt.setString(7,t.getGender());

        insertStmt.executeUpdate();

        generatedKeys=insertStmt.getGeneratedKeys();

        if(generatedKeys.next()) {
            int userId = generatedKeys.getInt(1);
            String insertTraveller = "INSERT INTO traveller VALUES (?)";
            PreparedStatement travellerStmt = conn.prepareStatement(insertTraveller);
            travellerStmt.setInt(1, userId);

            travellerStmt.executeUpdate();
        }
        else{
            throw new SQLException("Failed to get id");
        }
        return true;
    }


    @Override


    public boolean updateUser(User t) throws SQLException{
        //
        //           updateStmt=conn.prepareStatement("UPDATE user SET name=?, prename=?, number=? ,age=?,email=?,sexe=? WHERE id=?");
        boolean updated=false;
        updateStmt.setInt(7,t.getId());
        updateStmt.setString(1,t.getName());
        updateStmt.setString(2,t.getPrename());
        updateStmt.setInt(3,t.getNumber());
        updateStmt.setInt(4,t.getAge());
        updateStmt.setString(5,t.getEmail());
        updateStmt.setString(6,t.getGender());
        if (updateStmt.executeUpdate() ==1)    updated=true;

        return updated;


    }

    public  boolean deleteUser(User t) throws SQLException{
        boolean deleted=false;
        deleteStmt.setInt(1,t.getId());
        if (deleteStmt.executeUpdate() ==1)  deleted=true;
        return deleted;
    }

    public  ArrayList<User> reaadUser()throws SQLException{
        ArrayList<User> travellerlist=new ArrayList<>();
        String query="SELECT * FROM user u\n" +
                "JOIN traveller t ON u.id = t.id";
        try( ResultSet rs=readstmt.executeQuery(query)) {
            while(rs.next()){
            int id=rs.getInt("id");
            String name=rs.getString("name");
            String prename=rs.getString("prename");
            int number=rs.getInt("number");
            int age=rs.getInt("age");
            String gender=rs.getString("sex");
            String email=rs.getString("email");

            travellerlist.add(new Traveller( id,  name,  age,  gender,  email,  number,  prename));

            }
            return travellerlist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

