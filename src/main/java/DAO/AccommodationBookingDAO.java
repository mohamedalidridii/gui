package DAO;

import Models.AccommodationBooking;
import Util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccommodationBookingDAO implements IGenericDAO<AccommodationBooking> {
    private final Connection conn = DatabaseUtil.getConnection();

    public AccommodationBookingDAO() throws SQLException {
    }

    @Override
    public Optional<AccommodationBooking> getById(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM accommodation_bookings WHERE accommodationBookingId = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new AccommodationBooking(
                        rs.getInt("accommodationBookingId"),
                        rs.getInt("accommodation_id"),
                        rs.getDate("checkIn").toLocalDate(),
                        rs.getDate("checkOut").toLocalDate(),
                        rs.getInt("numGuests")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<AccommodationBooking> getAll() {
        List<AccommodationBooking> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM accommodation_bookings");
            while (rs.next()) {
                list.add(new AccommodationBooking(
                        rs.getInt("accommodationBookingId"),
                        rs.getInt("accommodationId"),
                        rs.getDate("checkIn").toLocalDate(),
                        rs.getDate("checkOut").toLocalDate(),
                        rs.getInt("numGuests")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean add(AccommodationBooking booking) {
        String sql = "INSERT INTO accommodation_bookings (accommodationId, checkIn, checkOut, numGuests) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getAccommodationId());
            stmt.setDate(2, Date.valueOf(booking.getCheckIn()));
            stmt.setDate(3, Date.valueOf(booking.getCheckOut()));
            stmt.setInt(4, booking.getNumGuests());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(AccommodationBooking booking) {
        String sql = "UPDATE accommodation_bookings SET accommodationId=?, checkIn=?, checkOut=?, numGuests=? WHERE accommodationBookingId=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getAccommodationId());
            stmt.setDate(2, Date.valueOf(booking.getCheckIn()));
            stmt.setDate(3, Date.valueOf(booking.getCheckOut()));
            stmt.setInt(4, booking.getNumGuests());
            stmt.setInt(5, booking.getAccommodationBookingId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM accommodation_bookings WHERE accommodationBookingId = ?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String toString(int accommodationBookingId) {
        return "";
    }
}