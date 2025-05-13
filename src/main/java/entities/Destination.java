package Entities;

public class Destination {
    private int id;
    private String name;
    private String address;

    public Destination(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Destination() {
    }

    public Destination(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
