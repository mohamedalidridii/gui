package services;

import entities.Avis;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;

public class ServiceAvis {

    protected Connection conn = DataSource.getInstance().getConnection();

    private PreparedStatement insertStmt;
    private PreparedStatement selectStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;

    public ServiceAvis() {
        try {
            insertStmt = conn.prepareStatement("INSERT INTO avis(idUser, idVoyage, note, commentaire) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            selectStmt = conn.prepareStatement("SELECT * FROM avis WHERE id=?");
            updateStmt = conn.prepareStatement("UPDATE avis SET idUser=?, idVoyage=?, note=?, commentaire=? WHERE id=?");
            deleteStmt = conn.prepareStatement("DELETE FROM avis WHERE id=?");
        } catch (SQLException e) {
            System.out.println("Error preparing statements: " + e.getMessage());
        }
    }

    public boolean addAvis(Avis avis) {
        try {
            insertStmt.setInt(1, avis.getIdUser());
            insertStmt.setInt(2, avis.getIdVoyage());
            insertStmt.setInt(3, avis.getNote());
            insertStmt.setString(4, avis.getCommentaire());
            if (insertStmt.executeUpdate() > 0) {
                ResultSet rs = insertStmt.getGeneratedKeys();
                if (rs.next()) {
                    avis.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding avis: " + e.getMessage());
        }
        return false;
    }

    public Avis getAvis(int id) {
        try {
            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                return new Avis(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getInt("idVoyage"),
                        rs.getInt("note"),
                        rs.getString("commentaire")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching avis: " + e.getMessage());
        }
        return null;
    }

    public boolean updateAvis(Avis avis) {
        try {
            updateStmt.setInt(1, avis.getIdUser());
            updateStmt.setInt(2, avis.getIdVoyage());
            updateStmt.setInt(3, avis.getNote());
            updateStmt.setString(4, avis.getCommentaire());
            updateStmt.setInt(5, avis.getId());

            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating avis: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteAvis(int id) {
        try {
            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting avis: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Avis> getAllAvis() {
        ArrayList<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM avis";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Avis avis = new Avis(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getInt("idVoyage"),
                        rs.getInt("note"),
                        rs.getString("commentaire")
                );
                avisList.add(avis);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all avis: " + e.getMessage());
        }

        return avisList;
    }
}
