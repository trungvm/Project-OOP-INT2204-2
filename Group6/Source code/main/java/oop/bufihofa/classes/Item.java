package oop.bufihofa.classes;


public class Item {
    private String name = "?";
    private double price = 1;
    private double quantity = 1;
    private int id = 1000000000;
    private String unit = "?";
    private BasicDate date = new BasicDate(1,1,2000);

    //constructor
    public Item(){}
    public Item(String s){

    }
    public Item(String name, int id, double price, double quantity, String unit) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
    }

    // getter and setter


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String toData() {
        return "Item{" + "name=" + name + ", price=" + price + ", quantity=" + quantity + ", id=" + id + ", unit=" + unit + ", date=" + date.toData() + '}';
    }
}
