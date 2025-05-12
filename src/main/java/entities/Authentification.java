package entities;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class Authentification {
    protected Connection conn=DataSource.getInstance().getConnection();
    LoginRequest loginRequest;
    public Authentification(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;

    }
    public boolean authentification() throws SQLException {

        try ( PreparedStatement readStmt = conn.prepareStatement("SELECT id ,passwordHashed FROM USER WHERE EMAIL=?")) {
            readStmt.setString(1, loginRequest.getEmail());
            try (ResultSet rs = readStmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String hashedPassword = rs.getString("passwordHashed");
                    if(BCrypt.checkpw(loginRequest.getPassword(), hashedPassword))
                    {
                        PreparedStatement loginStmt=conn.prepareStatement("UPDATE USER SET lastlogin=? where email=?");
                        loginStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                        loginStmt.setString(2, loginRequest.getEmail());
                        CurrentUser currentUser=new CurrentUser(id);
                        return loginStmt.executeUpdate()==1;

                    }
                }
                return false;


            }
        }






    }
}
