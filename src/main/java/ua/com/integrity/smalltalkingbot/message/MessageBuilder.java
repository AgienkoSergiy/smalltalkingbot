package ua.com.integrity.smalltalkingbot.message;

import org.telegram.telegrambots.api.methods.send.SendInvoice;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.com.integrity.smalltalkingbot.model.Product;
import ua.com.integrity.smalltalkingbot.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {


    public static String buildMenuMessage(){
        StringBuffer textMessage = new StringBuffer();

        for (Product product:
                ProductRepository.getInstance().getProducts().values()) {
            textMessage = textMessage.append(product.getName())
                    .append("(натисни тут -> /").append(product.getId()).append(") - ")
                    .append(product.getPrice().intValue()).append(" грн\n\n");
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
        return "Гастропрогноз на сьогодні:  у нашому кафе-магазині, як завжди, свіжа та смачна їжа. " +
                "В першій половині дня зазирніть на верхні полиці нашого холодильнику - " +
                "там на Вас чекають фірмові сирнички та гранола. " +
                "В обідню перерву сфокусуйтеся на середніх полицях - " +
                "там оберіть, що душа бажає для відновлення сил. " +
                "Ближче до вечора переведіть погляд на нижні полиці - " +
                "там легкі й корисні салати, що нададуть сил на реалізацію вечірніх планів:)";
    }

    public static ReplyKeyboardMarkup showButtonText(String buttonText){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(buttonText);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup showPhoneButton(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();

        keyboardButton.setText("\u260E Надати свій номер телефону").setRequestContact(true);
        row.add(keyboardButton);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        return keyboardMarkup;
    }

    public static void addRowButton(ReplyKeyboardMarkup keyboardMarkup, String buttonText){
        KeyboardRow row = new KeyboardRow();
        row.add(buttonText);
        keyboardMarkup.getKeyboard().add(row);
    }

    public static ReplyKeyboardMarkup showOneTimeButton(String buttonText){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        row.add(buttonText);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        return keyboardMarkup;
    }

}
