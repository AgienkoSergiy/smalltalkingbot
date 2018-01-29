package ua.com.integrity.smalltalkingbot.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.com.integrity.smalltalkingbot.controller.NotificationController;
import ua.com.integrity.smalltalkingbot.controller.OrderController;
import ua.com.integrity.smalltalkingbot.controller.UserController;
import ua.com.integrity.smalltalkingbot.message.MessageBuilder;
import ua.com.integrity.smalltalkingbot.model.Product;
import ua.com.integrity.smalltalkingbot.controller.ProductController;
import ua.com.integrity.smalltalkingbot.model.Order;
import ua.com.integrity.smalltalkingbot.repository.ProductRepository;
import ua.com.integrity.smalltalkingbot.util.CommonUtils;
import ua.com.integrity.smalltalkingbot.util.DBUtil;

import java.util.Collection;

public class SmallTalkingBot extends TelegramLongPollingBot {

    private ProductController productController;
    private OrderController orderController;
    private NotificationController notificationController;
    private UserController userController;

    public SmallTalkingBot() {
        this.productController = new ProductController(this);
        this.orderController = new OrderController(this);
        this.notificationController = new NotificationController(this);
        this.userController = new UserController(this);
        DBUtil.getInstance();
    }

    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            if (orderController.isActiveOrder(chatId)){
                if (CommonUtils.isValidPhoneNumber(messageText)) {
                    sendDefaultMessage(chatId, MessageBuilder.getAcceptedOrderMessage());
                    orderController.processOrder(chatId,orderController.getActiveOrder(chatId),messageText);
                    productController.releaseCurrentProduct(chatId);
                    orderController.releaseOrder(chatId);

                }else if ("\u274C Відмінити".equals(messageText)){
                    orderController.releaseOrder(chatId);
                    productController.releaseCurrentProduct(chatId);
                    ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showButtonText("\uD83C\uDF72 Меню");
                    sendTextMessage( "Оплату відмінено.\n", chatId, replyKeyboardMarkup);

                }else{
                    ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showPhoneButton();
                    MessageBuilder.addRowButton(replyKeyboardMarkup,"\u274C Відмінити");
                    sendTextMessage("Помилка введення.\n" +
                            "Введіть уважно свій номер телефону в форматі +380ХХХХХХХХХ," +
                            " або натисніть на кнопку:",chatId, replyKeyboardMarkup);
                }
            } else if (messageText.equals("/start")) {
                if(!userController.userExists(chatId))
                    userController.addUser(chatId);
                sendStartMessage(chatId);

            }else if(messageText.length() == 5 && CommonUtils.isValidProductId(messageText)){
                Integer productId = CommonUtils.extractProductId(messageText);
                Product product = productController.holdCurrentProduct(chatId,productId);
                sendProductInfoMessage(product, chatId);

            }else if (messageText.matches("/menu|Меню|меню|Menu|menu|\uD83C\uDF72 Меню")) {
                productController.releaseCurrentProduct(chatId);
                Collection<Product> products = ProductRepository.getInstance().getProducts().values();
                sendTextMessage( MessageBuilder.buildMenuMessage(products), chatId, new ReplyKeyboardRemove());

            }else if(messageText.equals("\uD83D\uDCB3 Оплатити")){
                orderController.createOrder(chatId, productController.getCurrentProduct(chatId));
                ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showPhoneButton();
                MessageBuilder.addRowButton(replyKeyboardMarkup,"\u274C Відмінити");
                sendTextMessage("Введіть свій номер телефону в форматі +380ХХХХХХХХХ, " +
                        "або натисніть на кнопку 'Надати свій номер телефону':",chatId, replyKeyboardMarkup);

            }else if (messageText.matches("/details|\uD83D\uDCDD Деталі страви")&& productController.hasCurrentProduct(chatId)) {
                ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showButtonText("\uD83D\uDCB3 Оплатити");
                MessageBuilder.addRowButton(replyKeyboardMarkup, "\uD83C\uDF72 Меню");
                sendTextMessage( productController.getCurrentProduct(chatId).getDetails(), chatId, replyKeyboardMarkup);

            }else{
                productController.releaseCurrentProduct(chatId);
                sendMisunderstandingMessage(chatId);
            }

        }else if(update.hasMessage() && update.getMessage().getContact()!=null && orderController.isActiveOrder(chatId)){
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();
            Order order = orderController.getActiveOrder(chatId);
            sendDefaultMessage(chatId, MessageBuilder.getAcceptedOrderMessage());
            orderController.processOrder(chatId, order, phoneNumber);
        }else{
            productController.releaseCurrentProduct(chatId);
            sendMisunderstandingMessage(chatId);
        }
    }


    private void sendTextMessage(String messageText, long chat_id){
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendTextMessage(String messageText, long chat_id, ReplyKeyboard keyboardMarkup){
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText(messageText);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendProductPhotoMessage(Product product, long chatId){
        String caption = product.getName() + " - " + product.getPrice().intValue() + " грн";
        SendPhoto msg = new SendPhoto()
                .setChatId(chatId)
                .setPhoto(product.getPicId())
                .setCaption(caption);
        try {
            sendPhoto(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendProductInfoMessage(Product product, long chatId){
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showButtonText("\uD83D\uDCB3 Оплатити");
        MessageBuilder.addButton(replyKeyboardMarkup,0,"\uD83D\uDCDD Деталі страви");
        MessageBuilder.addRowButton(replyKeyboardMarkup, "\uD83C\uDF72 Меню");

        sendProductPhotoMessage(product,chatId);
        sendTextMessage("Деталі тут \u27A1 /details", chatId, replyKeyboardMarkup);
    }

    public void sendDefaultMessage(long chatId, String text){
        sendTextMessage(text,chatId, MessageBuilder.showButtonText("\uD83C\uDF72 Меню"));
    }

    private void sendStartMessage(long chatId){
        sendTextMessage("Ласкаво просимо до SmallTalking :)",chatId);
        sendTextMessage(MessageBuilder.getGastroPrognosis(),chatId, MessageBuilder.showButtonText("\uD83C\uDF72 Меню"));
    }


    private void sendMisunderstandingMessage(long chatId){
        Collection<Product> products = ProductRepository.getInstance().getProducts().values();
        sendTextMessage("Не зрозумів?! Мабуть, хтось дуже голодний.", chatId);
        sendTextMessage("Сьогодні ми пропонуємо:\n" + MessageBuilder.buildMenuMessage(products),chatId, new ReplyKeyboardRemove());
    }


    public String getBotUsername() {
        return "SmallTalkingBot";
    }

    public String getBotToken() {
        return "544269816:AAF1lKlnu2Sh-5tmxM-OW4q8KjquwQ4DRMc";
    }
}
