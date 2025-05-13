package Services;

import DAO.AccommodationBookingDAO;
import Models.AccommodationBooking;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccommodationBookingService {
    private final AccommodationBookingDAO bookingDAO;

    public AccommodationBookingService() throws SQLException {
        this.bookingDAO = new AccommodationBookingDAO();
    }

    /**
     * Create a new accommodation booking
     * @param booking The booking to create
     * @return The created booking with its ID
     */
    public Optional<AccommodationBooking> createBooking(AccommodationBooking booking) {
        // Validate booking data
        if (booking.getCheckIn() == null || booking.getCheckOut() == null) {
            return Optional.empty();
        }

        if (booking.getCheckIn().isAfter(booking.getCheckOut())) {
            return Optional.empty();
        }

        if (booking.getNumGuests() <= 0) {
            return Optional.empty();
        }

        // Add booking to database
        boolean success = bookingDAO.add(booking);

        if (success) {
            // Get the latest booking for this accommodation to return
            List<AccommodationBooking> bookings = bookingDAO.getAll();
            return bookings.stream()
                    .filter(b -> b.getAccommodationId() == booking.getAccommodationId())
                    .max((b1, b2) -> Integer.compare(b1.getAccommodationBookingId(), b2.getAccommodationBookingId()));
        }

        return Optional.empty();
    }

    /**
     * Get a booking by ID
     */
    public Optional<AccommodationBooking> getBookingById(int id) {
        return bookingDAO.getById(id);
    }

    /**
     * Get all bookings
     */
    public List<AccommodationBooking> getAllBookings() {
        return bookingDAO.getAll();
    }

    /**
     * Update an existing booking
     */
    public boolean updateBooking(AccommodationBooking booking) {
        return bookingDAO.update(booking);
    }

    /**
     * Delete a booking
     */
    public boolean deleteBooking(int id) {
        return bookingDAO.delete(id);
    }
}