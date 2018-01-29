package ua.com.integrity.smalltalkingbot.message;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.com.integrity.smalltalkingbot.model.Product;
import ua.com.integrity.smalltalkingbot.repository.ProductRepository;
import ua.com.integrity.smalltalkingbot.util.CommonUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageBuilder {


    public static String buildMenuMessage(Collection <Product> products){
        StringBuffer textMessage = new StringBuffer();

        for (Product product:
                products) {
            textMessage = textMessage.append(product.getName())
                    .append("(натисни тут \u27A1 /").append(product.getId()).append(") - ")
                    .append(product.getPrice().intValue()).append(" грн\n\n");
        }

        return textMessage.toString();
    }

    public static String getTimeoutMessage(){
        return "Дякуємо, що звернулися до нашого сервісу.\n" +
                "Всього найкращого.";
    }

    public static String getAcceptedOrderMessage(){
        return "Ваше замовлення прийняте, зачекайте будь ласка.";
    }

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

    public static void addButton(ReplyKeyboardMarkup keyboardMarkup, Integer rowNumber, String buttonText){
        KeyboardRow row = keyboardMarkup.getKeyboard().get(rowNumber);
        row.add(buttonText);
    }

}
