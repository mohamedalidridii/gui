package Models;

import java.time.LocalDate;

public class AccommodationBooking {
    private int accommodationBookingId;
    private int accommodationId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numGuests;

    public AccommodationBooking(int id, int accommodationId, LocalDate checkIn, LocalDate checkOut, int numGuests) {
        this.accommodationBookingId = id; // Fixed: was using parameter name instead of field name
        this.accommodationId = accommodationId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numGuests = numGuests;
    }
    public AccommodationBooking( int accommodationId, LocalDate checkIn, LocalDate checkOut, int numGuests) {

        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numGuests = numGuests;
    }

    public AccommodationBooking() {

    }


    // Getters and Setters
    public int getAccommodationBookingId() { return accommodationBookingId; }
    public void setAccommodationBookingId(int accommodationBookingId) { this.accommodationBookingId = accommodationBookingId; }

    public int getAccommodationId() { return accommodationId; }
    public void setAccommodationId(int accommodationId) { this.accommodationId = accommodationId; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    public int getNumGuests() { return numGuests; }
    public void setNumGuests(int numGuests) { this.numGuests = numGuests; }
}
