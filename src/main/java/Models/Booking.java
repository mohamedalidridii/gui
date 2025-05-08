package Models;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int userId;
    private int flightId;
    private int accommodationId;
    private LocalDate bookingDate;

    public Booking(int id, int userId, int flightId, int accommodationId, LocalDate bookingDate) {
        this.id = id;
        this.userId = userId;
        this.flightId = flightId;
        this.accommodationId = accommodationId;
        this.bookingDate = bookingDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }
    public int getAccommodationId() { return accommodationId; }
    public void setAccommodationId(int accommodationId) { this.accommodationId = accommodationId; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
}

