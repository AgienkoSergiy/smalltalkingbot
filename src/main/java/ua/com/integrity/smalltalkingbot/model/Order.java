package ua.com.integrity.smalltalkingbot.model;


import java.util.UUID;

public class Order {

    private String id;
    private Long chatId;
    private Product product;

    public Order(Long chatId, Product product) {
        this.id = UUID.randomUUID().toString();
        this.chatId = chatId;
        this.product = product;
    }


    public String getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public Product getProduct() {
        return product;
    }
}
