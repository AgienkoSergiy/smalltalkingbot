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
    private static final String INIT_QUERY = "CREATE TABLE PRODUCT\n" +
            "(\n" +
            "    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
            "    NAME VARCHAR(50) NOT NULL,\n" +
            "    PRICE DOUBLE NOT NULL,\n" +
            "    CATEGORY VARCHAR(20) NOT NULL,\n" +
            "    INGREDIENTS VARCHAR(200) NOT NULL,\n" +
            "    PROTEIN DOUBLE NOT NULL,\n" +
            "    FAT DOUBLE NOT NULL,\n" +
            "    CARBOHYDRATE DOUBLE NOT NULL,\n" +
            "    CALORICITY DOUBLE NOT NULL,\n" +
            "    WEIGHT INT NOT NULL,\n" +
            "    PIC_ID VARCHAR(100) NOT NULL\n" +
            ");\n" +
            "CREATE UNIQUE INDEX PRODUCT_ID_uindex ON PRODUCT (ID)\n" +
            "\n" +
            "INSERT INTO PUBLIC.PRODUCT (ID, NAME, PRICE, CATEGORY, INGREDIENTS, PROTEIN, FAT, CARBOHYDRATE, CALORICITY, WEIGHT, PIC_ID) VALUES (1001, 'Суп східний з броколі', 66, 'перші страви', 'броколі, горох, сочевиця, спеції в асортименті, вершки 26 %, сіль, зелень в асортименті (кріп, петрушка, цибуля зел)', 55.5, 28.4, 23.1, 215.17, 250, 'AgADAgADkagxG5slUUvhRjYpQPzQ0M_9Mg4ABEBBe4UMjXen7T0DAAEC');\n" +
            "INSERT INTO PUBLIC.PRODUCT (ID, NAME, PRICE, CATEGORY, INGREDIENTS, PROTEIN, FAT, CARBOHYDRATE, CALORICITY, WEIGHT, PIC_ID) VALUES (2001, 'Філе білої риби на томатній подушці', 87, 'основні страви', 'філе мерлузи, морква, кабачок, вершки, томатний соус, кріп, лимон', 100.5, 45.4, 54.1, 287.17, 350, 'AgADAgADp6gxG5slUUvM5oOc3JRURvIDnA4ABDeF5KJuAbZVh5wAAgI');\n" +
            "INSERT INTO PUBLIC.PRODUCT (ID, NAME, PRICE, CATEGORY, INGREDIENTS, PROTEIN, FAT, CARBOHYDRATE, CALORICITY, WEIGHT, PIC_ID) VALUES (3001, 'Кіш лорен', 90, 'основні страви', 'пісочне тісто (пшеничне борошно, вершкове масло, яйця, сіль), твердий сир, вершки, шинка, бекон, цибуля - порей', 90.5, 99.4, 124.1, 341.17, 200, 'AgADAgADqKgxG5slUUsi9s8MvpCZoqn_Mg4ABA3XiuwQ4uKLgjsDAAEC');\n" +
            "INSERT INTO PUBLIC.PRODUCT (ID, NAME, PRICE, CATEGORY, INGREDIENTS, PROTEIN, FAT, CARBOHYDRATE, CALORICITY, WEIGHT, PIC_ID) VALUES (4001, 'Наполеон десерт', 42, 'десерти', 'листкове тісто, білий шоколад, яєчний жовток, цукор, вершкове масло', 71.2, 153.4, 90.1, 247.8, 150, 'AgADAgADqagxG5slUUtZG8_TiTrcmmwGnA4ABIxRYBCzz9ISk5cAAgI');";
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
            conn.prepareStatement(INIT_QUERY);
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

/*
    private void initProducts(){
        products.put(1001, new Product(1001, "Суп східний з броколі", 66.0, "перші страви",
                                  "броколі, горох, сочевиця, спеції в асортименті, вершки 26 %, сіль, зелень в асортименті (кріп, петрушка, цибуля зел)",
                                    55.5, 28.4, 23.1, 215.17, 250, "AgADAgADkagxG5slUUvhRjYpQPzQ0M_9Mg4ABEBBe4UMjXen7T0DAAEC"));

        products.put(2001, new Product(2001, "Філе білої риби на томатній подушці", 87.0, "основні страви",
                            "філе мерлузи, морква, кабачок, вершки, томатний соус, кріп, лимон",
                            100.5, 45.4, 54.1, 287.17, 350, "AgADAgADp6gxG5slUUvM5oOc3JRURvIDnA4ABDeF5KJuAbZVh5wAAgI"));

        products.put(3001, new Product(3001, "Кіш лорен", 90.0, "основні страви",
                "пісочне тісто (пшеничне борошно, вершкове масло, яйця, сіль), твердий сир, вершки, шинка, бекон, цибуля - порей",
                90.5, 99.4, 124.1, 341.17, 200, "AgADAgADqKgxG5slUUsi9s8MvpCZoqn_Mg4ABA3XiuwQ4uKLgjsDAAEC"));

        products.put(4001, new Product(4001, "Наполеон десерт", 42.0, "десерти",
                "листкове тісто, білий шоколад, яєчний жовток, цукор, вершкове масло",
                71.2, 153.4, 90.1, 247.8,150, "AgADAgADqagxG5slUUtZG8_TiTrcmmwGnA4ABIxRYBCzz9ISk5cAAgI"));
    }
*/
    public HashMap<Integer,Product> getProducts() {
        return products;
    }


    public Product getProductById(Integer productId){
        return products.get(productId);
    }

}
