package Models;

import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private double amount;
    private String status;
    private String paymentMethod;
    private int flightBookingId; // Can be 0 if not applicable
    private int accommodationBookingId; // Can be 0 if not applicable
    private LocalDate paymentDate;

    public Payment() {}

public Payment(int paymentId, double amount, String status, String paymentMethod, int flightBookingId, int accommodationBookingId, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.flightBookingId = flightBookingId;
        this.accommodationBookingId = accommodationBookingId;
        this.paymentDate = paymentDate;
}

    // Getters and setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getFlightBookingId() { return flightBookingId; }
    public void setFlightBookingId(int flightBookingId) { this.flightBookingId = flightBookingId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getAccommodationBookingId() { return accommodationBookingId; }
    public void setAccommodationBookingId(int accommodationBookingId) { this.accommodationBookingId = accommodationBookingId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
