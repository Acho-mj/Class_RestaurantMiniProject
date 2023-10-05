package miniproject;

import java.io.*;

class Menu{
    private String name;
    private double price;

    public Menu(String name, double price) {
        this.name = name;
        this.price = price;
    }
    public Menu() {
        // 기본 생성자
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
    
    // Menu 객체를 파일로 저장
    public void saveMenu(DataOutputStream dos) throws IOException {
        dos.writeUTF(name); // 메뉴 이름 저장
        dos.writeDouble(price); // 메뉴 가격 저장
    }

    // 파일에서 Menu 객체를 읽어오기
    public Menu loadMenu(DataInputStream dis) throws IOException {
        String name = dis.readUTF(); // 메뉴 이름 읽어오기
        double price = dis.readDouble(); // 메뉴 가격 읽어오기
        return new Menu(name, price);
    }
    
    // equals 정의할 것
    public boolean equals(Menu m) {
        if (this.getName() == null || m.getName() == null) {
            return false;
        }
        return this.getName().equals(m.getName());
    }

    
    @Override
    public String toString() {
        return "메뉴 이름: " + name + ", 가격: " + price + "원";
    }

}