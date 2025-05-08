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
