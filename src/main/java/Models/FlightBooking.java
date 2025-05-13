package Models;

import java.time.LocalDate;

public class FlightBooking {

        private int flightBookingId;
        private int flightId;
        private LocalDate bookingDate;
        private int numPassengers;

        public FlightBooking(int flightBookingId, int flightId, LocalDate bookingDate, int numPassengers) {
            this.flightBookingId = flightBookingId;
            this.flightId = flightId;
            this.bookingDate = bookingDate;
            this.numPassengers = numPassengers;
        }
    public FlightBooking( LocalDate bookingDate, int numPassengers) {

        this.bookingDate = bookingDate;
        this.numPassengers = numPassengers;
    }

    public FlightBooking() {

    }


    public int getId() { return flightBookingId; }
        public void setId(int id) { this.flightId = id; }

        public int getFlightId() { return flightId; }
        public void setFlightId(int flightId) { this.flightId = flightId; }

        public LocalDate getBookingDate() { return bookingDate; }
        public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

        public int getNumPassengers() { return numPassengers; }
        public void setNumPassengers(int numPassengers) { this.numPassengers = numPassengers; }
}
