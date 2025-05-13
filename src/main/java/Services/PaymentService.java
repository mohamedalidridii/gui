package Services;

import DAO.PaymentDAO;
import Models.Payment;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentService {

    private PaymentDAO paymentDAO = new PaymentDAO();

    public PaymentService() {
    }

    public void makePayment(Payment payment) {
        // Validate payment data
        validatePayment(payment);

        // Set payment date to now if not provided
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        // Set initial status if not provided
        if (payment.getStatus() == null || payment.getStatus().isEmpty()) {
            payment.setStatus("COMPLETED");
        }

        paymentDAO.add(payment);
    }

    private void validatePayment(Payment payment) {
        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required");
        }

        // Validate that at least one booking type is present
        if (payment.getFlightBookingId() <= 0 && payment.getAccommodationBookingId() <= 0) {
            throw new IllegalArgumentException("At least one booking reference (flight or accommodation) is required");
        }
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAll();
    }

    public List<Payment> getPaymentsByStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }

        return paymentDAO.getAll().stream()
                .filter(payment -> status.equalsIgnoreCase(payment.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Payment> getPaymentsByDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Date range cannot be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return paymentDAO.getAll().stream()
                .filter(payment -> !payment.getPaymentDate().isBefore(startDate) &&
                        !payment.getPaymentDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Payment> getPaymentsByBookingId(int bookingId, boolean isFlightBooking) {
        if (bookingId <= 0) {
            throw new IllegalArgumentException("Booking ID must be positive");
        }

        return paymentDAO.getAll().stream()
                .filter(payment -> isFlightBooking ?
                        payment.getFlightBookingId() == bookingId :
                        payment.getAccommodationBookingId() == bookingId)
                .collect(Collectors.toList());
    }

    public double getTotalAmountByStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }

        return paymentDAO.getAll().stream()
                .filter(payment -> status.equalsIgnoreCase(payment.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public void updatePayment(Payment payment) {
        if (payment.getPaymentId() <= 0) {
            throw new IllegalArgumentException("Valid payment ID is required for updates");
        }

        validatePayment(payment);
        paymentDAO.update(payment);
    }

    public void updatePaymentStatus(int paymentId, String newStatus) {
        if (paymentId <= 0) {
            throw new IllegalArgumentException("Valid payment ID is required");
        }

        if (newStatus == null || newStatus.isEmpty()) {
            throw new IllegalArgumentException("New status cannot be empty");
        }

        List<Payment> payments = paymentDAO.getAll().stream()
                .filter(p -> p.getPaymentId() == paymentId)
                .collect(Collectors.toList());

        if (payments.isEmpty()) {
            throw new IllegalArgumentException("Payment not found with ID: " + paymentId);
        }

        Payment payment = payments.get(0);
        payment.setStatus(newStatus);
        paymentDAO.update(payment);
    }

    public void deletePayment(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Valid payment ID is required for deletion");
        }

        paymentDAO.delete(id);
    }

    public boolean refundPayment(int paymentId) {
        if (paymentId <= 0) {
            throw new IllegalArgumentException("Valid payment ID is required");
        }

        List<Payment> payments = paymentDAO.getAll().stream()
                .filter(p -> p.getPaymentId() == paymentId)
                .collect(Collectors.toList());

        if (payments.isEmpty()) {
            throw new IllegalArgumentException("Payment not found with ID: " + paymentId);
        }

        Payment payment = payments.get(0);

        // Only completed payments can be refunded
        if (!"COMPLETED".equalsIgnoreCase(payment.getStatus())) {
            return false;
        }

        payment.setStatus("REFUNDED");
        paymentDAO.update(payment);
        return true;
    }
}