package ua.com.integrity.smalltalkingbot.utils;

import ua.com.integrity.smalltalkingbot.repository.ProductRepository;

import java.nio.charset.StandardCharsets;

public class CommonUtils {
    public static Integer extractProductId(String productIdCommand){
        return Integer.valueOf(productIdCommand.substring(1));
    }

    public static boolean isValidProductId(String textCommand){
        String productId = textCommand.substring(1);
        return isNumeric(productId) && ProductRepository.getInstance().getProducts().containsKey(Integer.valueOf(productId));
    }

    public static boolean isNumeric(String str)    {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isValidPhoneNumber(String str){
        return str.matches("^\\+380\\d{9}$");
    }

    public static String getUTF8String(String str){
        return new String ( str.getBytes(StandardCharsets.UTF_8),StandardCharsets.UTF_8);
    }
}
