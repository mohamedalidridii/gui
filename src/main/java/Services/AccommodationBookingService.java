package Services;

import DAO.AccommodationBookingDAO;
import Models.AccommodationBooking;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccommodationBookingService {
    private final AccommodationBookingDAO bookingDAO;

    public AccommodationBookingService(AccommodationBookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    /**
     * Get all bookings
     */
    public List<AccommodationBooking> getAllBookings() {
        return bookingDAO.getAll();
    }

    /**
     * Get booking by ID
     */
    public Optional<AccommodationBooking> getBooking(int id) {
        return bookingDAO.getById(id);
    }

    /**
     * Get booking by ID (string version)
     */
    public Optional<AccommodationBooking> getBooking(String id) {
        try {
            return bookingDAO.getById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid booking ID format", e);
        }
    }

    /**
     * Add a new booking
     */
    public boolean addBooking(AccommodationBooking booking) {
        Objects.requireNonNull(booking, "Booking cannot be null");
        validateBooking(booking);
        return bookingDAO.add(booking);
    }

    /**
     * Update an existing booking
     */
    public boolean updateBooking(AccommodationBooking booking) {
        Objects.requireNonNull(booking, "Booking cannot be null");
        validateBooking(booking);
        return bookingDAO.update(booking);
    }

    /**
     * Delete a booking by ID
     */
    public boolean deleteBooking(int id) {
        return bookingDAO.delete(id);
    }

    /**
     * Delete a booking by ID (string version)
     */
    public boolean deleteBooking(String id) {
        try {
            return bookingDAO.delete(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid booking ID format", e);
        }
    }

    /**
     * Get bookings for a specific accommodation
     */
    public List<AccommodationBooking> getBookingsByAccommodation(int accommodationId) {
        return bookingDAO.getAll().stream()
                .filter(booking -> booking.getAccommodationId() == accommodationId)
                .collect(Collectors.toList());
    }

    /**
     * Get bookings for a date range
     */
    public List<AccommodationBooking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return bookingDAO.getAll().stream()
                .filter(booking ->
                        (booking.getCheckIn().isEqual(startDate) || booking.getCheckIn().isAfter(startDate)) &&
                                (booking.getCheckOut().isEqual(endDate) || booking.getCheckOut().isBefore(endDate))
                )
                .collect(Collectors.toList());
    }

    /**
     * Sort bookings by check-in date
     */
    public List<AccommodationBooking> sortByCheckInDate(List<AccommodationBooking> bookings) {
        return bookings.stream()
                .sorted(Comparator.comparing(AccommodationBooking::getCheckIn))
                .collect(Collectors.toList());
    }

    /**
     * Calculate the duration of a booking in days
     */
    public long calculateBookingDuration(AccommodationBooking booking) {
        Objects.requireNonNull(booking, "Booking cannot be null");
        return ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
    }

    /**
     * Check if dates are available for an accommodation
     */
    public boolean areDatesAvailable(int accommodationId, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates cannot be null");
        }
        if (checkIn.isAfter(checkOut)) {
            throw new IllegalArgumentException("Check-in date cannot be after check-out date");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        List<AccommodationBooking> existingBookings = getBookingsByAccommodation(accommodationId);

        // Check if there's any overlap with existing bookings
        return existingBookings.stream().noneMatch(booking ->
                (checkIn.isBefore(booking.getCheckOut()) || checkIn.isEqual(booking.getCheckOut())) &&
                        (checkOut.isAfter(booking.getCheckIn()) || checkOut.isEqual(booking.getCheckIn()))
        );
    }

    /**
     * Validate booking properties
     */
    private void validateBooking(AccommodationBooking booking) {
        if (booking.getAccommodationId() <= 0) {
            throw new IllegalArgumentException("Invalid accommodation ID");
        }
        if (booking.getCheckIn() == null) {
            throw new IllegalArgumentException("Check-in date cannot be null");
        }
        if (booking.getCheckOut() == null) {
            throw new IllegalArgumentException("Check-out date cannot be null");
        }
        if (booking.getCheckIn().isAfter(booking.getCheckOut())) {
            throw new IllegalArgumentException("Check-in date cannot be after check-out date");
        }
        if (booking.getCheckIn().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
        if (booking.getNumGuests() <= 0) {
            throw new IllegalArgumentException("Number of guests must be positive");
        }
    }
}