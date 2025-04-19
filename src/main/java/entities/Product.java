package entities;

public class Product {
    private int id;
    private String nom;
    private String desc;
    private int qt;

    public Product() {
    }

    public Product(String nom, String desc, int qt) {
        this.nom = nom;
        this.desc = desc;
        this.qt = qt;
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
                ", qt=" + qt +
                '}';
    }
}
