package ua.com.integrity.smalltalkingbot.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.com.integrity.smalltalkingbot.message.MessageBuilder;
import ua.com.integrity.smalltalkingbot.model.Product;
import ua.com.integrity.smalltalkingbot.order.Order;
import ua.com.integrity.smalltalkingbot.repository.ProductRepository;
import ua.com.integrity.smalltalkingbot.utils.CommonUtils;

public class SmallTalkingBot extends TelegramLongPollingBot {
    private Boolean orderInProcess = false;


    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            if (orderInProcess){
                //TODO finish it!
                if (CommonUtils.isValidPhoneNumber(messageText)) {
                    processOder(messageText,chatId);
                }else if ("\u274C Відмінити".equals(messageText)){
                    orderInProcess = false;
                    ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showButtonText("\uD83C\uDF72 Меню");
                    sendTextMessage( "Оплату відмінено.\n", chatId, replyKeyboardMarkup);
                }else{
                    ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showPhoneButton();
                    MessageBuilder.addRowButton(replyKeyboardMarkup,"\u274C Відмінити");
                    sendTextMessage("Помилка введення.\n" +
                            "Введіть уважно свій номер телефону в форматі +380ХХХХХХХХХ, або натисніть на кнопку:",chatId, replyKeyboardMarkup);
                }
            } else if (messageText.equals("/start")) {
                sendStartMessage(chatId);

            }else if(messageText.length() == 5 && CommonUtils.isValidProductId(messageText)){
                sendProductInfoMessage(messageText, chatId);

            }else if (messageText.matches("/menu|Меню|меню|Menu|menu|\uD83C\uDF72 Меню")) {
                sendTextMessage( MessageBuilder.buildMenuMessage(), chatId, new ReplyKeyboardRemove());

            }else if(messageText.equals("\uD83D\uDCB3 Оплатити")){
                orderInProcess = true;
                ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showPhoneButton();
                MessageBuilder.addRowButton(replyKeyboardMarkup,"\u274C Відмінити");
                sendTextMessage("Введіть свій номер телефону в форматі +380ХХХХХХХХХ, " +
                        "або натисніть на кнопку 'Надати свій номер телефону':",chatId, replyKeyboardMarkup);

            }else{
                sendDefaultMessage(chatId);
            }

        }else if(update.hasMessage() && update.getMessage().getContact() !=null && orderInProcess){
            processOder(update.getMessage().getContact().getPhoneNumber(),chatId);
        }else{
            sendDefaultMessage(chatId);
        }
        //That is for getting photo id
        /*else if (update.hasMessage() && update.getMessage().hasPhoto()) {

            List<PhotoSize> photos = update.getMessage().getPhoto();
            String photoId = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null).getFileId();
            int photoWidth = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null).getWidth();
            // Know photo height
            int photoHeight = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null).getHeight();
            // Set photo caption
            String caption = "file_id: " + photoId + "\nwidth: " + Integer.toString(photoWidth) + "\nheight: " + Integer.toString(photoHeight);
            SendPhoto msg = new SendPhoto()
                    .setChatId(chatId)
                    .setPhoto(photoId)
                    .setCaption(caption);
            try {
                sendPhoto(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }*/
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
        SendPhoto msg = new SendPhoto()
                .setChatId(chatId)
                .setPhoto(product.getPicId())
                .setCaption(product.getName());
        try {
            sendPhoto(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendStartMessage(long chatId){
        sendTextMessage("Ласкаво просимо до SmallTalking :)",chatId);
        sendTextMessage(MessageBuilder.getGastroPrognosis(),chatId, MessageBuilder.showButtonText("Меню"));
    }

    private void sendProductInfoMessage(String messageText, long chatId){
        Integer productId = CommonUtils.extractProductId(messageText);
        Product product = ProductRepository.getInstance().getProduct(productId);

        ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showButtonText("\uD83D\uDCB3 Оплатити");
        MessageBuilder.addRowButton(replyKeyboardMarkup, "\uD83C\uDF72 Меню");

        sendProductPhotoMessage(product,chatId);
        sendTextMessage(MessageBuilder.getProductDescription(productId), chatId, replyKeyboardMarkup);
    }

    private void sendDefaultMessage(long chatId){
        sendTextMessage("Не зрозумів?! Мабуть, хтось дуже голодний.", chatId);
        sendTextMessage("Сьогодні ми пропонуємо:\n" + MessageBuilder.buildMenuMessage(),chatId, new ReplyKeyboardRemove());
    }


    private void processOder(String phoneNumber, long chatId){
        Order order = new Order();
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showButtonText("\uD83C\uDF72 Меню");
        sendTextMessage(order.callLiqPayApi(phoneNumber), chatId, replyKeyboardMarkup);
        orderInProcess = false;
    }

    private void processOder(String phoneNumber, Integer userId, long chatId){
        Order order = new Order();
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageBuilder.showButtonText("\uD83C\uDF72 Меню");
        sendTextMessage(order.callLiqPayApi(phoneNumber, userId), chatId, replyKeyboardMarkup);
        orderInProcess = false;
    }



    public String getBotUsername() {
        return "SmallTalkingBot";
    }

    public String getBotToken() {
        return "544269816:AAF1lKlnu2Sh-5tmxM-OW4q8KjquwQ4DRMc";
    }
}
