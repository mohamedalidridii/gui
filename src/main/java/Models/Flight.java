package Models;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Flight {
    private int flightId;
    private String airline;
    private String departure;
    private String destination;
    private LocalDate date;
    private LocalTime time;
    private double price;

    // Constructor with ID
    public Flight(int flightId, String airline, String departure, String destination,
                  LocalDate date, LocalTime time, double price) {
        this.flightId = flightId;
        this.airline = airline;
        this.departure = departure;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    // Constructor without ID (for when inserting a new flight)
    public Flight(String airline, String departure, String destination,
                  LocalDate date, LocalTime time, double price) {
        this.airline = airline;
        this.departure = departure;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    // Getters and Setters
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Time getTime() {
        return Time.valueOf(time);
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public Time getSqlTime() {
        return Time.valueOf(time); // Converts LocalTime to java.sql.Time
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightId=" + flightId +
                ", airline='" + airline + '\'' +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", date=" + date +
                ", time=" + time +  // Displays LocalTime
                ", price=" + price +
                '}';
    }
}
