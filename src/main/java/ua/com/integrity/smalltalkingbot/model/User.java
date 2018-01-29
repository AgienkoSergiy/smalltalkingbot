package ua.com.integrity.smalltalkingbot.model;

public class User {

    private Long chatId;
    private String proneNumber;
    private Double caloricityLimit;

    public User (){

    }

    public User(Long chatId) {
        this.chatId = chatId;
    }

    public User(Long chatId, String proneNumber, Double caloricityLimit) {
        this.chatId = chatId;
        this.proneNumber = proneNumber;
        this.caloricityLimit = caloricityLimit;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getProneNumber() {
        return proneNumber;
    }

    public void setProneNumber(String proneNumber) {
        this.proneNumber = proneNumber;
    }

    public Double getCaloricityLimit() {
        return caloricityLimit;
    }

    public void setCaloricityLimit(Double caloricityLimit) {
        this.caloricityLimit = caloricityLimit;
    }
}
