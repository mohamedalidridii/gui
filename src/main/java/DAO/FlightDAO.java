package DAO;

import Models.Flight;
import Util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDAO implements IGenericDAO<Flight> {
    @Override
    public boolean add(Flight flight) {
        String sql = "INSERT INTO flights (airline, departure, destination, flight_date, flight_time, price) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, flight.getAirline());
            stmt.setString(2, flight.getDeparture());
            stmt.setString(3, flight.getDestination());
            stmt.setDate(4, Date.valueOf(flight.getDate()));
            stmt.setTime(5, flight.getTime());
            stmt.setDouble(6, flight.getPrice());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Flight> getAll() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("airline"),
                        rs.getString("departure"),
                        rs.getString("destination"),
                        rs.getDate("flight_date").toLocalDate(),
                        rs.getTime("flight_time").toLocalTime(),
                        rs.getDouble("price")
                );
                flights.add(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public Optional<Flight> getById(int flightID) {
        String sql = "SELECT * FROM flights WHERE flight_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, flightID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("airline"),
                        rs.getString("departure"),
                        rs.getString("destination"),
                        rs.getDate("flight_date").toLocalDate(),
                        rs.getTime("flight_time").toLocalTime(),
                        rs.getDouble("price")
                );
                return Optional.of(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Flight flight) {
        String sql = "UPDATE flights SET airline = ?, departure = ?, destination = ?, flight_date = ?, flight_time = ?, price = ? WHERE flight_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, flight.getAirline());
            stmt.setString(2, flight.getDeparture());
            stmt.setString(3, flight.getDestination());
            stmt.setDate(4, Date.valueOf(flight.getDate()));
            stmt.setTime(5, flight.getTime());
            stmt.setDouble(6, flight.getPrice());
            stmt.setInt(7, flight.getFlightId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int flightId) {
        String sql = "DELETE FROM flights WHERE flight_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, flightId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}