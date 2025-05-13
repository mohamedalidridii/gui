package Models;

public class Accommodation {
    private int accommodationId;
    private String name;
    private String location;
    private String type;
    private int availableRooms;
    private double pricePerNight;

    // Constructors
    public Accommodation() {}

    public Accommodation(int id, String name, String location, String type, int availableRooms, double pricePerNight) {
        this.accommodationId = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.availableRooms = availableRooms;
        this.pricePerNight = pricePerNight;
    }


    public Accommodation(String name, String location, String type, int availableRooms, double pricePerNight) {
        this(0, name, location, type, availableRooms, pricePerNight);
    }

    // Getters and Setters
    public int getAccommodationId() { return accommodationId; }

    public void setAccommodationId(int id) { this.accommodationId = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public int getAvailableRooms() { return availableRooms; }

    public void setAvailableRooms(int availableRooms) { this.availableRooms = availableRooms; }

    public double getPricePerNight() { return pricePerNight; }

    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
}

