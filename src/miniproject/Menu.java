package miniproject;

import java.io.*;

class Menu implements Serializable{
    public String name;
    public double price;

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
    
    public void setPrice(double price) {
        this.price = price; // setPrice 메서드로 메뉴 가격을 수정
    }
    
    // Menu 객체를 파일로 저장
    public void saveMenu(DataOutputStream dos) throws Exception {
        dos.writeUTF(name); // 메뉴 이름 저장
        dos.writeDouble(price); // 메뉴 가격 저장
    }

    // 파일에서 Menu 객체를 읽어오기
    public void loadMenu(DataInputStream dis) throws Exception {
    	this.name = dis.readUTF(); // 메뉴 이름 읽어오기
    	this.price = dis.readDouble(); // 메뉴 가격 읽어오기
    }
    
    // 메뉴 객체 직렬화
    public void saveMenuObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this);
    }

    // 메뉴 객체 역직렬화
    public void loadMenuObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	Menu menu = (Menu) in.readObject(); // 메뉴 객체를 역직렬화하여 읽어옴
        this.name = menu.name;
        this.price = menu.price;
    }
    
    // equals함수 재정의
 	public boolean equals(Object object) {
 		Menu menu = (Menu) object;
     	if (name.equals(menu.getName()))
     		return true;
     	else
     		return false;
     }

    
    @Override
    public String toString() {
        return "메뉴 이름: " + name + ", 가격: " + price + "원";
    }

}