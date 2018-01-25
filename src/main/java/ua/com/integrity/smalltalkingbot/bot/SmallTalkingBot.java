package ua.com.integrity.smalltalkingbot.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.com.integrity.smalltalkingbot.message.MessageBuilder;

public class SmallTalkingBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String outputMessage;

            if (message_text.equals("/start")) {
                // User send /start
                outputMessage = "Доброго дня!\n" +
                        "Ласкаво просимо до SmallTalking.\n" +
                        "Сьогоднішнє меню:\n" + MessageBuilder.buildGastroPrognosis();
            }else if(message_text.length() > 6 && message_text.substring(0,3).equals("/id")){
                Integer productId = Integer.valueOf(message_text.substring(3));
                outputMessage = MessageBuilder.getProductDescription(productId);

                if(outputMessage ==null){
                    outputMessage = "Продукт з таким id не знайдено :(\n" +
                            "Спробуйте вибрати щось інше:\n";
                }
                outputMessage = outputMessage + "\nНазад до меню (/menu)";
            }else if (message_text.equals("/menu")) {
                outputMessage = "Сьогоднішнє меню:\n" + MessageBuilder.buildGastroPrognosis();
            }else{
                outputMessage = "Не треба сумувати, в холодильничку для Вас є щось смачненьке:\n" +
                        MessageBuilder.buildGastroPrognosis();
            }

            SendMessage message = new SendMessage() // Create a message object
                    .setChatId(chat_id)
                    .setText(outputMessage);
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    public String getBotUsername() {
        return "SmallTalkingBot";
    }

    public String getBotToken() {
        return "544269816:AAF1lKlnu2Sh-5tmxM-OW4q8KjquwQ4DRMc";
    }
}
