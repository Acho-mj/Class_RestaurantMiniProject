package miniproject;

import java.io.*;

class Order extends Menu {
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
    
    // Order 객체를 파일로 저장
    public void saveOrder(DataOutputStream dos) throws IOException {
        dos.writeUTF(getName()); // 메뉴 이름 저장
        dos.writeDouble(getPrice()); // 메뉴 가격 저장
        dos.writeInt(quantity); // 수량 저장
    }

    // 파일에서 Order 객체를 읽어오기
    public static Order loadOrder(DataInputStream dis) throws IOException {
        String name = dis.readUTF(); // 메뉴 이름 읽어오기
        double price = dis.readDouble(); // 메뉴 가격 읽어오기
        int quantity = dis.readInt(); // 수량 읽어오기
        return new Order(name, price, quantity);
    }
}
