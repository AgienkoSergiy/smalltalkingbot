package ua.com.integrity.smalltalkingbot.model;

import java.math.BigDecimal;

public class Product {

    private Integer id;
    private String name;
    private Double price;
    private String category;
    private String description;
    private Double protein;
    private Double fat;
    private Double carbohydrate;
    private Double caloricity;

    public Product(Integer id, String name, Double price, String category, String description, Double protein, Double fat, Double carbohydrate, Double caloricity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.caloricity = caloricity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public Double getCaloricity() {
        return caloricity;
    }

    public void setCaloricity(Double caloricity) {
        this.caloricity = caloricity;
    }


    public String getFullDescription() {
        return "Вибраний товар:\n" + name + "\n" +
                "Ціна: " + price + " грн\n" +
                "Категорія: " + category + "\n" +
                "Опис: " +description + "\n\n" +
                "Білки: " + protein + "\n" +
                "Жири: " + fat + "\n" +
                "Вуглеводи: " + carbohydrate + "\n" +
                "Калорійність: " + caloricity;
    }
}
