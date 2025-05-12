package services;

import java.sql.*;

import entities.Tag;
import utils.Database;
import java.sql.Connection;
import java.util.ArrayList;

public class ServiceTag {
    protected Connection conn=Database.getInstance().getConnection();

    private PreparedStatement insertStmt;
    private PreparedStatement selectStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;

    public ServiceTag() {
        try {
            insertStmt = conn.prepareStatement("insert into tag(name) values (?)", Statement.RETURN_GENERATED_KEYS);
            selectStmt = conn.prepareStatement("select * from tag where id=?");
            updateStmt = conn.prepareStatement("update tag set name=? where id=?");
            deleteStmt = conn.prepareStatement("delete from tag where id=?");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public boolean addTag(Tag tag){
        try {
            insertStmt.setString(1,tag.getName());
            if(insertStmt.executeUpdate()>0){
                ResultSet rs=insertStmt.getGeneratedKeys();
                if(rs.next()){
                    tag.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return false;
    }
    public Tag getTag(int id){
        try {
            selectStmt.setInt(1,id);
            ResultSet rs=selectStmt.executeQuery();
            if(rs.next()){
                Tag tag=new Tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                return tag;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean updateTag(Tag tag){
        try {
            updateStmt.setString(1,tag.getName());
            updateStmt.setInt(2,tag.getId());
            if(updateStmt.executeUpdate()>0)
                return true;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean deleteTag(int id){
        try {
            deleteStmt.setInt(1,id);
            if(deleteStmt.executeUpdate()>0)
                return true;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public ArrayList<Tag> getAllTag(){
        ArrayList<Tag> materielList = new ArrayList<>();

        String sql = "SELECT * FROM tag";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                materielList.add(new Tag(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching tags : " + e.getMessage());
        }

        return materielList;
    }
}
