package miniproject;

public class Restaurant {
    private Menu[] menuList;
    int max = 100;

    public Restaurant() {
        menuList = new Menu[max]; //메뉴 100개로 제한
        
        menuList[0] = new Menu("햄버거", 5600);
        menuList[1] = new Menu("피자", 12000);
        menuList[2] = new Menu("마라탕", 10000);
        menuList[3] = new Menu("닭갈비", 15000);
        menuList[4] = new Menu("떡볶이", 14000);
    }

    public Menu[] getMenuList() {
        return menuList;
    }
}