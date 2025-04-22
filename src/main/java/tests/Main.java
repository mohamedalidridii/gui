package tests;

import entities.Product;
import services.ServiceProduct;

import java.util.*;

import java.sql.Date;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args)  {

        Product product = new Product("hello","this Desc is a test",17, 80.500, "path", new Date(2000, 11, 15),new Date(2025, 5, 1));

        ServiceProduct serviceProduct = new ServiceProduct();
        try {
            Product productMod = new Product();
            serviceProduct.ajouter(product);
            productMod.setId(16);
            productMod.setNom("NouveauNom");
            productMod.setDesc("NouveauDesc");
            productMod.setQt(25);

            //serviceProduct.modifier(productMod);//
            Product prodSupp = new Product();
            //prodSupp.setId(46); Remplacez par l'ID réel de la personne à supprimer.
            //serviceProduct.supprimer(prodSupp);
            System.out.println(serviceProduct.afficher());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
