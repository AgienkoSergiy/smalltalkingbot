package ua.com.integrity.smalltalkingbot.controller;

import ua.com.integrity.smalltalkingbot.bot.SmallTalkingBot;
import ua.com.integrity.smalltalkingbot.message.MessageBuilder;
import ua.com.integrity.smalltalkingbot.model.Product;
import ua.com.integrity.smalltalkingbot.repository.ProductRepository;

import java.util.HashMap;
import java.util.concurrent.*;

public class ProductController {

    private SmallTalkingBot smallTalkingBot;
    private HashMap<Long, Product> currentProducts;
    private HashMap<Long, ScheduledExecutorService> activeTimeoutTasks;

    public ProductController(SmallTalkingBot smallTalkingBot) {
        this.smallTalkingBot = smallTalkingBot;
        currentProducts = new HashMap<>();
        activeTimeoutTasks = new HashMap<>();
    }

    public Product holdCurrentProduct(long chatId, Integer productId) {
        Product product = ProductRepository.getInstance().getProductById(productId);
        currentProducts.put(chatId, product);
        setProductViewTimeout(chatId);
        return product;

    }

    private void setProductViewTimeout(long chatId){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            currentProducts.remove(chatId);
            smallTalkingBot.sendDefaultMessage(chatId, MessageBuilder.getTimeoutMessage());
        };

        executor.schedule(task,5,TimeUnit.MINUTES);
        activeTimeoutTasks.put(chatId,executor);
    }

    public void releaseCurrentProduct(long chatId){
        if(hasCurrentProduct(chatId))
            currentProducts.remove(chatId);

        if(timeoutIsActive(chatId)) {
            activeTimeoutTasks.get(chatId).shutdownNow();
            activeTimeoutTasks.remove(chatId);
        }
    }

    public Boolean hasCurrentProduct(long chatId){
        return currentProducts.entrySet().stream()
                .anyMatch(ent -> ent.getKey() == chatId);
    }

    private Boolean timeoutIsActive(long chatId){
        return activeTimeoutTasks.entrySet().stream()
                .anyMatch(ent -> ent.getKey() == chatId);
    }

    public Product getCurrentProduct(Long chatId){
        return currentProducts.get(chatId);
    }

    public HashMap<Long, Product> getCurrentProducts() {
        return currentProducts;
    }
}
