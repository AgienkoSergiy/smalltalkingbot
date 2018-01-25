package ua.com.integrity.smalltalkingbot.message;

import ua.com.integrity.smalltalkingbot.model.Product;
import ua.com.integrity.smalltalkingbot.repository.ProductRepository;

public class MessageBuilder {


    public static String buildMenuMessage(){
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

    //TODO from configs
    public static String getGastroPrognosis(){
        return "У нашому кафе-магазині, як завжди, свіжа та смачна іжа. " +
                "В першій половині дня зазирніть на верхні полиці нашого холодильнику" +
                " - там на Вас чекають фірмові сирнички та гранола. " +
                "В обідню перерву фокусуйте погляд на середніх полицях. " +
                "Ближче до вечора переведіть погляд на нижні полиці - " +
                "там легкі й корисні салати, що нададуть сил на реалізацію вечірніх планів:)";
    }



}
