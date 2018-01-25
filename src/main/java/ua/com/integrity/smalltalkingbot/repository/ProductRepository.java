package ua.com.integrity.smalltalkingbot.repository;

import ua.com.integrity.smalltalkingbot.model.Product;

import java.util.HashMap;

public class ProductRepository {

    private HashMap<Integer,Product> products;

    private ProductRepository(){
        products = new HashMap<Integer,Product>();
        initProducts();
    }

    private static class ProductRepositoryHelper{
        private static final ProductRepository INSTANCE = new ProductRepository();
    }

    public static ProductRepository getInstance(){
        return ProductRepositoryHelper.INSTANCE;
    }


    private void initProducts(){
        products.put(1001, new Product(1001, "Суп східний з броколі", 66.0, "перші страви",
                                  "броколі, горох, сочевиця, спеції в асортименті, вершки 26 %, сіль, зелень в асортименті (кріп, петрушка, цибуля зел)",
                                    55.5, 28.4, 23.1, 215.17, 250, "AgADAgADkagxG5slUUvhRjYpQPzQ0M_9Mg4ABEBBe4UMjXen7T0DAAEC"));

        products.put(2001, new Product(2001, "Філе білої риби на томатній подушці", 87.0, "основні страви",
                            "філе мерлузи, морква, кабачок, вершки, томатний соус, кріп, лимон",
                            100.5, 45.4, 54.1, 287.17, 350, "AgADAgADp6gxG5slUUvM5oOc3JRURvIDnA4ABDeF5KJuAbZVh5wAAgI"));

        products.put(3001, new Product(3001, "Кіш лорен", 87.0, "сендвічі",
                "пісочне тісто (пшеничне борошно, вершкове масло, яйця, сіль), твердий сир, вершки, шинка, бекон, цибуля - порей",
                90.5, 99.4, 124.1, 341.17, 200, "AgADAgADqKgxG5slUUsi9s8MvpCZoqn_Mg4ABA3XiuwQ4uKLgjsDAAEC"));

        products.put(4001, new Product(4001, "Наполеон десерт", 42.0, "десерти",
                "листкове тісто, білий шоколад, яєчний жовток, цукор, вершкове масло",
                71.2, 153.4, 90.1, 247.8,150, "AgADAgADqagxG5slUUtZG8_TiTrcmmwGnA4ABIxRYBCzz9ISk5cAAgI"));
    }

    public HashMap<Integer,Product> getProducts() {
        return products;
    }


    //TODO make it right
    public Product getProduct(Integer productId){
        Product product = products.get(productId);
        if (product == null){
            return null;
        }
        return product;
    }

}
