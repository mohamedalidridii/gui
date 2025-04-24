package entities;

public abstract class User {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String email;
    private int number;
    private String prename;


    public User(int id, String name, int age, String gender, String email, int number, String prename) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.number = number;
        this.prename = prename;

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
        return prename;
    }
    public void setPrename(String prename) {
        this.prename = prename;
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

    public String toString() {
        return id + " " + name + " " + age + " " + gender + " " + email + " " + number + " " + prename;
    }



}
