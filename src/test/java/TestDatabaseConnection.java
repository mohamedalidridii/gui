import DAO.FlightDAO;
import Models.Flight;
import Services.FlightService;
import Util.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn != null) {
                System.out.println("Connection to MySQL database was successful!");

                // Initialize DAO and Service
                FlightDAO flightDAO = new FlightDAO(); // assumes your FlightDAO has a constructor accepting Connection
                FlightService flightService = new FlightService();

                // Create and add a new flight
                Flight sampleFlight = new Flight(
                        "Air France",
                        "Paris",
                        "Rome",
                        LocalDate.of(2025, 6, 15),
                        LocalTime.parse("12:00"),
                        150.0
                );
                flightService.addFlight(sampleFlight);

                // Get all flights
                List<Flight> allFlights = flightService.getAllFlights();
                System.out.println("\nAll Flights:");
                allFlights.forEach(System.out::println);

                // Search for flights
                List<Flight> searchedFlights = flightService.searchFlights(
                        "Paris", "Rome",
                        LocalDate.of(2025, 6, 1),
                        LocalDate.of(2025, 6, 30),
                        null
                );
                System.out.println("\nSearched Flights:");
                searchedFlights.forEach(System.out::println);

                // Sort by price
                List<Flight> sortedByPrice = flightService.sortByPrice(searchedFlights);
                System.out.println("\nFlights Sorted by Price:");
                sortedByPrice.forEach(System.out::println);

                // Sort by date
                List<Flight> sortedByDate = flightService.sortByDate(searchedFlights);
                System.out.println("\nFlights Sorted by Date:");
                sortedByDate.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL database.");
            e.printStackTrace();
        }
    }
}
/*import Util.DatabaseUtil;


import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn != null) {
                System.out.println(" Connection to MySQL database was successful!");
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL database.");
            e.printStackTrace();
        }
    }
}
*/