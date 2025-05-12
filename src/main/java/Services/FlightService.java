package Services;

import DAO.FlightDAO;
import Models.Flight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightService {
    private static FlightDAO flightDAO;

    public FlightService() {
        FlightService.flightDAO = flightDAO;
    }

    /**
     * Get all flights
     */
    public static List<Flight> getAllFlights() {
        return flightDAO.getAll();
    }

    /**
     * Get flight by ID
     */
    public static Optional<Flight> getFlight(int id) {
        return flightDAO.getById(id);
    }

    /**
     * Get flight by ID (string version)
     */
    public Optional<Flight> getFlight(String id) {
        try {
            return flightDAO.getById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid flight ID format", e);
        }
    }

    /**
     * Add a new flight
     */
    public boolean addFlight(Flight flight) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        validateFlight(flight);
        return flightDAO.add(flight);
    }

    /**
     * Update an existing flight
     */
    public boolean updateFlight(Flight flight) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        validateFlight(flight);
        return flightDAO.update(flight);
    }

    /**
     * Delete a flight by ID
     */
    public boolean deleteFlight(int id) {
        return flightDAO.delete(id);
    }

    /**
     * Delete a flight by ID (string version)
     */
    public boolean deleteFlight(String id) {
        try {
            return flightDAO.delete(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid flight ID format", e);
        }
    }

    /**
     * Search flights by departure location
     */
    public static List<Flight> searchByDeparture(String departure) {
        if (departure == null || departure.trim().isEmpty()) {
            throw new IllegalArgumentException("Departure location cannot be null or empty");
        }

        return flightDAO.getAll().stream()
                .filter(flight -> flight.getDeparture().equalsIgnoreCase(departure.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Search flights by destination
     */
    public static List<Flight> searchByDestination(String destination) {
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }

        return flightDAO.getAll().stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Search flights by airline
     */
    public static List<Flight> searchByAirline(String airline) {
        if (airline == null || airline.trim().isEmpty()) {
            throw new IllegalArgumentException("Airline name cannot be null or empty");
        }

        return flightDAO.getAll().stream()
                .filter(flight -> flight.getAirline().equalsIgnoreCase(airline.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Search flights by date
     */
    public List<Flight> searchByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        return flightDAO.getAll().stream()
                .filter(flight -> flight.getDate().isEqual(date))
                .collect(Collectors.toList());
    }

    /**
     * Search flights by departure and destination
     */
    public static List<Flight> searchByRoute(String departure, String destination) {
        if (departure == null || departure.trim().isEmpty()) {
            throw new IllegalArgumentException("Departure location cannot be null or empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }

        return flightDAO.getAll().stream()
                .filter(flight -> flight.getDeparture().equalsIgnoreCase(departure.trim()) &&
                        flight.getDestination().equalsIgnoreCase(destination.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Search flights by date range
     */
    public static List<Flight> searchByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return flightDAO.getAll().stream()
                .filter(flight ->
                        (flight.getDate().isEqual(startDate) || flight.getDate().isAfter(startDate)) &&
                                (flight.getDate().isEqual(endDate) || flight.getDate().isBefore(endDate)))
                .collect(Collectors.toList());
    }

    /**
     * Sort flights by price (ascending)
     */
    public List<Flight> sortByPrice(List<Flight> flights) {
        return flights.stream()
                .sorted(Comparator.comparingDouble(Flight::getPrice))
                .collect(Collectors.toList());
    }

    /**
     * Sort flights by date and time
     */
    public List<Flight> sortByDateTime(List<Flight> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(Flight::getDate)
                        .thenComparing(flight -> flight.getTime().toLocalTime()))
                .collect(Collectors.toList());
    }

    /**
     * Sort flights by airline
     */
    public List<Flight> sortByAirline(List<Flight> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(Flight::getAirline))
                .collect(Collectors.toList());
    }

    /**
     * Filter flights by maximum price
     */
    public List<Flight> filterByMaxPrice(List<Flight> flights, double maxPrice) {
        if (maxPrice <= 0) {
            throw new IllegalArgumentException("Maximum price must be positive");
        }

        return flights.stream()
                .filter(flight -> flight.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Check if a flight is available on a specific date
     */
    public boolean isFlightAvailable(String departure, String destination, LocalDate date) {
        if (departure == null || departure.trim().isEmpty()) {
            throw new IllegalArgumentException("Departure location cannot be null or empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }

        return !searchByRoute(departure, destination).stream()
                .filter(flight -> flight.getDate().isEqual(date))
                .collect(Collectors.toList())
                .isEmpty();
    }

    /**
     * Get flights departing after a specific time on a given date
     */
    public List<Flight> getFlightsAfterTime(LocalDate date, LocalTime time) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }

        return flightDAO.getAll().stream()
                .filter(flight -> flight.getDate().isEqual(date) &&
                        flight.getTime().toLocalTime().isAfter(time))
                .collect(Collectors.toList());
    }

    /**
     * Validate flight properties
     */
    private void validateFlight(Flight flight) {
        if (flight.getAirline() == null || flight.getAirline().trim().isEmpty()) {
            throw new IllegalArgumentException("Airline name cannot be null or empty");
        }
        if (flight.getDeparture() == null || flight.getDeparture().trim().isEmpty()) {
            throw new IllegalArgumentException("Departure location cannot be null or empty");
        }
        if (flight.getDestination() == null || flight.getDestination().trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        if (flight.getDeparture().equalsIgnoreCase(flight.getDestination())) {
            throw new IllegalArgumentException("Departure and destination cannot be the same");
        }
        if (flight.getDate() == null) {
            throw new IllegalArgumentException("Flight date cannot be null");
        }
        if (flight.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Flight date cannot be in the past");
        }
        if (flight.getTime() == null) {
            throw new IllegalArgumentException("Flight time cannot be null");
        }
        if (flight.getPrice() <= 0) {
            throw new IllegalArgumentException("Flight price must be positive");
        }
    }
}