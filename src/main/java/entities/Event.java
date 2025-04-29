package entities;

import java.util.Date;
import java.util.List;

public class Event {
    private int idEvent;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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
}


