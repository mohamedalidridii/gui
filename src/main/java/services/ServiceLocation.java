package services;

import entities.Location;
import utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceLocation {
    private final Connection connection;

    public ServiceLocation() {
        connection = Database.getInstance().getConnection();
    }

    public void create(Location location) {
        String sql = "INSERT INTO location (country, description, visa, images) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, location.getCountry());
            ps.setString(2, location.getDescription());
            ps.setBoolean(3, location.isVisa());
            ps.setString(4, location.getImage());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating location: " + e.getMessage());
            throw new RuntimeException("Could not create location", e);
        }
    }

    public void update(Location location) {
        String sql = "UPDATE location SET country = ?, description = ?, visa = ?, images = ? WHERE idLocation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, location.getCountry());
            ps.setString(2, location.getDescription());
            ps.setBoolean(3, location.isVisa());
            ps.setString(4, location.getImage());
            ps.setInt(5, location.getIdLocation());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating location: " + e.getMessage());
            throw new RuntimeException("Could not update location", e);
        }
    }

    public void delete(int idLocation) {
        String sql = "DELETE FROM location WHERE idLocation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idLocation);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting location: " + e.getMessage());
            throw new RuntimeException("Could not delete location", e);
        }
    }

    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM location";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Location location = new Location();
                location.setIdLocation(rs.getInt("idLocation"));
                location.setCountry(rs.getString("country"));
                location.setDescription(rs.getString("description"));
                location.setVisa(rs.getBoolean("visa"));
                location.setImage(rs.getString("images"));
                locations.add(location);
            }
        } catch (SQLException e) {
            System.err.println("Error getting locations: " + e.getMessage());
            throw new RuntimeException("Could not get locations", e);
        }
        return locations;
    }

    public Location getLocationById(int idLocation) {
        String sql = "SELECT * FROM location WHERE idLocation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idLocation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Location location = new Location();
                location.setIdLocation(rs.getInt("idLocation"));
                location.setCountry(rs.getString("country"));
                location.setDescription(rs.getString("description"));
                location.setVisa(rs.getBoolean("visa"));
                location.setImage(rs.getString("images"));
                return location;
            }
        } catch (SQLException e) {
            System.err.println("Error getting location: " + e.getMessage());
            throw new RuntimeException("Could not get location", e);
        }
        return null;
    }
    public List<Location> dynamicSearch(String searchTerm){
        String query = "SELECT * FROM location WHERE country LIKE ? OR description LIKE ?";
        List<Location> maliste = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Location location = new Location();
                location.setIdLocation(rs.getInt("idLocation"));
                location.setCountry(rs.getString("country"));
                location.setDescription(rs.getString("description"));
                location.setVisa(rs.getBoolean("visa"));
                location.setImage(rs.getString("images"));
                maliste.add(location);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return maliste;
    }
} 