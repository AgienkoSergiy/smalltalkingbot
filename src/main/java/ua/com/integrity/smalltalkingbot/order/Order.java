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
            for (String key :
                    res.keySet()) {
                System.out.println(key + ": " + res.get(key));
            }
            System.out.println(res.get("result"));
            return res.get("result").toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Сталася прикра помилка :(";
    }

    public String callLiqPayApi(String phoneNumber/*, Integer userId*/){

        LiqPay liqpay = new LiqPay( "i52327802934", "UcuUoBuTYbLvbXUkXm7JFcPfzvRIF2u6pjaJ4G1I");
        try {
            HashMap<String, Object> res = (HashMap<String, Object>)liqpay.api("request", getOrderParams(phoneNumber));
            for (String key :
                    res.keySet()) {
                System.out.println(key + ": " + res.get(key));
            }
            System.out.println(res.get("result"));
            return res.get("result").toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Сталася прикра помилка :(";
    }

    private HashMap<String, String> getOrderParams(String phoneNumber, Integer userId){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("currency", "UAH");
        params.put("amount", "1");
        params.put("description", "За їжу");
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
        params.put("description", "За їжу");
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
