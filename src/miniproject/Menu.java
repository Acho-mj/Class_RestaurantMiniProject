package miniproject;

import java.io.Serializable;

class Menu implements Serializable{
    private String name;
    private double price;

    public Menu(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
    
    // equals 정의할 것
    public boolean equals(Menu m) {
        if (this.getName().equals(m.getName())) {
            return true;
        } else {
            return false;
        }
    }

}