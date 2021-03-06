package ua.com.integrity.smalltalkingbot.controller;

import com.liqpay.LiqPay;
import ua.com.integrity.smalltalkingbot.bot.SmallTalkingBot;
import ua.com.integrity.smalltalkingbot.message.MessageBuilder;
import ua.com.integrity.smalltalkingbot.model.Product;
import ua.com.integrity.smalltalkingbot.model.Order;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderController {
    private static final String PROPERTY_FILE_PATH = "application.properties";

    private SmallTalkingBot smallTalkingBot;
    private HashMap<Long, Order> activeOrders;
    private HashMap<Long, ScheduledExecutorService> activeTimeoutTasks;

    public OrderController(SmallTalkingBot smallTalkingBot) {
        this.smallTalkingBot = smallTalkingBot;
        activeOrders = new HashMap<>();
        activeTimeoutTasks = new HashMap<>();
    }

    public void createOrder(long chatId, Product product){
        Order order = new Order(chatId, product);
        holdOrder(chatId, order);
    }

    public void processOrder(long chatId, Order order, String phoneNumber){
        try{
            String resultMessage = processLiqPayOrder(phoneNumber,order);
            smallTalkingBot.sendDefaultMessage(chatId, resultMessage);
            releaseOrder(chatId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void holdOrder(long chatId, Order order){
        activeOrders.put(chatId, order);
        setOrderProcessingTimeout(chatId);
    }

    private void setOrderProcessingTimeout(long chatId){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            activeOrders.remove(chatId);
            smallTalkingBot.sendDefaultMessage(chatId, MessageBuilder.getTimeoutMessage());
        };

        executor.schedule(task,30, TimeUnit.SECONDS);
        activeTimeoutTasks.put(chatId,executor);
    }

    public void releaseOrder(long chatId){
        if(isActiveOrder(chatId)){
            activeOrders.remove(chatId);
        }
        if (timeoutIsActive(chatId)){
            activeTimeoutTasks.get(chatId).shutdownNow();
            activeTimeoutTasks.remove(chatId);
        }
    }

    public boolean isActiveOrder(long chatId){
        return activeOrders.entrySet().stream()
                .anyMatch(ent -> ent.getKey() == chatId);
    }

    private Boolean timeoutIsActive(long chatId){
        return activeTimeoutTasks.entrySet().stream()
                .anyMatch(ent -> ent.getKey() == chatId);
    }

    public Order getActiveOrder(long chatId){
        return activeOrders.get(chatId);
    }

    private String processLiqPayOrder(String phoneNumber, Order order){
        Properties properties = getProperties(PROPERTY_FILE_PATH);

        LiqPay liqpay = new LiqPay(properties.getProperty("store.publickey"),
                                    properties.getProperty("store.privatekey"));
        try {
            HashMap<String, Object> res = (HashMap<String, Object>)liqpay.api("request", getNewOrderParams(order, phoneNumber));

            if ("ok".equals(res.get("result").toString())){
                return "Платіж відправлено, замовлення можна оплатити за посиланням:\n" +
                        res.get("href").toString() + "\n" +
                        "або підтвердити платіж в додатку Приват24.\n" +
                        "Смачного!";
            }else{
                return "Сталася прикра помилка :(";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Сталася прикра помилка :(";
    }

    private Properties getProperties(String filePath){
        try (InputStream inputStream =
                     getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Property file reading error ", e);
        }
    }


    private HashMap<String, String> getNewOrderParams(Order order, String phoneNumber){
        HashMap<String, String> params = new HashMap<>();
        params.put("currency", "UAH");
        params.put("amount", "1"/*order.getProduct().getPrice().toString()*/);
        params.put("description", "Small Talking food refrigerator");
        params.put("channel_type", "telegram");
        params.put("version", "3");
        params.put("phone", phoneNumber);
        params.put("order_id", order.getId());
        params.put("action", "invoice_bot");
        //put this parameter for test payments
        params.put("sandbox", "1");
        return params;
    }



}
