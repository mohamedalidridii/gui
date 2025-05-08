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
        String sql = "INSERT INTO accommodations (name, location, type, available_rooms, price_per_night) VALUES (?, ?, ?, ?, ?)";

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
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getInt("available_rooms"),
                        rs.getDouble("price_per_night")
                );
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Accommodation> getById(int id) {
        String sql = "SELECT * FROM accommodations WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Accommodation a = new Accommodation(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getInt("available_rooms"),
                        rs.getDouble("price_per_night")
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
        String sql = "UPDATE accommodations SET name = ?, location = ?, type = ?, available_rooms = ?, price_per_night = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accommodation.getName());
            stmt.setString(2, accommodation.getLocation());
            stmt.setString(3, accommodation.getType());
            stmt.setInt(4, accommodation.getAvailableRooms());
            stmt.setDouble(5, accommodation.getPricePerNight());
            stmt.setInt(6, accommodation.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM accommodations WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

