package services;

import entities.AccessLevel;
import entities.Admin;
import entities.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ServiceAdmin {


    protected Connection conn = DataSource.getInstance().getConnection();

    private PreparedStatement insertStmt = null;
    private PreparedStatement updateStmt = null;
    private PreparedStatement deleteStmt = null;
    private Statement readStmt = null;
    private ResultSet generatedKeys = null;

    public ServiceAdmin() {
        try{
            insertStmt = conn.prepareStatement("INSERT INTO user VALUES (null, ?, ?, ?, ? , ? , ? , ?, ? ,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            updateStmt=conn.prepareStatement("UPDATE user SET name=?, lastname=?, number=? ,age=?,email=?,sex=?,location=? WHERE id=?");
            deleteStmt=conn.prepareStatement("delete from admin where id=?");
            readStmt=conn.createStatement();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());;
        }
    }



    public boolean addAdmin(Admin admin) throws SQLException {
        insertStmt.setString(2,admin.getPrename());
        insertStmt.setString(1,admin.getName());
        insertStmt.setString(3,admin.getEmail());
        insertStmt.setInt(4,admin.getAge());
        insertStmt.setInt(5,admin.getNumber());
        insertStmt.setString(6,admin.getGender());
        insertStmt.setString(7,admin.getPasswordHashed());
        insertStmt.setTimestamp(8,Timestamp.valueOf(admin.getCreatedat()));
        insertStmt.setTimestamp(9,Timestamp.valueOf(admin.getLastLogin()));
        insertStmt.setString(10,admin.getLocation());
        insertStmt.setString(11,admin.getRole().toString());


        insertStmt.executeUpdate();

        generatedKeys=insertStmt.getGeneratedKeys();


        if(generatedKeys.next()) {
            int userId = generatedKeys.getInt(1);
            String insertAdmin = "INSERT INTO admin VALUES (?,?)";
            PreparedStatement adminStmt = conn.prepareStatement(insertAdmin);
            adminStmt.setInt(1, userId);
            adminStmt.setString(2,admin.getAccessLevel().toString());
            return adminStmt.executeUpdate()==1;
        }
        else{
            throw new SQLException("Failed to get id");
        }
    }

    public boolean updateAdmin(Admin admin) throws SQLException {
        updateStmt.setInt(8,admin.getId());
        updateStmt.setString(1,admin.getName());
        updateStmt.setString(2,admin.getPrename());
        updateStmt.setInt(3,admin.getNumber());
        updateStmt.setInt(4,admin.getAge());
        updateStmt.setString(5,admin.getEmail());
        updateStmt.setString(6,admin.getGender());
        updateStmt.setString(7,admin.getLocation());

        String updateadmin="UPDATE admin SET accesslevel=? WHERE id=?";
        PreparedStatement updateAdminStmt = conn.prepareStatement(updateadmin);
        updateAdminStmt.setString(1,admin.getAccessLevel().toString());

        return updateStmt.executeUpdate()==1 && updateAdminStmt.executeUpdate()==1;

    }


    public boolean deleteAdmin(int id) throws SQLException {
        deleteStmt.setInt(1,id);
        return deleteStmt.executeUpdate()>0;
    }

    public ArrayList<Admin> readAll() throws SQLException {
        ArrayList<Admin> admins = new ArrayList<>();
        String query="SELECT * FROM user u\n" +
                "JOIN admin t ON u.id = t.id";
        try(ResultSet rs=readStmt.executeQuery(query)){
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
                String accesslevelDB=rs.getString("accesslevel");
                AccessLevel accessLevel=AccessLevel.valueOf(accesslevelDB);

                admins.add(new Admin(id,  name,  age,  gender,  email,  number,  prename,   password,   createdat,  lastlogin,  location,accessLevel));
            }
        }
        return admins;
    }

}
