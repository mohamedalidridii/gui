package DAO;

import Models.Payment;
import Util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public void add(Payment payment) {
        String sql = "INSERT INTO payments (amount, status, method, flight_booking_id, accommodation_booking_id, paymentDate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getStatus());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setInt(4, payment.getFlightBookingId());
            stmt.setInt(5, payment.getAccommodationBookingId());
            stmt.setDate(6, Date.valueOf(payment.getPaymentDate()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Payment> getAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("paymentId"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setStatus(rs.getString("status"));
                payment.setPaymentMethod(rs.getString("paymentMethod"));
                payment.setFlightBookingId(rs.getInt("flight_booking_id"));
                payment.setAccommodationBookingId(rs.getInt("accommodation_booking_id"));
                payment.setPaymentDate(rs.getDate("paymentDate").toLocalDate());

                payments.add(payment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    public void delete(int id) {
        String sql = "DELETE FROM payments WHERE paymentId = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Payment payment) {
        String sql = "UPDATE payments SET amount=?, status=?, paymentMethod=?, flight_booking_id=?, " +
                "accommodation_booking_id=?, paymentDate=? WHERE paymentId=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getStatus());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setInt(4, payment.getFlightBookingId());
            stmt.setInt(5, payment.getAccommodationBookingId());
            stmt.setDate(6, Date.valueOf(payment.getPaymentDate()));
            stmt.setInt(7, payment.getPaymentId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}