package Services;

import DAO.FlightBookingDAO;
import Models.FlightBooking;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightBookingService {
    private final FlightBookingDAO bookingDAO;

    public FlightBookingService(FlightBookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    /**
     * Get all flight bookings
     */
    public List<FlightBooking> getAllBookings() {
        return bookingDAO.getAll();
    }

    /**
     * Get flight booking by ID
     */
    public Optional<FlightBooking> getBooking(int id) {
        return bookingDAO.getById(id);
    }

    /**
     * Get flight booking by ID (string version)
     */
    public Optional<FlightBooking> getBooking(String id) {
        try {
            return bookingDAO.getById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid booking ID format", e);
        }
    }

    /**
     * Add a new flight booking
     */
    public boolean addBooking(FlightBooking booking) {
        Objects.requireNonNull(booking, "Flight booking cannot be null");
        validateBooking(booking);
        return bookingDAO.add(booking);
    }

    /**
     * Update an existing flight booking
     */
    public boolean updateBooking(FlightBooking booking) {
        Objects.requireNonNull(booking, "Flight booking cannot be null");
        validateBooking(booking);
        return bookingDAO.update(booking);
    }

    /**
     * Delete a flight booking by ID
     */
    public boolean deleteBooking(int id) {
        return bookingDAO.delete(id);
    }

    /**
     * Delete a flight booking by ID (string version)
     */
    public boolean deleteBooking(String id) {
        try {
            return bookingDAO.delete(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid booking ID format", e);
        }
    }

    /**
     * Get bookings for a specific flight
     */
    public List<FlightBooking> getBookingsByFlight(int flightId) {
        return bookingDAO.getAll().stream()
                .filter(booking -> booking.getFlightId() == flightId)
                .collect(Collectors.toList());
    }

    /**
     * Get bookings for a specific date
     */
    public List<FlightBooking> getBookingsByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Booking date cannot be null");
        }

        return bookingDAO.getAll().stream()
                .filter(booking -> booking.getBookingDate().isEqual(date))
                .collect(Collectors.toList());
    }

    /**
     * Get bookings for a date range
     */
    public List<FlightBooking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return bookingDAO.getAll().stream()
                .filter(booking ->
                        (booking.getBookingDate().isEqual(startDate) || booking.getBookingDate().isAfter(startDate)) &&
                                (booking.getBookingDate().isEqual(endDate) || booking.getBookingDate().isBefore(endDate))
                )
                .collect(Collectors.toList());
    }

    /**
     * Sort bookings by booking date
     */
    public List<FlightBooking> sortByBookingDate(List<FlightBooking> bookings) {
        return bookings.stream()
                .sorted(Comparator.comparing(FlightBooking::getBookingDate))
                .collect(Collectors.toList());
    }

    /**
     * Check if there's capacity available for a specific flight
     */
    public boolean isCapacityAvailable(int flightId, int requestedSeats, int totalCapacity) {
        if (requestedSeats <= 0) {
            throw new IllegalArgumentException("Requested seats must be positive");
        }
        if (totalCapacity <= 0) {
            throw new IllegalArgumentException("Total capacity must be positive");
        }

        List<FlightBooking> existingBookings = getBookingsByFlight(flightId);

        int currentBookedSeats = existingBookings.stream()
                .mapToInt(FlightBooking::getNumPassengers)
                .sum();

        return (currentBookedSeats + requestedSeats) <= totalCapacity;
    }

    /**
     * Get total number of passengers for a specific flight
     */
    public int getTotalPassengers(int flightId) {
        return getBookingsByFlight(flightId).stream()
                .mapToInt(FlightBooking::getNumPassengers)
                .sum();
    }

    /**
     * Validate flight booking properties
     */
    private void validateBooking(FlightBooking booking) {
        if (booking.getFlightId() <= 0) {
            throw new IllegalArgumentException("Invalid flight ID");
        }
        if (booking.getBookingDate() == null) {
            throw new IllegalArgumentException("Booking date cannot be null");
        }
        if (booking.getNumPassengers() <= 0) {
            throw new IllegalArgumentException("Number of passengers must be positive");
        }
    }
}