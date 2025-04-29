package entities;

import java.util.List;

public class Location {
    private int idLocation;
    private String country;
    private boolean visa;
    private String description;
    private List<String> images;
    private List<String> transportaion;
    private List<String> weather;

    public int getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(int idLocation) {
        this.idLocation = idLocation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isVisa() {
        return visa;
    }

    public void setVisa(boolean visa) {
        this.visa = visa;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getTransportaion() {
        return transportaion;
    }

    public void setTransportaion(List<String> transportaion) {
        this.transportaion = transportaion;
    }

    public List<String> getWeather() {
        return weather;
    }

    public void setWeather(List<String> weather) {
        this.weather = weather;
    }
}
