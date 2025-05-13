package Services;

import DAO.AccommodationDAO;
import Models.Accommodation;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccommodationService {
    private AccommodationDAO accommodationDAO = new AccommodationDAO();

    public AccommodationService() {
        this.accommodationDAO = accommodationDAO;
    }

    /**
     * Search accommodations by location and optional type.
     */
    public List<Accommodation> search(String location, String type) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location is required.");
        }

        List<Accommodation> all = accommodationDAO.getAll();

        return all.stream()
                .filter(acc -> acc.getLocation().equalsIgnoreCase(location))
                .filter(acc -> type == null || type.isEmpty() || acc.getType().equalsIgnoreCase(type))
                .filter(acc -> acc.getAvailableRooms() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Sort accommodations by price per night.
     */
    public List<Accommodation> sortByPrice(List<Accommodation> accommodations) {
        return accommodations.stream()
                .sorted(Comparator.comparingDouble(Accommodation::getPricePerNight))
                .collect(Collectors.toList());
    }

    /**
     * Check if the accommodation has at least one room available.
     */
    public boolean isAvailable(int accommodationId) {
        Optional<Accommodation> acc = accommodationDAO.getById(accommodationId);
        return acc.isPresent() && acc.get().getAvailableRooms() > 0;
    }

    /**
     * Get all accommodations
     */
    public List<Accommodation> getAllAccommodations() {
        return accommodationDAO.getAll();
    }

    /**
     * Add a new accommodation
     */
    public boolean addAccommodation(Accommodation accommodation) {
        Objects.requireNonNull(accommodation, "Accommodation cannot be null");
        validateAccommodation(accommodation);
        return accommodationDAO.add(accommodation);
    }

    /**
     * Update an existing accommodation
     */
    public boolean updateAccommodation(Accommodation accommodation) {
        Objects.requireNonNull(accommodation, "Accommodation cannot be null");
        validateAccommodation(accommodation);
        return accommodationDAO.update(accommodation);
    }

    /**
     * Delete an accommodation by ID
     */
    public boolean deleteAccommodation(int id) {
        return accommodationDAO.delete(id);
    }

    /**
     * Delete an accommodation by ID (string version)
     */
    public boolean deleteAccommodation(String id) {
        try {
            return accommodationDAO.delete(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid accommodation ID format", e);
        }
    }

    /**
     * Get accommodation by ID
     */
    public Optional<Accommodation> getAccommodation(int id) {
        return accommodationDAO.getById(id);
    }

    /**
     * Get accommodation by ID (string version)
     */
    public Optional<Accommodation> getAccommodation(String id) {
        try {
            return accommodationDAO.getById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid accommodation ID format", e);
        }
    }

    /**
     * Filter accommodations by price range
     */
    public List<Accommodation> filterByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range");
        }

        return accommodationDAO.getAll().stream()
                .filter(acc -> acc.getPricePerNight() >= minPrice && acc.getPricePerNight() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Validate accommodation properties
     */
    private void validateAccommodation(Accommodation accommodation) {
        if (accommodation.getName() == null || accommodation.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Accommodation name cannot be empty");
        }
        if (accommodation.getLocation() == null || accommodation.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Accommodation location cannot be empty");
        }
        if (accommodation.getType() == null || accommodation.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Accommodation type cannot be empty");
        }
        if (accommodation.getAvailableRooms() < 0) {
            throw new IllegalArgumentException("Available rooms cannot be negative");
        }
        if (accommodation.getPricePerNight() <= 0) {
            throw new IllegalArgumentException("Price per night must be positive");
        }
    }
}