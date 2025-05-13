package services;

import com.mysql.cj.protocol.Resultset;
import entities.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;
import utils.Database;


public abstract class ServiceUser {

    protected Connection conn= Database.getInstance().getConnection();



    public abstract boolean addUser(User user) throws SQLException;
    public  boolean updateUser(User user) throws SQLException{
        boolean IsRoleChanged;
        IsRoleChanged=!(user.getRole().equals(getRole(user.getId())));
        PreparedStatement updateStmt =null;

        conn.setAutoCommit(false);

        try {
            if (user.getPasswordHashed()==null)
                 updateStmt = conn.prepareStatement("UPDATE user SET name=?, lastname=?, number=? ,age=?,email=?,sex=?,location=?,type=? WHERE id=?");
            else
                 updateStmt = conn.prepareStatement("UPDATE user SET name=?, lastname=?, number=? ,age=?,email=?,sex=?,location=?,passwordHashed=?,type=? WHERE id=?");



            updateStmt.setInt(10,user.getId());
            updateStmt.setString(1,user.getName());
            updateStmt.setString(2,user.getPrename());
            updateStmt.setInt(3,user.getNumber());
            updateStmt.setInt(4,user.getAge());
            updateStmt.setString(5,user.getEmail());
            updateStmt.setString(6,user.getGender());
            updateStmt.setString(7,user.getLocation());
            updateStmt.setString(8,user.getPasswordHashed());
            updateStmt.setString(9,user.getRole().toString());

            updateStmt.executeUpdate();
            if(!IsRoleChanged && user instanceof Traveller){
                PreparedStatement updateTav= conn.prepareStatement("UPDATE traveller SET passport=? WHERE id=?");
                updateTav.setString(1,((Traveller) user).getPassportNumber());
                updateTav.setInt(2,user.getId());
                updateTav.executeUpdate();


            }
            else if(!IsRoleChanged && user instanceof Seller){
                PreparedStatement updateSel= conn.prepareStatement("UPDATE seller SET storename=? WHERE id=?");
                updateSel.setString(1,((Seller) user).getStoreName());
                updateSel.setInt(2,user.getId());
                updateSel.executeUpdate();
            }
            else if(IsRoleChanged && user instanceof Traveller){
                PreparedStatement deleteSel= conn.prepareStatement("DELETE FROM seller WHERE id=?");
                deleteSel.setInt(1,user.getId());
                PreparedStatement insertTrav =conn.prepareStatement("INSERT INTO traveller VALUES(?,?)");
                insertTrav.setInt(1,user.getId());
                insertTrav.setString(2,((Traveller)user).getPassportNumber());
                deleteSel.executeUpdate();
                insertTrav.executeUpdate();

            }
            else if(IsRoleChanged && user instanceof Seller){
                PreparedStatement deleteTrav= conn.prepareStatement("DELETE FROM traveller WHERE id=?");
                deleteTrav.setInt(1,user.getId());
                PreparedStatement insertSel =conn.prepareStatement("INSERT INTO seller VALUES(?,?)");
                insertSel.setInt(1,user.getId());
                insertSel.setString(2,((Seller)user).getStoreName());
                deleteTrav.executeUpdate();
                insertSel.executeUpdate();

            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
        finally {
            conn.setAutoCommit(true);
        }







    }

    public Role getRole(int id) throws SQLException{
        PreparedStatement statement = conn.prepareStatement("SELECT type FROM user WHERE id=?");
        statement.setInt(1, id);
        try(ResultSet rs = statement.executeQuery()){
            if(rs.next()){
                return Role.valueOf(rs.getString("type"));
            }
        }
        return null;
    }




    public abstract boolean deleteUser(int  id) throws SQLException;
    public abstract ArrayList<User> reaadUser()throws SQLException;
    public  ArrayList<User> reaadAll()throws SQLException{
        ArrayList<User> userList=new ArrayList<>();
        String query="SELECT * FROM user u";
        Statement readstmt=conn.createStatement();
        try( ResultSet rs=readstmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String prename = rs.getString("lastname");
                int number = rs.getInt("number");
                int age = rs.getInt("age");
                String gender = rs.getString("sex");
                String email = rs.getString("email");
                String password = rs.getString("passwordHashed");
                LocalDateTime createdat = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime lastlogin = rs.getTimestamp("lastlogin").toLocalDateTime();
                String location = rs.getString("location");
                String type = rs.getString("type");

                if (type.equals("Traveller"))
                    userList.add(new Traveller(id, name, age, gender, email, number, prename, password, createdat, lastlogin, location));
                else if (type.equals("Seller"))
                    userList.add(new Seller(id, name, age, gender, email, number, prename, password, createdat, lastlogin, location));
                else if(type.equals("Admin"))
                    userList.add(new Admin(id, name, age, gender, email, number, prename, password, createdat, lastlogin, location));


            }
            return userList;
        }
    }




    public ArrayList<User> searchUserByEmail(String email)throws SQLException{
        ArrayList<User> userList=new ArrayList<>();
        String query ="SELECT * FROM user WHERE email LIKE ?";
        PreparedStatement searchStmt=conn.prepareStatement(query);
        searchStmt.setString(1, "%" + email + "%");
        try(ResultSet rs=searchStmt.executeQuery()){
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String prename = rs.getString("lastname");
                String dbEamil = rs.getString("email");
                int number = rs.getInt("number");
                int age = rs.getInt("age");
                String gender = rs.getString("sex");
                String password = rs.getString("passwordHashed");
                LocalDateTime createdat = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime lastlogin = rs.getTimestamp("lastlogin").toLocalDateTime();
                String location = rs.getString("location");
                String type = rs.getString("type");

                if (type.equals("Traveller"))
                    userList.add(new Traveller(id, name, age, gender, dbEamil, number, prename, password, createdat, lastlogin, location));
                else if (type.equals("Seller"))
                    userList.add(new Seller(id, name, age, gender, dbEamil, number, prename, password, createdat, lastlogin, location));
                else if(type.equals("Admin"))
                    userList.add(new Admin(id, name, age, gender, dbEamil, number, prename, password, createdat, lastlogin, location));




            }
            return userList;



        }
    }
    /*

    public ArrayList<User> searchUserByNumber(int number)throws SQLException{
        ArrayList<User> userList=new ArrayList<>();
        String query ="SELECT * FROM user WHERE CAST(number as char) LIKE ?";
        PreparedStatement searchStmt=conn.prepareStatement(query);
        searchStmt.setString(1, "%" + number + "%");
        try(ResultSet rs=searchStmt.executeQuery()){
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String prename = rs.getString("lastname");
                String dbEamil = rs.getString("email");
                int trueNumber = rs.getInt("number");
                int age = rs.getInt("age");
                String gender = rs.getString("sex");
                String password = rs.getString("passwordHashed");
                LocalDateTime createdat = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime lastlogin = rs.getTimestamp("lastlogin").toLocalDateTime();
                String location = rs.getString("location");
                String type = rs.getString("type");

                if (type.equals("Traveller"))
                    userList.add(new Traveller(id, name, age, gender, dbEamil, trueNumber, prename, password, createdat, lastlogin, location));
                else if (type.equals("Seller"))
                    userList.add(new Seller(id, name, age, gender, dbEamil, trueNumber, prename, password, createdat, lastlogin, location));
                else if(type.equals("Admin"))
                    userList.add(new Admin(id, name, age, gender, dbEamil, trueNumber, prename, password, createdat, lastlogin, location));




            }
            return userList;



        }
    }





    public ArrayList<User> searchUserByNameLastName(String name,String lastName)throws SQLException{
        ArrayList<User> userList=new ArrayList<>();
        String query ="SELECT * FROM user WHERE name LIKE ? AND lastname LIKE ?";
        PreparedStatement searchStmt=conn.prepareStatement(query);
        searchStmt.setString(1,"%"+name+"%");
        searchStmt.setString(2,"%"+lastName+"%");
        try(ResultSet rs=searchStmt.executeQuery()){
            while (rs.next()) {
                int id = rs.getInt("id");
                String dbName = rs.getString("name");
                String dbLastName = rs.getString("lastname");
                int number = rs.getInt("number");
                int age = rs.getInt("age");
                String gender = rs.getString("sex");
                String password = rs.getString("passwordHashed");
                LocalDateTime createdat = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime lastlogin = rs.getTimestamp("lastlogin").toLocalDateTime();
                String location = rs.getString("location");
                String type = rs.getString("type");
                String email = rs.getString("email");

                if (type.equals("Traveller"))
                    userList.add(new Traveller(id, dbName, age, gender, email, number, dbLastName, password, createdat, lastlogin, location));
                else if (type.equals("Seller"))
                    userList.add(new Seller(id, dbName, age, gender, email, number, dbLastName, password, createdat, lastlogin, location));
                else if (type.equals("Admin"))
                    userList.add(new Admin(id, dbName, age, gender, email, number, dbLastName, password, createdat, lastlogin, location));


            }
            return userList;



        }
    }


    public ArrayList<User> searchUserByLastName(String lastname)throws SQLException{
        ArrayList<User> userList=new ArrayList<>();
        String query ="SELECT * FROM user WHERE lastname LIKE ?";
        PreparedStatement searchStmt=conn.prepareStatement(query);
        searchStmt.setString(1, "%"+lastname+"%");
        try(ResultSet rs=searchStmt.executeQuery()){
            while (rs.next()) {
                int id = rs.getInt("id");
                String dbLastName = rs.getString("lastname");
                String email = rs.getString("email");
                String name = rs.getString("name");
                int number = rs.getInt("number");
                int age = rs.getInt("age");
                String gender = rs.getString("sex");
                String password = rs.getString("passwordHashed");
                LocalDateTime createdat = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime lastlogin = rs.getTimestamp("lastlogin").toLocalDateTime();
                String location = rs.getString("location");
                String type = rs.getString("type");

                if (type.equals("Traveller"))
                    userList.add(new Traveller(id, name, age, gender, email, number, dbLastName, password, createdat, lastlogin, location));
                else if (type.equals("Seller"))
                    userList.add(new Seller(id, name, age, gender, email, number, dbLastName, password, createdat, lastlogin, location));
                else if(type.equals("Admin"))
                    userList.add(new Admin(id, name, age, gender, email, number, dbLastName, password, createdat, lastlogin, location));




            }
            return userList;



        }
    }


    public ArrayList<User> searchUserByName(String name)throws SQLException{
        ArrayList<User> userList=new ArrayList<>();
        String query ="SELECT * FROM user WHERE name LIKE ?";
        PreparedStatement searchStmt=conn.prepareStatement(query);
        searchStmt.setString(1, "%"+name+"%");
        try(ResultSet rs=searchStmt.executeQuery()){
            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String dbName = rs.getString("name");
                String prename = rs.getString("lastname");
                int number = rs.getInt("number");
                int age = rs.getInt("age");
                String gender = rs.getString("sex");
                String password = rs.getString("passwordHashed");
                LocalDateTime createdat = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime lastlogin = rs.getTimestamp("lastlogin").toLocalDateTime();
                String location = rs.getString("location");
                String type = rs.getString("type");

                if (type.equals("Traveller"))
                    userList.add(new Traveller(id, dbName, age, gender, email, number, prename, password, createdat, lastlogin, location));
                else if (type.equals("Seller"))
                    userList.add(new Seller(id, dbName, age, gender, email, number, prename, password, createdat, lastlogin, location));
                else if(type.equals("Admin"))
                    userList.add(new Admin(id, dbName, age, gender, email, number, prename, password, createdat, lastlogin, location));




            }
            return userList;



        }
    }*/

