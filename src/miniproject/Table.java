package miniproject;

import java.io.Serializable;

class Table implements Serializable {
    private Order[] orderList;	//주문 목록을 배열 형태로 저장
    private int orderCount;	//주문 수 저장
    private String tableName;  //테이블 이름
    private int capacity; // 테이블 수용인원
    
    public Table(String tableName, int capacity) {
        this.tableName = tableName;
        this.capacity = capacity;
        this.orderList = new Order[100];
        this.orderCount = 0;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void addOrder(Order order) {
        Menu menu = order.getMenu();
        int quantity = order.getQuantity();

        // 이미 주문된 메뉴인지 확인하고, 중복된 경우 해당 메뉴의 주문 수량을 누적
        for (int i = 0; i < orderCount; i++) {
            if (orderList[i].getMenu().equals(menu)) {
                orderList[i].setQuantity(orderList[i].getQuantity() + quantity);
                return;
            }
        }

        // 중복된 메뉴가 없는 경우 새로운 주문을 추가
        orderList[orderCount] = order;
        orderCount++;
    }

    public Order[] getOrderList() {
        return orderList;
    }
    
    public void clearOrderList() {
        orderList = new Order[100];
        orderCount = 0;
    }
    
    public boolean availableTables() {
    	return orderCount > 0;
    }
    
    // 주문 목록의 총 결제 금액을 계산하여 반환
    public double totalPay() {
        double total = 0.0;
        Order[] orders = getOrderList(); 
        
        for (Order order : orders) {
            if (order != null) {
                total += order.pay();
            }
        }
        return total;
    }

    // equals 정의할 것
    public boolean equals(Table t) {
    	if (this.tableName == t.getTableName())
    		return true;
    	else
    		return false;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("테이블 이름: ").append(tableName).append("\n");
        sb.append("수용 인원: ").append(capacity).append("\n");
        sb.append("주문 목록:\n");
        for (Order order : orderList) {
            sb.append("메뉴: ").append(order.getMenu()).append(", 수량: ").append(order.getQuantity()).append("\n");
        }
        sb.append("총 금액: ").append(totalPay()).append("\n");
        return sb.toString();
    }
}