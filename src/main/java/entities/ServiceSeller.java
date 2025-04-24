package entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public final class ServiceSeller extends ServiceUser{

    private PreparedStatement insertStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;
    private Statement readAll;
    ResultSet generatedKeys;


    public ServiceSeller() {
        try{
        insertStmt=conn.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ? , ? , ?)",Statement.RETURN_GENERATED_KEYS);
        updateStmt=conn.prepareStatement("UPDATE user SET name=?, prename=?, number=? ,age=?,email=?,sex=? WHERE id=?");
        deleteStmt=conn.prepareStatement("DELETE FROM user WHERE id=?");
        readAll=conn.createStatement();

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }








    @Override
    public boolean addUser(User t) throws SQLException {
        insertStmt.setInt(1,t.getId());
        insertStmt.setString(3,t.getPrename());
        insertStmt.setString(2,t.getName());
        insertStmt.setString(4,t.getEmail());
        insertStmt.setInt(5,t.getAge());
        insertStmt.setInt(6,t.getNumber());
        insertStmt.setString(7,t.getGender());

        insertStmt.executeUpdate();

        generatedKeys=insertStmt.getGeneratedKeys();

        if(generatedKeys.next()){
            int id=generatedKeys.getInt(1);
            PreparedStatement insertSeller= conn.prepareStatement("INSERT INTO seller VALUES (?)");
            insertSeller.setInt(1,id);
            insertSeller.executeUpdate();
            return true;

        }
        return false;

    }

    @Override
    public boolean updateUser(User s) throws SQLException {
        boolean updated=false;
        updateStmt.setInt(7,s.getId());
        updateStmt.setString(1,s.getName());
        updateStmt.setString(2,s.getPrename());
        updateStmt.setInt(3,s.getNumber());
        updateStmt.setInt(4,s.getAge());
        updateStmt.setString(5,s.getEmail());
        updateStmt.setString(6,s.getGender());
        if (updateStmt.executeUpdate() ==1)    updated=true;

        return updated;
    }

    @Override
    public boolean deleteUser(User s) throws SQLException {

        deleteStmt.setInt(1,s.getId());
        if (deleteStmt.executeUpdate()==1)    return true;
        return false;
    }

    public ArrayList<User> reaadUser()throws SQLException {
        ArrayList<User> sellersList = new ArrayList<>();

        String query="SELECT * FROM user u\n" +
                "JOIN seller s ON u.id = s.id";

        try (ResultSet rs=readAll.executeQuery(query)){
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("name");
                String prename=rs.getString("prename");
                int number=rs.getInt("number");
                int age=rs.getInt("age");
                String gender=rs.getString("sex");
                String email=rs.getString("email");

                sellersList.add(new Seller(id,  name,  age,  gender,  email,  number,  prename));
            }


        }
        return sellersList;
    }
}
