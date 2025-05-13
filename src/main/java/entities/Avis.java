package entities;

public class Avis {
    private int id;
    private int idUser;
    private int idVoyage;
    private int note;
    private String commentaire;
    public Avis(int id, int idUser, int idVoyage, int note, String commentaire) {
        this.id = id;
        this.idUser = idUser;
        this.idVoyage = idVoyage;
        this.note = note;
        this.commentaire = commentaire;
    }
    public Avis(int idUser, int idVoyage, int note, String commentaire) {
        this.idUser = idUser;
        this.idVoyage = idVoyage;
        this.note = note;
        this.commentaire = commentaire;
    }
    public Avis() {
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
    public int getNote() {
        return note;
    }
    public String getCommentaire() {
        return commentaire;
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
    public void setNote(int note) {
        this.note = note;
    }
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idVoyage=" + idVoyage +
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }
}
