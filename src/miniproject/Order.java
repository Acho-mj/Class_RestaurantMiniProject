package miniproject;

class Order extends Menu{
    private int quantity;

    public Order(String name, double price, int quantity) {
        super(name, price);
        this.quantity = quantity;
    }
    
    public Menu getMenu() {
        return this;
    }

    public int getQuantity() {
        return quantity;
    }
}
