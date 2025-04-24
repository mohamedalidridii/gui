package entities;

public class Traveller extends User {
     public Traveller(int id, String name, int age, String gender, String email, int number, String prename) {
         super(id,name,age,gender,email,number,prename);

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

}
