package services;

import entities.Product;
import utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduct implements Iservice<Product> {
    Connection connection;
    public ServiceProduct() {
        connection= Database.getInstance().getConnection();
    }
    @Override
    public void ajouter(Product product) throws SQLException {
        System.out.println("in ajouter");
        try{
            System.out.println("in try ajouter");
            String req ="INSERT INTO `product` (`nom`, `desc`, `qt`) VALUES ('"+product.getNom()+"','"+product.getDesc()+"',"+product.getQt()+")";
            System.out.println("after insert");
            Statement statement=connection.createStatement();
            System.out.println("after create stat");
            statement.executeUpdate(req);
            System.out.println("Produit ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Product product) throws SQLException {
        String req = "DELETE FROM `product` WHERE `id` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, product.getId());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(" supprimée avec succès");
            } else {
                System.out.println("Aucun product trouvée avec l'ID : " + product.getId());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Product product) throws SQLException {
        String req = "update product set `nom`=?, `desc`=?, `qt`=? where `id`=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, product.getNom());
        preparedStatement.setString(2, product.getDesc());
        preparedStatement.setInt(3, product.getQt());
        preparedStatement.setInt(4, product.getId());
        preparedStatement.executeUpdate();
        System.out.println("produit modified");

    }

    @Override
    public List<Product> afficher() throws SQLException {
        List<Product> products = new ArrayList<>();
        String req = "SELECT * FROM `product`\n";
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt(1));
            product.setNom(rs.getString("nom"));
            product.setDesc(rs.getString("desc"));
            product.setQt(rs.getInt(4));
            products.add(product);

        }


        return products;
    }
}
