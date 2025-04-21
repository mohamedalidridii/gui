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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Date getmTime() {
        return mTime;
    }

    public void setmTime(Date mTime) {
        this.mTime = mTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product(String nom, String desc, int qt) {
        this.nom = nom;
        this.desc = desc;
        this.qt = qt;
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
