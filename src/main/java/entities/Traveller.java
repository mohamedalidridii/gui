package entities;

import java.time.LocalDateTime;

public class Traveller extends User {

    public String passportNumber;


     public Traveller(int id, String name, int age, String gender, String email, int number,
                      String prename,String password ,LocalDateTime createdat, LocalDateTime lastLogin,String location) {
         super( id,  name,  age,  gender,  email,  number,
          prename, password , createdat,  lastLogin, location,Role.Traveller);

     }
     public Traveller(int id, String name, int age, String gender, String email, int number, String prename,String passwordHashed,String location,String passportNumber) {
         super(id,name,age,gender,email,number,prename,passwordHashed,location,Role.Traveller);
         this.passportNumber = passportNumber;
     }
     public Traveller(int id, String name, int age, String gender, String email,
                       int number, String prename, String passwordHashed , LocalDateTime createdat, LocalDateTime lastLogin,String location,String passportNumber) {
         super(id,name,age,gender,email,number,prename,passwordHashed,createdat,lastLogin,location,Role.Traveller);
         this.passportNumber = passportNumber;


     }
    public Traveller(int id, String name, int age, String gender, String email, int number,
                  String prename ,String location ,String passportNumber ) {
        super( id,  name,  age,  gender,  email,  number,
                prename, location );

        this.passportNumber = passportNumber;


    }

     public boolean joinEvent (){
         return false;
     }
     public boolean bookFlight(){
         return false;
     }
     public boolean buyProduct(){
         return false;
     }

     public String getPassportNumber() {
         return passportNumber;
     }
     public void setPassportNumber(String passportNumber) {
         this.passportNumber = passportNumber;
     }
     public String toString() {
         return  super.toString() + " " + passportNumber;
     }


}