    public User searchUserById(int id)throws SQLException{
        PreparedStatement searchStmt=conn.prepareStatement("SELECT * FROM user WHERE id =?");
        searchStmt.setInt(1, id);
        User user=null;
        try(ResultSet rs=searchStmt.executeQuery()){
            while (rs.next()) {
                String email = rs.getString("email");
                String dbName = rs.getString("name");
                String prename = rs.getString("lastname");
                int number = rs.getInt("number");
                int age = rs.getInt("age");
                String gender = rs.getString("sex");
                String password = rs.getString("passwordHashed");
                LocalDateTime createdat = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime lastlogin = rs.getTimestamp("lastlogin").toLocalDateTime();
                String location = rs.getString("location");
                String type = rs.getString("type");

                if (type.equals("Traveller")){
                    String passport="";
                    PreparedStatement passportStmt=conn.prepareStatement("SELECT passport from traveller WHERE id = ?");
                    passportStmt.setInt(1, id);
                    try(ResultSet rspass=passportStmt.executeQuery()){
                        while (rspass.next()) {
                             passport = rspass.getString("passport");
                        }
                    }

                    user=new Traveller(id, dbName, age, gender, email, number, prename, password, createdat, lastlogin, location,passport);


                }
                else if (type.equals("Seller")){
                    String storename="";
                    PreparedStatement passportStmt=conn.prepareStatement("SELECT storeName from seller WHERE id = ?");
                    passportStmt.setInt(1, id);
                    try(ResultSet rspass=passportStmt.executeQuery()){
                        while (rspass.next()) {
                            storename = rspass.getString("storeName");
                        }
                    }
                    user=new Seller(id, dbName, age, gender, email, number, prename, password, createdat, lastlogin, location,storename);

                }
                else if(type.equals("Admin"))
                    user=new Admin(id, dbName, age, gender, email, number, prename, password, createdat, lastlogin, location);
            }
            return user;

        }
    }

