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
                                    55.5, 28.4, 23.1, 215.17));

        products.put(2001, new Product(2001, "Філе білої риби на томатній подушці", 87.0, "основні страви",
                            "філе мерлузи, морква, кабачок, вершки, томатний соус, кріп, лимон",
                            100.5, 45.4, 54.1, 287.17));

        products.put(3001, new Product(3001, "Кіш лорен", 87.0, "сендвічі",
                "пісочне тісто (пшеничне борошно, вершкове масло, яйця, сіль), твердий сир, вершки, шинка, бекон, цибуля - порей",
                90.5, 99.4, 124.1, 341.17));
    }

    public HashMap<Integer,Product> getProducts() {
        return products;
    }
}
