package miniproject;

import java.util.Arrays;

class Table {
    private Order[] orderList;	//주문 목록을 배열 형태로 저장
    private int orderCount;	//주문 수 저장
    private String tableName;  //테이블 이름
    
    public Table(String tableName) {
    	this.tableName = tableName;
        this.orderList = new Order[100]; // 최대 100개의 주문까지 저장 가능
        this.orderCount = 0;	
    }
    
    public String getTableName() {
        return tableName;
    }
    
//    public void addOrder(Menu menu) {
//    	if (orderCount < orderList.length) {
//            orderList[orderCount] = menu;
//            orderCount++;
//        } else {
//            System.out.println("더 이상 주문을 추가할 수 없습니다.");
//        }
//    }
    public void addOrder(Menu menu, int quantity) {
        if (orderCount < orderList.length) {
        	Order order = new Order(menu.getName(), menu.getPrice(), quantity);
            orderList[orderCount] = order;
            orderCount++;
        }
    }

    public Order[] getOrderList() {
        return orderList;
    }
    
    public static int[] showAvailableTables(Table[] tableList) {
    	int[] availableTables = new int[tableList.length];
        int count = 0;

        for (Table table : tableList) {
            if (table != null) {
                availableTables[count] = Integer.parseInt(table.getTableName());
                count++;
            }
        }
        return Arrays.copyOf(availableTables, count);
    }
   
    
    // equals 정의할 것
    public boolean equals(Table t) {
    	if (this.tableName == t.getTableName())
    		return true;
    	else
    		return false;
    }
}