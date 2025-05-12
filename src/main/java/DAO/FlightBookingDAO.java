package DAO;

import Models.FlightBooking;
import Util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightBookingDAO implements IFlightBookingDAO {
    private final Connection conn = DatabaseUtil.getConnection();

    public FlightBookingDAO() throws SQLException {
    }

    @Override
    public Optional<FlightBooking> getById(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM flight_bookings WHERE flight_booking_id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new FlightBooking(
                            rs.getInt("flight_booking_id"),
                            rs.getInt("flight_id"),
                            rs.getDate("booking_date").toLocalDate(),
                            rs.getInt("num_passengers")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FlightBooking> getAll() {
        List<FlightBooking> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM flight_bookings")) {
                while (rs.next()) {
                    list.add(new FlightBooking(
                            rs.getInt("flight_booking_id"),
                            rs.getInt("flight_id"),
                            rs.getDate("booking_date").toLocalDate(),
                            rs.getInt("num_passengers")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean add(FlightBooking booking) {
        String sql = "INSERT INTO flight_bookings (flight_id, booking_date, num_passengers) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getFlightId());
            stmt.setDate(2, Date.valueOf(booking.getBookingDate()));
            stmt.setInt(3, booking.getNumPassengers());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(FlightBooking booking) {
        String sql = "UPDATE flight_bookings SET flight_id=?, booking_date=?, num_passengers=? WHERE flight_booking_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getFlightId());
            stmt.setDate(2, Date.valueOf(booking.getBookingDate()));
            stmt.setInt(3, booking.getNumPassengers());
            stmt.setInt(4, booking.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM flight_bookings WHERE flight_booking_id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}