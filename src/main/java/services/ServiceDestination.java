package services;

import java.sql.*;

import Entities.Destination;
import utils.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceDestination {
    protected Connection conn = DataSource.getInstance().getConnection();

    private PreparedStatement insertStmt;
    private PreparedStatement selectStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;

    public ServiceDestination() {
        try {
            insertStmt = conn.prepareStatement("INSERT INTO location(name, address) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            selectStmt = conn.prepareStatement("SELECT * FROM location WHERE id=?");
            updateStmt = conn.prepareStatement("UPDATE location SET name=?, address=? WHERE id=?");
            deleteStmt = conn.prepareStatement("DELETE FROM location WHERE id=?");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean addDestination(Destination destination) {
        try {
            insertStmt.setString(1, destination.getName());
            insertStmt.setString(2, destination.getAddress());
            if (insertStmt.executeUpdate() > 0) {
                ResultSet rs = insertStmt.getGeneratedKeys();
                if (rs.next()) {
                    destination.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Destination getDestination(int id) {
        try {
            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                Destination destination = new Destination();
                destination.setId(rs.getInt("id"));
                destination.setName(rs.getString("name"));
                destination.setAddress(rs.getString("address"));
                return destination;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean updateDestination(Destination destination) {
        try {
            updateStmt.setString(1, destination.getName());
            updateStmt.setString(2, destination.getAddress());
            updateStmt.setInt(3, destination.getId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean deleteDestination(int id) {
        try {
            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public ArrayList<Destination> getAllDestinations() {
        ArrayList<Destination> destinationList = new ArrayList<>();
        String sql = "SELECT * FROM location";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                destinationList.add(new Destination(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching destinations: " + e.getMessage());
        }

        return destinationList;
    }

    public static List<Destination> searchDestinations(List<Destination> destinations, String keyword) {
        return destinations.stream()
                .filter(destination ->
                        destination.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                destination.getAddress().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
