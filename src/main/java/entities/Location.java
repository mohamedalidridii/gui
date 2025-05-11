package entities;

import java.util.List;
import java.util.Objects;

public class Location {
    private int idLocation;
    private String country;
    private String description;
    private boolean visa;
    private String image;
    private String images;
    private List<String> transportaion;
    private List<String> weather;

    public Location() {
    }

    public Location(int idLocation, String country, String description, boolean visa, String image) {
        this.idLocation = idLocation;
        this.country = country;
        this.description = description;
        this.visa = visa;
        this.image = image;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVisa() {
        return visa;
    }

    public void setVisa(boolean visa) {
        this.visa = visa;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
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

    @Override
    public String toString() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return getIdLocation() == location.getIdLocation() && isVisa() == location.isVisa() && Objects.equals(getCountry(), location.getCountry()) && Objects.equals(getDescription(), location.getDescription()) && Objects.equals(getImages(), location.getImages()) && Objects.equals(getTransportaion(), location.getTransportaion()) && Objects.equals(getWeather(), location.getWeather());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdLocation(), getCountry(), isVisa(), getDescription(), getImages(), getTransportaion(), getWeather());
    }
}
