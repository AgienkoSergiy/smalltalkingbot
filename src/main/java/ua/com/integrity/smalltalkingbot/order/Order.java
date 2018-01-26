package ua.com.integrity.smalltalkingbot.order;

import com.liqpay.LiqPay;

import java.util.HashMap;
import java.util.UUID;

public class Order {

    private Long id;
    private String chatId;
    private Integer productId;
    private String companyPublicId;
    private String companyPrivateKey;


    //TODO remove one of them

    public String callLiqPayApi(String phoneNumber, Integer userId){

        LiqPay liqpay = new LiqPay( "i52327802934", "UcuUoBuTYbLvbXUkXm7JFcPfzvRIF2u6pjaJ4G1I");
        try {
            HashMap<String, Object> res = (HashMap<String, Object>)liqpay.api("request", getOrderParams(phoneNumber, userId));
            //TODO remove this foreach after test
            for (String key :
                    res.keySet()) {
                System.out.println(key + ": " + res.get(key));
            }
            if ("ok".equals(res.get("result").toString())){
                return "";
            }else{
                return "Сталася прикра помилка :(";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "Сталася прикра помилка :(";
    }

    public String callLiqPayApi(String phoneNumber/*, Integer userId*/){

        LiqPay liqpay = new LiqPay( "i52327802934", "UcuUoBuTYbLvbXUkXm7JFcPfzvRIF2u6pjaJ4G1I");
        try {
            HashMap<String, Object> res = (HashMap<String, Object>)liqpay.api("request", getOrderParams(phoneNumber));
            //TODO remove this foreach after test
            for (String key :
                    res.keySet()) {
                System.out.println(key + ": " + res.get(key));
            }
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

    private HashMap<String, String> getOrderParams(String phoneNumber, Integer userId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("currency", "UAH");
        params.put("amount", "1");
        params.put("description", "Small Talking food test");
        params.put("account", userId.toString());
        params.put("channel_type", "telegram");
        params.put("version", "3");
        params.put("phone", phoneNumber);
        params.put("order_id", UUID.randomUUID().toString());
        params.put("action", "invoice_bot");
        params.put("sandbox", "1");

        return params;
    }

    private HashMap<String, String> getOrderParams(String phoneNumber){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("currency", "UAH");
        params.put("amount", "1");
        params.put("description", "Small Talking food refrigerator");
        //params.put("account", userId.toString());
        params.put("channel_type", "telegram");
        params.put("version", "3");
        params.put("phone", phoneNumber);
        params.put("order_id", UUID.randomUUID().toString());
        params.put("action", "invoice_bot");
        params.put("sandbox", "1");

        return params;
    }
}
