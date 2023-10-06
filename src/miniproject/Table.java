package miniproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Table {
	private String tableName;
    private int capacity;
    private ArrayList<Order> orderList; // 주문 목록을 ArrayList로 변경

    public Table(String tableName, int capacity) {
        this.tableName = tableName;
        this.capacity = capacity;
        this.orderList = new ArrayList<>(); // ArrayList로 초기화
    }
    
    public Table() {
        // 기본 생성자
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
        for (Order existingOrder : orderList) {
            if (existingOrder.getMenu().equals(menu)) {
                existingOrder.setQuantity(existingOrder.getQuantity() + quantity);
                return;
            }
        }

        // 중복된 메뉴가 없는 경우 새로운 주문을 추가
        orderList.add(order);
    }

    
    // 각 테이블의 주문 수 반환
    public int getOrderCount() {
        return orderList.size(); // 주문 수를 List의 크기로 반환
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }
    
    public void clearOrderList() {
        orderList = new ArrayList<Order>();
    }

    public boolean availableTables() {
        return !orderList.isEmpty(); // 주문 목록이 비어있지 않으면 true 반환
    }
    
    // 주문 목록의 총 결제 금액을 계산하여 반환
    public double totalPay() {
        double total = 0.0;

        for (Order order : orderList) {
            total += order.pay();
        }

        return total;
    }
    
    // Table 객체를 파일로 저장
    public void saveTable(DataOutputStream dos) throws IOException {
        dos.writeUTF(tableName); // 테이블 이름 저장
        dos.writeInt(capacity); // 수용 인원 저장
        
        // 주문 정보 저장
        dos.writeInt(orderList.size()); // 현재 주문 수 저장
        for (Order order : orderList) {
        	order.saveOrder(dos);
        }
    }
    
    public Table loadTable(DataInputStream dis, Restaurant restaurant) throws IOException {
        String tableName = dis.readUTF(); // 테이블 이름 읽어오기
        int capacity = dis.readInt(); // 수용 인원 읽어오기
        int orderCount = dis.readInt(); // 주문 수 읽어오기

        // 레스토랑 객체에서 해당 테이블 이름을 가진 테이블 찾기
        Table table = restaurant.findTableByName(tableName);

        if (table == null) {
            // 테이블이 null인 경우, 새로운 테이블 객체 생성
            table = new Table(tableName, capacity);
        }

        // 주문 목록 읽어오기
        for (int i = 0; i < orderCount; i++) {
            Order order = new Order(); // Order 인스턴스 생성
            order.loadOrder(dis); // loadOrder 호출
            table.addOrder(order); // 주문 추가
        }

        return table;
    }
    
    // equals 정의할 것
    public boolean equals(Table t) {
    	if (this.tableName == t.getTableName())
    		return true;
    	else
    		return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("테이블 이름: ").append(getTableName()).append(" (수용인원: ").append(getCapacity()).append(")\n");

        for (Order order : orderList) {
            sb.append("    → ").append(order.getMenu().getName()).append(" x")
                    .append(order.getQuantity()).append(": " + order.pay() + "원").append("\n");
        }

        sb.append("    총 주문 금액: ").append(totalPay() + "원").append("\n\n");

        return sb.toString();
    }
}