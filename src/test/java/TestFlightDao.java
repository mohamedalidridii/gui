
/*
import DAO.FlightDAO;
import Models.Flight;
import Util.DatabaseUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TestFlightDao {
    public static void main(String[] args) {
        // Ensure the DB connection is available
        try (var conn = DatabaseUtil.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Database connected.");

                // Instantiate DAO
                FlightDAO flightDAO = new FlightDAO();

                // Create a test flight
                Flight newFlight = new Flight("Air France", "Paris", "Rome", LocalDate.of(2025, 6, 15), "10:00", 120.0);

                // Add flight
                boolean added = flightDAO.add(newFlight);
                System.out.println("Add flight: " + (added ? "✅ Success" : "❌ Failed"));

                // Retrieve all
                List<Flight> flights = flightDAO.getAll();
                System.out.println("All flights:");
                flights.forEach(f -> System.out.println(" - " + f.getAirline() + " from " + f.getDeparture() + " to " + f.getDestination()));

                // Get by ID (assuming 1 exists)
                List<Flight> flight = flightDAO.getAll();
                System.out.println(flight);

                // Delete flight (if you want to clean up)
                boolean deleted = flightDAO.delete(12); // replace 1 with real ID
                System.out.println("Delete flight ID 2: " + (deleted ? "✅ Deleted" : "❌ Not found"));

            } else {
                System.out.println("❌ Database not connected.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/