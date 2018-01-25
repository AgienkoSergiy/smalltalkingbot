package ua.com.integrity.smalltalkingbot.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.com.integrity.smalltalkingbot.message.MessageBuilder;
import ua.com.integrity.smalltalkingbot.model.Product;
import ua.com.integrity.smalltalkingbot.order.Order;
import ua.com.integrity.smalltalkingbot.repository.ProductRepository;

import java.util.Comparator;
import java.util.List;

public class SmallTalkingBot extends TelegramLongPollingBot {
    private Boolean orderInProcess = false;

    public void onUpdateReceived(Update update) {
        String outputMessage;
        long chatId = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            if (orderInProcess){
                //TODO finish it!
                Order order = new Order();
                sendTextMessage(order.callLiqPayApi(messageText), chatId);
                orderInProcess = false;

            } else if (messageText.equals("/start")) {
                sendTextMessage("Доброго дня!\n" +
                        "Ласкаво просимо до SmallTalking :)",chatId);
                sendTextMessage(MessageBuilder.getGastroPrognosis(),chatId);
                sendTextMessage("Переглянути меню (/menu)",chatId);

            }else if(messageText.length() > 6 && messageText.substring(0,3).equals("/id")){
                Integer productId = Integer.valueOf(messageText.substring(3));
                Product product = ProductRepository.getInstance().getProduct(productId);


                if(product ==null){
                    sendTextMessage("Продукт з таким id не знайдено :(\n" +
                            "Спробуйте вибрати щось інше:\n", chatId);
                } else{
                    sendTextMessage(MessageBuilder.getProductDescription(productId), chatId);
                    sendPhotoMessage(ProductRepository.getInstance().getProduct(productId),chatId);
                }
                sendTextMessage("\nНазад до меню (/menu)",chatId);

            }else if (messageText.equals("/menu")) {
                sendTextMessage( "Сьогоднішнє меню:\n" + MessageBuilder.buildMenuMessage(), chatId);
            }else if(messageText.equals("/confirm")){
                orderInProcess = true;
                sendTextMessage("Введіть свій номер телефону в форматі +380ХХХХХХХХХ:",chatId);
            }else{
               sendTextMessage("Не треба сумувати, в холодильничку для Вас є щось смачненьке:\n" +
                        MessageBuilder.buildMenuMessage(),chatId);
            }

        }else if (update.hasMessage() && update.getMessage().hasPhoto()) {

            List<PhotoSize> photos = update.getMessage().getPhoto();
            String photoId = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();
            int photoWidth = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getWidth();
            // Know photo height
            int photoHeight = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getHeight();
            // Set photo caption
            String caption = "file_id: " + photoId + "\nwidth: " + Integer.toString(photoWidth) + "\nheight: " + Integer.toString(photoHeight);
            SendPhoto msg = new SendPhoto()
                    .setChatId(chatId)
                    .setPhoto(photoId)
                    .setCaption(caption);
            try {
                sendPhoto(msg); // Call method to send the photo with caption
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else{
            sendTextMessage("Не треба сумувати, в холодильничку для Вас є щось смачненьке:\n" +
                    MessageBuilder.buildMenuMessage(), chatId);
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

    private void sendPhotoMessage(Product product, long chatId){
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

    public String getBotUsername() {
        return "SmallTalkingBot";
    }

    public String getBotToken() {
        return "544269816:AAF1lKlnu2Sh-5tmxM-OW4q8KjquwQ4DRMc";
    }
}
