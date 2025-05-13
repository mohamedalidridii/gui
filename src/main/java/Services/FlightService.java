package Services;

import DAO.FlightDAO;
import Models.Flight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightService {
    private FlightDAO flightDAO;

    /**
     * Constructor that initializes the FlightDAO
     */
    public FlightService() {
        // Initialize flightDAO to prevent NullPointerException
        this.flightDAO = new FlightDAO();
    }

    /**
     * Get all flights from the database
     *
     * @return List of all flights
     */
    public List<Flight> getAllFlights() {
        return flightDAO.getAll();
    }

    /**
     * Search for flights based on criteria
     *
     * @param departure Departure location
     * @param destination Destination location
     * @param startDate Start date range
     * @param endDate End date range
     * @param airline Airline name
     * @return List of flights matching criteria
     */
    public List<Flight> searchFlights(String departure, String destination,
                                      LocalDate startDate, LocalDate endDate,
                                      String airline) {
        List<Flight> allFlights = getAllFlights();

        return allFlights.stream()
                .filter(flight -> departure.isEmpty() || flight.getDeparture().toLowerCase().contains(departure.toLowerCase()))
                .filter(flight -> destination.isEmpty() || flight.getDestination().toLowerCase().contains(destination.toLowerCase()))
                .filter(flight -> airline.isEmpty() || flight.getAirline().toLowerCase().contains(airline.toLowerCase()))
                .filter(flight -> startDate == null || !flight.getDate().isBefore(startDate))
                .filter(flight -> endDate == null || !flight.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Get a flight by its ID
     *
     * @param flightId The ID of the flight to find
     * @return The flight with the given ID, or null if not found
     */
    public Optional<Flight> getFlightById(String flightId) {
        return flightDAO.getById(Integer.parseInt(flightId));
    }

    /**
     * Save a new flight to the database
     *
     * @param flight The flight to save
     * @return true if successful, false otherwise
     */
    public boolean saveFlight(Flight flight) {
        return flightDAO.add(flight);
    }

    /**
     * Update an existing flight
     *
     * @param flight The flight to update
     * @return true if successful, false otherwise
     */
    public boolean updateFlight(Flight flight) {
        return flightDAO.update(flight);
    }

    /**
     * Delete a flight by its ID
     *
     * @param flightId The ID of the flight to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteFlight(String flightId) {
        return flightDAO.delete(Integer.parseInt(flightId));
    }
}