package miniproject;

import java.io.Serializable;

class Order extends Menu implements Serializable {
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
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    // 주문의 가격을 계산하여 반환
    public double pay() {
        return getPrice() * quantity;
    }

}