    public boolean emailExists(String email)throws SQLException{
        PreparedStatement searchStmt=conn.prepareStatement("SELECT 1 FROM user WHERE email = ?");
        searchStmt.setString(1, email);
        try(ResultSet rs=searchStmt.executeQuery()){
            return  (rs.next()) ;
        }
    }

    public boolean changePassword(String email, String newPassword)throws SQLException{
        PreparedStatement searchStmt=conn.prepareStatement("update password = ? FROM user WHERE email = ?");
        String newHashedPAss=BCrypt.hashpw(newPassword , BCrypt.gensalt());
        searchStmt.setString(1, newHashedPAss);
        searchStmt.setString(2, email);
         return searchStmt.executeUpdate()==1;

    }



    public ArrayList<User> combinedSearch(String firstNmae,String lastName ,String location,String email ,String number)throws SQLException{
        ArrayList<User> userList=new ArrayList<>();
        ArrayList<String> conditions=new ArrayList<>();

        if(!firstNmae.isEmpty()){
            conditions.add("name LIKE '%"+firstNmae+"%'");
        }
        if(!lastName.isEmpty()){
            conditions.add("lastname LIKE '%"+lastName+"%'");
        }
        if(!email.isEmpty()){
            conditions.add("email LIKE '%"+email+"%'");
        }
        if(!number.isEmpty()){
            conditions.add(" cast(number as char) LIKE '%"+number+"%'");
        }
        if(!location.isEmpty()){
            conditions.add("location LIKE '%"+location+"%'");
        }
        StringBuilder query = new StringBuilder("SELECT * FROM user WHERE ");

        if (!conditions.isEmpty()) {
            query.append(String.join(" AND ", conditions));
        }
        PreparedStatement searchStmt=conn.prepareStatement(query.toString());
        try(ResultSet rs=searchStmt.executeQuery()){
            while (rs.next()) {
                int id = rs.getInt("id");
                String dbemail = rs.getString("email");
                String dbName = rs.getString("name");
                String dbLastName = rs.getString("lastname");
                int dbnumber = rs.getInt("number");
                int age = rs.getInt("age");
                String gender = rs.getString("sex");
                String password = rs.getString("passwordHashed");
                LocalDateTime createdat = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime lastlogin = rs.getTimestamp("lastlogin").toLocalDateTime();
                String dblocation = rs.getString("location");
                String type = rs.getString("type");

                if (type.equals("Traveller"))
                    userList.add(new Traveller(id, dbName, age, gender, dbemail, dbnumber, dbLastName, password, createdat, lastlogin, dblocation));
                else if (type.equals("Seller"))
                    userList.add(new Seller(id, dbName, age, gender, dbemail, dbnumber, dbLastName, password, createdat, lastlogin, dblocation));
                else if(type.equals("Admin"))
                    userList.add(new Admin(id, dbName, age, gender, dbemail, dbnumber, dbLastName, password, createdat, lastlogin, dblocation));

            }
            return userList;



        }
    }




}

