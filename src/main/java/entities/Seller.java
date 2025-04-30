package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Seller extends User {


    private String storeName;
    private ArrayList<Product> products;

    public Seller(int id, String name, int age, String gender, String email, int number,
                     String prename,String password ,LocalDateTime createdat, LocalDateTime lastLogin,String location) {
        super( id,  name,  age,  gender,  email,  number,
                prename, password , createdat,  lastLogin, location,Role.Seller);
    }


    public Seller(int id, String name, int age, String gender, String email, int number, String prename,String passwordHashed,String location,String storeName) {
        super(id, name, age, gender, email, number, prename, passwordHashed,location,Role.Seller);
        this.storeName = storeName;

    }

    public Seller(int id, String name, int age, String gender, String email,
                     int number, String prename, String passwordHashed , LocalDateTime createdat, LocalDateTime lastLogin,String location,String storeName) {
        super(id,name,age,gender,email,number,prename,passwordHashed,createdat,lastLogin,location,Role.Seller);
        this.storeName = storeName;

    }

    public boolean sellProduct(){
        return true;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }


    public String toString() {
        return super.toString() + " " + getStoreName();
    }

}
