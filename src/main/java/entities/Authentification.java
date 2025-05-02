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

        try ( PreparedStatement readStmt = conn.prepareStatement("SELECT passwordHashed FROM USER WHERE EMAIL=?")) {
            readStmt.setString(1, loginRequest.getEmail());
            try (ResultSet rs = readStmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("passwordHashed");
                    return BCrypt.checkpw(loginRequest.getPassword(), hashedPassword);
                }
                return false;


            }
        }






    }
}
