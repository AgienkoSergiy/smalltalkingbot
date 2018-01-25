package ua.com.integrity.smalltalkingbot.message;

import ua.com.integrity.smalltalkingbot.model.Product;
import ua.com.integrity.smalltalkingbot.repository.ProductRepository;

public class MessageBuilder {


    public static String buildGastroPrognosis(){
        StringBuffer textMessage = new StringBuffer();

        for (Product product:
             ProductRepository.getInstance().getProducts().values()) {
            textMessage = textMessage.append(product.getName())
                    .append("(/id").append(product.getId()).append(") - ")
                    .append(product.getPrice()).append(" грн\n");
        }

        return textMessage.toString();
    }

    public static String getProductDescription(Integer productId){
        if (!ProductRepository.getInstance().getProducts().containsKey(productId)){
            return null;
        }
        return ProductRepository.getInstance().getProducts().get(productId).getFullDescription();
    }

}
