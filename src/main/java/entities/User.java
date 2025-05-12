package entities;

import java.time.LocalDateTime;
import org.mindrot.jbcrypt.BCrypt;


public abstract class User {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String email;
    private int number;
    private String lastname;
    private String passwordHashed;
    private final LocalDateTime createdat;
    private LocalDateTime lastLogin;
    private String location;
    private Role role;


    public User(int id, String name, int age, String gender, String email, int number, String prename,String password,String location ,Role role ) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.number = number;
        this.lastname = prename;
        this.createdat = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
        this.passwordHashed = BCrypt.hashpw(password, BCrypt.gensalt());
        this.location = location;
        this.role=role;

    }
    public User(int id, String name, int age, String gender, String email, int number,
                String prename,String password ,LocalDateTime createdat, LocalDateTime lastLogin,String location ,Role role ) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.number = number;
        this.lastname = prename;
        this.createdat = createdat;
        this.lastLogin = lastLogin;
        this.passwordHashed = BCrypt.hashpw(password, BCrypt.gensalt());
        this.location = location;
        this.role=role;

    }
    public User(int id, String name, int age, String gender, String email, int number,
                String prename,String location  ) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.number = number;
        this.lastname = prename;
        this.createdat = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
        this.location = location;

    }





    //getters and setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrename () {
        return lastname;
    }
    public void setPrename(String prename) {
        this.lastname = prename;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getPasswordHashed() {
        return passwordHashed;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Role getRole() {return role;}

    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed = passwordHashed;
    }
    public LocalDateTime getCreatedat() {
        return createdat;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String toString() {
        return id + " " + name + " " + lastname+ " " + age + " " + gender + " " + email +" "+ passwordHashed +" " + number + " " + createdat + " " + lastLogin+" "+location +" "+role;
    }




}
