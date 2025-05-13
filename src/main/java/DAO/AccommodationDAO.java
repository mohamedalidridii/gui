package DAO;

import Models.Accommodation;
import Util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccommodationDAO implements IGenericDAO<Accommodation>{

    @Override
    public boolean add(Accommodation accommodation) {
        String sql = "INSERT INTO accommodations (name, location, type, availableRooms, pricePerNight) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accommodation.getName());
            stmt.setString(2, accommodation.getLocation());
            stmt.setString(3, accommodation.getType());
            stmt.setInt(4, accommodation.getAvailableRooms());
            stmt.setDouble(5, accommodation.getPricePerNight());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Accommodation> getAll() {
        List<Accommodation> list = new ArrayList<>();
        String sql = "SELECT * FROM accommodations";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Accommodation a = new Accommodation(
                        rs.getInt("accommodationId"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getInt("availableRooms"),
                        rs.getDouble("pricePerNight")
                );
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Accommodation> getById(int accommodationId) {
        String sql = "SELECT * FROM accommodations WHERE accommodationId = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accommodationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Accommodation a = new Accommodation(
                        rs.getInt("accommodationId"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getInt("availableRooms"),
                        rs.getDouble("pricePerNight")
                );
                return Optional.of(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Accommodation accommodation) {
        String sql = "UPDATE accommodations SET name = ?, location = ?, type = ?, availableRooms = ?, pricePerNight = ? WHERE accommodationId = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accommodation.getName());
            stmt.setString(2, accommodation.getLocation());
            stmt.setString(3, accommodation.getType());
            stmt.setInt(4, accommodation.getAvailableRooms());
            stmt.setDouble(5, accommodation.getPricePerNight());
            stmt.setInt(6, accommodation.getAccommodationId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int accommodationId) {
        String sql = "DELETE FROM accommodations WHERE accommodation_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accommodationId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

