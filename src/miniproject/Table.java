package miniproject;

import java.util.Arrays;

class Table {
    private Menu[] orderList;	//주문 목록을 배열 형태로 저장
    private int orderCount;	//주문 수 저장

    public Table() {
        orderList = new Menu[100]; // 최대 100개의 주문까지 저장 가능
        orderCount = 0;	
    }

    public void addOrder(Menu menu) {
        orderList[orderCount] = menu;
        orderCount++;
    }

    public Menu[] getOrderList() {
        return Arrays.copyOf(orderList, orderCount);
    }
}