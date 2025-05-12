package services;

import java.sql.*;

import entities.Location;
import utils.Database;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ServiceLocation {
    protected Connection conn=Database.getInstance().getConnection();

    private PreparedStatement insertStmt;
    private PreparedStatement selectStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;

    public ServiceLocation() {
        try {
            insertStmt = conn.prepareStatement("insert into location(name,address) values (?,?)", Statement.RETURN_GENERATED_KEYS);
            selectStmt = conn.prepareStatement("select * from location where id=?");
            updateStmt = conn.prepareStatement("update location set name=?,address=? where id=?");
            deleteStmt = conn.prepareStatement("delete from location where id=?");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public boolean addLocation(Location location){
        try {
            insertStmt.setString(1,location.getName());
            insertStmt.setString(2,location.getAddress());
            if(insertStmt.executeUpdate()>0){
                ResultSet rs=insertStmt.getGeneratedKeys();
                if(rs.next()){
                    location.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return false;
    }
    public Location getLocation(int id){
        try {
            selectStmt.setInt(1,id);
            ResultSet rs=selectStmt.executeQuery();
            if(rs.next()){
                Location location=new Location();
                location.setId(rs.getInt("id"));
                location.setName(rs.getString("name"));
                location.setAddress(rs.getString("address"));
                return location;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean updateLocation(Location location){
        try {
            updateStmt.setString(1,location.getName());
            updateStmt.setString(2,location.getAddress());
            updateStmt.setInt(3,location.getId());
            if(updateStmt.executeUpdate()>0)
                return true;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean deleteLocation(int id){
        try {
            deleteStmt.setInt(1,id);
            if(deleteStmt.executeUpdate()>0)
                return true;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public ArrayList<Location> getAllLocation(){
        ArrayList<Location> materielList = new ArrayList<>();

        String sql = "SELECT * FROM location";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                materielList.add(new Location(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching locations : " + e.getMessage());
        }

        return materielList;
    }
    public static List<Location> searchLocations(List<Location> locations, String keyword) {
        return locations.stream()
                .filter(location ->
                        location.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                location.getAddress().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
