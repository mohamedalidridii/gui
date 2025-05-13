package services;

import entities.Reclamation;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;

public class ServiceReclamation {

    protected Connection conn = DataSource.getInstance().getConnection();

    private PreparedStatement insertStmt;
    private PreparedStatement selectStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;

    public ServiceReclamation() {
        try {
            insertStmt = conn.prepareStatement("INSERT INTO reclamation(idUser, idVoyage, message) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            selectStmt = conn.prepareStatement("SELECT * FROM reclamation WHERE id=?");
            updateStmt = conn.prepareStatement("UPDATE reclamation SET idUser=?, idVoyage=?, message=? WHERE id=?");
            deleteStmt = conn.prepareStatement("DELETE FROM reclamation WHERE id=?");
        } catch (SQLException e) {
            System.out.println("Error initializing statements: " + e.getMessage());
        }
    }

    public boolean addReclamation(Reclamation reclamation) {
        try {
            insertStmt.setInt(1, reclamation.getIdUser());
            insertStmt.setInt(2, reclamation.getIdVoyage());
            insertStmt.setString(3, reclamation.getMessage());
            if (insertStmt.executeUpdate() > 0) {
                ResultSet rs = insertStmt.getGeneratedKeys();
                if (rs.next()) {
                    reclamation.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding reclamation: " + e.getMessage());
        }
        return false;
    }

    public Reclamation getReclamation(int id) {
        try {
            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                return new Reclamation(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getInt("idVoyage"),
                        rs.getString("message")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching reclamation: " + e.getMessage());
        }
        return null;
    }

    public boolean updateReclamation(Reclamation reclamation) {
        try {
            updateStmt.setInt(1, reclamation.getIdUser());
            updateStmt.setInt(2, reclamation.getIdVoyage());
            updateStmt.setString(3, reclamation.getMessage());
            updateStmt.setInt(4, reclamation.getId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating reclamation: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteReclamation(int id) {
        try {
            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting reclamation: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Reclamation> getAllReclamations() {
        ArrayList<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reclamation r = new Reclamation(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getInt("idVoyage"),
                        rs.getString("message")
                );
                reclamations.add(r);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching reclamations: " + e.getMessage());
        }

        return reclamations;
    }
}
