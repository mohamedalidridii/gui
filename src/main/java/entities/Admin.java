package entities;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;
import services.AccessLevel;
import services.ServiceSeller;
import services.serviceTraveller;


public class Admin extends User {

    private AccessLevel accessLevel;
    private ServiceSeller serviceSeller;
    private serviceTraveller serviceTraveller;


    public Admin(int id, String name, int age, String gender, String email, int number, String prename,String password,String location,AccessLevel accessLevel){
        super(id,name,age,gender,email,number,prename,password,location,Role.Admin);
        this.serviceSeller=new ServiceSeller();
        this.serviceTraveller=new serviceTraveller();
        this.accessLevel=accessLevel;
    }

    public Admin(int id, String name, int age, String gender, String email,
                     int number, String prename, String passwordHashed , LocalDateTime createdat, LocalDateTime lastLogin, String location,AccessLevel accessLevel) {
        super(id, name, age, gender, email, number, prename, passwordHashed, createdat, lastLogin, location, Role.Admin);
        this.accessLevel = accessLevel;
    }
    public Admin(int id, String name, int age, String gender, String email,
                     int number, String prename, String passwordHashed , LocalDateTime createdat, LocalDateTime lastLogin, String location) {
        super(id, name, age, gender, email, number, prename, passwordHashed, createdat, lastLogin, location, Role.Admin);
    }


    //setters getters
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    //TOSTRING

    public String toString(){
        return super.toString()+" "+accessLevel.toString();
    }


    //MANAGE USER

    public void createUser(User user)throws SQLException {


            if (user instanceof Traveller)             serviceTraveller.addUser(user);
            else serviceSeller.addUser(user);


    }

    public void updateUser(User user) throws SQLException {

        serviceTraveller.updateUser(user);
    }

    public void deleteUser(int id) {
        try{
            serviceTraveller.deleteUser(id);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }



    public ArrayList<User> readTravellers() {
        try{
            return serviceTraveller.reaadUser();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }
    public ArrayList<User> readSellers() {
        try{
            return serviceSeller.reaadUser();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }

    public ArrayList<User> readAll() throws SQLException {

        try {
            return serviceTraveller.reaadAll();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }














    public void maanageFlights () {}
    public void manageProducts () {}
    public void manageHebergement() {}



}
