package entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Event {
    private int idEvent;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    private float price;
    private int nbParticipant;
    private int maxParticipants;
    private int VuesNb;
    private int FidelityPoints;
    private boolean visa;
    private int idCreator;
    private float promotionRate;
    private float finalPrice;
    private boolean isDeleted;
    private List<Integer> listParticipantsID;
    private GenreEvent genreEvent;
    private StatusEvent statusEvent;
    private TypeEvent typeEvent;
    private Location location;

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNbParticipant() {
        return nbParticipant;
    }

    public void setNbParticipant(int nbParticipant) {
        this.nbParticipant = nbParticipant;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getVuesNb() {
        return VuesNb;
    }

    public void setVuesNb(int vuesNb) {
        VuesNb = vuesNb;
    }

    public int getFidelityPoints() {
        return FidelityPoints;
    }

    public void setFidelityPoints(int fidelityPoints) {
        FidelityPoints = fidelityPoints;
    }

    public boolean isVisa() {
        return visa;
    }

    public void setVisa(boolean visa) {
        this.visa = visa;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public float getPromotionRate() {
        return promotionRate;
    }

    public void setPromotionRate(float promotionRate) {
        this.promotionRate = promotionRate;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<Integer> getListParticipantsID() {
        return listParticipantsID;
    }

    public void setListParticipantsID(List<Integer> listParticipantsID) {
        this.listParticipantsID = listParticipantsID;
    }

    public GenreEvent getGenreEvent() {
        return genreEvent;
    }

    public void setGenreEvent(GenreEvent genreEvent) {
        this.genreEvent = genreEvent;
    }

    public StatusEvent getStatusEvent() {
        return statusEvent;
    }

    public void setStatusEvent(StatusEvent statusEvent) {
        this.statusEvent = statusEvent;
    }

    public TypeEvent getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(TypeEvent typeEvent) {
        this.typeEvent = typeEvent;
    }

    public Event(int idEvent, String name, String description, LocalDate startDate, LocalDate endDate, int duration, float price, int nbParticipant, int maxParticipants, int vuesNb, int fidelityPoints, boolean visa, int idCreator, float promotionRate, float finalPrice, boolean isDeleted, List<Integer> listParticipantsID, GenreEvent genreEvent, StatusEvent statusEvent, TypeEvent typeEvent, Location location) {
        this.idEvent = idEvent;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.price = price;
        this.nbParticipant = nbParticipant;
        this.maxParticipants = maxParticipants;
        VuesNb = vuesNb;
        FidelityPoints = fidelityPoints;
        this.visa = visa;
        this.idCreator = idCreator;
        this.promotionRate = promotionRate;
        this.finalPrice = finalPrice;
        this.isDeleted = isDeleted;
        this.listParticipantsID = listParticipantsID;
        this.genreEvent = genreEvent;
        this.statusEvent = statusEvent;
        this.typeEvent = typeEvent;
        this.location = location;
    }

    public Event() {
    }
}


