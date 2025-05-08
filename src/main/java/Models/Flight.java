package Models;

import java.time.LocalDate;

public class Flight {
    private int id;
    private String airline;
    private String departure;
    private String destination;
    private LocalDate date;
    private String time;
    private double price;

    public Flight(int id, String airline, String departure, String destination,
                  LocalDate date, String time, double price) {
        this.id = id;
        this.airline = airline;
        this.departure = departure;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public Flight( String airline, String departure, String destination,
                  LocalDate date, String time, double price) {
        this.airline = airline;
        this.departure = departure;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }
    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", airline='" + airline + '\'' +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", price=" + price +
                '}';
    }


}
