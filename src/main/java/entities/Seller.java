package entities;

public class Seller extends User {

    public Seller(int id, String name, int age, String gender, String email, int number, String prename) {
        super(id, name, age, gender, email, number, prename);
    }

    public boolean sellProduct(){
        return true;
    }
}
