package Repositories;

import entities.*;
import utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LocationRepository {

    private final Connection connection;

    public LocationRepository() {
        connection = Database.getInstance().getConnection();
    }

    public void createLocation(Location location) {
        String sql =" INSERT INTO `location` ( `country`, `visa`, `description`, `images`, `trasportation`, `weather`) VALUES ( ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, location.getCountry());
            ps.setBoolean(2, location.isVisa());
            ps.setString(3, location.getDescription());
            ps.setString(4, location.getImages());
            ps.setString(5, location.getTransportaion().stream().collect(Collectors.joining(",")));
            ps.setString(6, location.getWeather().stream().collect(Collectors.joining(",")));

            ps.executeUpdate();
            System.out.println("Event added successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void deleteLocation(Location location) {
        String sql =" DELETE FROM `location` WHERE `idLocation` = ?";
        try(PreparedStatement ps=connection.prepareStatement(sql)) {
            ps.setInt(1,location.getIdLocation());
            System.out.println("Location deleted successfully");

        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void updateLocation(Location location) {
        String sql = "UPDATE location SET country=?, visa=?,description=?,images=?,trasportation=?,weather=?";
        try (PreparedStatement ps= connection.prepareStatement(sql)){
            ps.setString(1, location.getCountry());
            ps.setString(2, location.getDescription());
            ps.setBoolean(3, location.isVisa());
            ps.setString(4, location.getImages());
            ps.setString(5, location.getTransportaion().stream().collect(Collectors.joining(",")));
            ps.setString(6, location.getWeather().stream().collect(Collectors.joining(",")));

            ps.executeUpdate();
            System.out.println("Event added successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


        }
    private Location mapResultSetToLocation(ResultSet rs) throws SQLException {
        Location location = new Location();
        location.setIdLocation(rs.getInt("idLocation"));
        location.setCountry(rs.getString("country"));
        location.setVisa(rs.getBoolean("visa"));
        location.setDescription(rs.getString("description"));
        location.setImages(rs.getString("images"));
        // Note: transportaion and weather lists are not handled here
        return location;
    }

    public List<Location> getAllLocations() {
        String sql = "SELECT * FROM location";
        List<Location> locations = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                locations.add(mapResultSetToLocation(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return locations;
    }

    public Location getLocationByCountry(String country) {
        String sql = "SELECT * FROM location WHERE country = ?";
        Location location = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, country);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                location = mapResultSetToLocation(rs);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return location;
    }

    public Location getLocationById(int id) {
        String sql = "SELECT * FROM location WHERE idLocation = ?";
        Location location = null;
        try (PreparedStatement ps=connection.prepareStatement(sql)){
           ps.setInt(1,id);
           ResultSet rs = ps.executeQuery();
           if (rs.next()){
               location = mapResultSetToLocation(rs);

           }
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return location;
    }
}
