package miniproject;

import java.util.Arrays;

class Table {
    private Menu[] orderList;	//주문 목록을 배열 형태로 저장
    private int orderCount;	//주문 수 저장
    private int tableNumber;  //테이블 번호

    public Table(int tableNumber) {
    	this.tableNumber = tableNumber;
        orderList = new Menu[100]; // 최대 100개의 주문까지 저장 가능
        orderCount = 0;	
    }
    
    public static int[] showAvailableTables(Table[] tableList) {
    	int[] availableTables = new int[tableList.length];
        int count = 0;

        for (Table table : tableList) {
            if (table != null) {
                availableTables[count] = table.getTableNumber();
                count++;
            }
        }
        return Arrays.copyOf(availableTables, count);
    }
    
    public int getTableNumber() {
        return tableNumber;
    }

    public void addOrder(Menu menu) {
        orderList[orderCount] = menu;
        orderCount++;
    }

    public Menu[] getOrderList() {
        return Arrays.copyOf(orderList, orderCount);
    }
}