package ua.com.integrity.smalltalkingbot.repository;

import ua.com.integrity.smalltalkingbot.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static ua.com.integrity.smalltalkingbot.util.DBUtil.getConnection;

public class ProductRepository {

    private HashMap<Integer,Product> products;
   private static final String GET_ALL_QUERY = "SELECT * FROM SMALLTALKINGDB.PUBLIC.PRODUCT";

    private ProductRepository(){
        products = new HashMap<>();
        initProducts();
    }

    private static class ProductRepositoryHelper{
        private static final ProductRepository INSTANCE = new ProductRepository();
    }

    public static ProductRepository getInstance(){
        return ProductRepositoryHelper.INSTANCE;
    }

    private void initProducts(){
        products = getAllProducts();
    }

    private HashMap<Integer,Product> getAllProducts(){
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(GET_ALL_QUERY)) {
                ResultSet rs = st.executeQuery();
                return getProductsList(rs);
            } catch (SQLException e) {
                throw new RuntimeException("Fail to get all users", e);
            }
        } catch (SQLException e1) {
            throw new RuntimeException("A problem with connection", e1);
        }
    }

    private HashMap<Integer,Product> getProductsList(ResultSet rs)throws SQLException {
        HashMap<Integer,Product> map = new HashMap<>();
        while(rs.next()) {
            Product product = new Product(
                    Integer.parseInt(rs.getString("ID")),
                    rs.getString("NAME"),
                    rs.getDouble("PRICE"),
                    rs.getString("CATEGORY"),
                    rs.getString("INGREDIENTS"),
                    rs.getDouble("PROTEIN"),
                    rs.getDouble("FAT"),
                    rs.getDouble("CARBOHYDRATE"),
                    rs.getDouble("CALORICITY"),
                    rs.getInt("WEIGHT"),
                    rs.getString("PIC_ID"));
            map.put(product.getId(),product);
        }
        return map;
    }

    public HashMap<Integer,Product> getProducts() {
        return products;
    }


    public Product getProductById(Integer productId){
        return products.get(productId);
    }

}
