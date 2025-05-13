package entities;

public class Reclamation {
    private int id;
    private int idUser;
    private int idVoyage;
    private String message;
    public Reclamation(int id, int idUser, int idVoyage, String message) {
        this.id = id;
        this.idUser = idUser;
        this.idVoyage = idVoyage;
        this.message = message;
    }
    public Reclamation(int idUser, int idVoyage, String message) {
        this.idUser = idUser;
        this.idVoyage = idVoyage;
        this.message = message;
    }
    public Reclamation() {
    }
    public int getId() {
        return id;
    }
    public int getIdUser() {
        return idUser;
    }
    public int getIdVoyage() {
        return idVoyage;
    }
    public String getMessage() {
        return message;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    public void setIdVoyage(int idVoyage) {
        this.idVoyage = idVoyage;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idVoyage=" + idVoyage +
                ", message='" + message + '\'' +
                '}';
    }
}
