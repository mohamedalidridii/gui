package entities;
import java.sql.Date;

public class Product {
    private int id;
    private String nom;
    private String image;
    private Date cTime;
    private Date mTime;
    private double price;
    private String desc;
    private int qt;

    public Product(int id, String nom, String description, int quantite, float prix, String image, Date dateCreation, Date dateModification) {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCTime() {
        return cTime;
    }

    public void setCTime(Date cTime) {
        this.cTime = cTime;
    }

    public Date getMTime() {
        return mTime;
    }

    public void setMTime(Date mTime) {
        this.mTime = mTime;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product(String nom, String desc, int qt, double price, String image, Date cTime, Date mTime) {
        this.nom = nom;
        this.desc = desc;
        this.qt = qt;
        this.price = price;
        this.image = image;
        this.cTime = cTime;
        this.mTime = mTime;
    }
    public Product() {

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getQt() {
        return qt;
    }

    public void setQt(int qt) {
        this.qt = qt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", desc='" + desc + '\'' +
                ", qt=" + qt + '\'' +
                ", price=" + price + '\'' +
                ", img=" + image + '\'' +
                ", cTime=" + cTime + '\'' +
                ", mTime=" + mTime + '\'' +
                '}';
    }
}
