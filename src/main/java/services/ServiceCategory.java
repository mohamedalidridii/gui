package services;

import entities.Category;
import entities.Product;
import utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategory implements Iservice<Category> {
    Connection connection;
    public ServiceCategory() {
        connection= Database.getInstance().getConnection();
    }
    @Override
    public void ajouter(Category category) throws SQLException {

        try{
            String req ="INSERT INTO `category` (`nom`, `desc`) VALUES ('"+category.getNom()+"','"+category.getDesc()+"')";
            Statement statement=connection.createStatement();
            statement.executeUpdate(req);
            System.out.println("Category ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void supprimer(Category category) throws SQLException {
        String req = "DELETE FROM `category` WHERE `id` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, category.getId());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(" supprimée avec succès");
            } else {
                System.out.println("Aucune category trouvée avec l'ID : " + category.getId());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }
    @Override
    public void modifier(Category category) throws SQLException {
        String req = "update category set `nom`=?, `desc`=? where `id`=?";
        System.out.println(req);
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, category.getNom());
        preparedStatement.setString(2, category.getDesc());

        preparedStatement.setInt(8, category.getId());
        preparedStatement.executeUpdate();
        System.out.println("Category modified");

    }
    @Override
    public List<Category> afficher() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String req = "SELECT * FROM `category`\n";
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            Category category = new Category();
            category.setId(rs.getInt(1));
            category.setNom(rs.getString("nom"));
            category.setDesc(rs.getString("desc"));
            categories.add(category);

        }
        return categories;
    }
}
