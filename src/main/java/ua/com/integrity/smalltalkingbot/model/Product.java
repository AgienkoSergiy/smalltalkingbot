package ua.com.integrity.smalltalkingbot.model;

public class Product {

    private Integer id;
    private String name;
    private Double price;
    private String category;
    private String ingredients;
    private Double protein;
    private Double fat;
    private Double carbohydrate;
    private Double caloricity;
    private Integer weight;
    private String picId;

    public Product(Integer id, String name, Double price, String category, String ingredients, Double protein, Double fat, Double carbohydrate, Double caloricity, Integer weight, String picId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.ingredients = ingredients;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.caloricity = caloricity;
        this.weight = weight;
        this.picId = picId;
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

    public String getIngredients() {
        return ingredients;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }


    public String getDetails() {
        return  "Категорія: " + category + "\n" +
                "Інгредієнти: " + ingredients + "\n\n" +
                "Білки: " + protein + " г\n" +
                "Жири: " + fat + " г\n" +
                "Вуглеводи: " + carbohydrate + " г\n" +
                "Калорійність блюда: " + caloricity + " ккал\n" +
                "Вага: " + weight + " г";
    }
}
