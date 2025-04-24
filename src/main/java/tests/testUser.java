package tests;

import entities.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class testUser {
    public static void main(String[] args) {
        serviceTraveller st = new serviceTraveller();
        ServiceSeller ss=new ServiceSeller();

        Traveller t =new Traveller(7,"Walae",21,"Male","ghwala@gmail",20155144,"Ghazouani");
        Seller s =new Seller(15,"manel",21,"Female","manel@gmail",20155144,"jamal");
        Seller s2 =new Seller(5,"jamel",25,"Male","manel@gmail",20155144,"BenAli");

        Traveller nt=new Traveller(4,"hamza",21,"Male","hamza@gmail",20155144,"benhmida");
        try{
            //st.addUser(t);
            //st.updateUser(nt);
            //st.deleteUser(t);

            ArrayList< User > users=ss.reaadUser();
            for(User u:users){
                System.out.println(u);
            }

            //ss.deleteUser(s);


            //ss.addUser(s);
            //ss.addUser(s);

        }

        catch( SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
