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
            int bookingId = Integer.parseInt(id);
            return getBooking(bookingId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Add a new flight booking
     */
    public boolean addBooking(FlightBooking booking) {
        try {
            validateBooking(booking);
            return bookingDAO.add(booking);
        } catch (IllegalArgumentException e) {
            System.err.println("Validation failed: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error saving booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update an existing flight booking
     */
    public boolean updateBooking(FlightBooking booking) {
        try {
            validateBooking(booking);
            return bookingDAO.update(booking);
        } catch (IllegalArgumentException e) {
            System.err.println("Validation failed: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error updating booking: " + e.getMessage());
            return false;
        }
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
            int bookingId = Integer.parseInt(id);
            return deleteBooking(bookingId);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Get bookings for a specific flight
     */
    public List<FlightBooking> getBookingsByFlight(int flightId) {
        return getAllBookings().stream()
                .filter(booking -> booking.getFlightId() == flightId)
                .collect(Collectors.toList());
    }

    /**
     * Get bookings for a specific date
     */
    public List<FlightBooking> getBookingsByDate(LocalDate date) {
        return getAllBookings().stream()
                .filter(booking -> booking.getBookingDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Get bookings for a date range
     */
    public List<FlightBooking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return getAllBookings().stream()
                .filter(booking -> {
                    LocalDate bookingDate = booking.getBookingDate();
                    return !bookingDate.isBefore(startDate) && !bookingDate.isAfter(endDate);
                })
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
        int currentPassengers = getTotalPassengers(flightId);
        return currentPassengers + requestedSeats <= totalCapacity;
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
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        if (booking.getFlightId() <= 0) {
            throw new IllegalArgumentException("Invalid flight ID");
        }

        if (booking.getBookingDate() == null) {
            throw new IllegalArgumentException("Booking date cannot be null");
        }

        if (booking.getNumPassengers() <= 0) {
            throw new IllegalArgumentException("Number of passengers must be greater than zero");
        }
    }
}