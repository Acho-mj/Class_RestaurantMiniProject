package miniproject;

import java.io.*;
import java.util.ArrayList;

class Table implements Serializable{
	private String tableName;
    private int capacity;
    private ArrayList<Order> orderList; // 주문 목록을 ArrayList로 변경
    private boolean isAvailable; // 상태를 나타내는 필드

    public Table(String tableName, int capacity) {
        this.tableName = tableName;
        this.capacity = capacity;
        this.orderList = new ArrayList<>(); // ArrayList로 초기화
        this.isAvailable = true; // 기본적으로 "이용 가능"
    }
    
    public Table() {
    	orderList = new ArrayList<>();
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
                isAvailable = false; 
                return;
            }
        }

        // 중복된 메뉴가 없는 경우 새로운 주문을 추가
        orderList.add(order);
        isAvailable = false;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    // 테이블 배정 완료시
    public void tableCompleted() {
        isAvailable = false;
    }

    // 테이블 체크아웃시
    public void guestDeparted() {
        // 손님이 떠났을 때 테이블을 "이용 가능" 상태로 변경
        isAvailable = true;
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
    
    // 오더 객체를 파일에 출력
 	void saveOrder(DataOutputStream dos) throws Exception{
 		dos.writeInt(orderList.size());	// 오더 수 
 		// 반복을 통해 menus[]에 저장된 메뉴 객체들의 요소를 프리미티브 타입으로 파일에 출력
 		for (int i=0; i<orderList.size(); i++) {
 			orderList.get(i).saveOrder(dos);
 		}
 	}
 		
 	// 저장된 테이블 객체 정보 입력
 	void loadOrder(DataInputStream dis) throws Exception{
 		int orderSize = dis.readInt();	// 주문 메뉴 종류 수 
 		// 반복을 통해 파일에 저장된 오더 객체 정보를 새로운 오더 객체를 생성하여 정보 입력 후 orders[]에 추가
 		for (int i=0; i<orderSize; i++) {
 			Order order = new Order();	// 오더 객체 생성
 			order.loadOrder(dis);	// 오더 정보 입력
 			orderList.add(order);	// 오더 배열에 추가
 		}
 	}
    
    // Table 객체를 파일로 저장
    public void saveTable(DataOutputStream dos) throws Exception {
        dos.writeUTF(tableName); // 테이블 이름 저장
        dos.writeInt(capacity); // 수용 인원 저장
        saveOrder(dos);
    }
    
    public void loadTable(DataInputStream dis) throws Exception {
        tableName = dis.readUTF(); // 테이블 이름 읽어오기
        capacity = dis.readInt(); // 수용 인원 읽어오기
        loadOrder(dis);
    }
    
    
    // 테이블 객체 직렬화
    public void saveTableObject(ObjectOutputStream out) throws IOException {
    	out.writeObject(this); // 테이블 객체를 직렬화하여 저장
    	out.writeObject(this.orderList);
    }

    // 테이블 객체 역직렬화
    public void loadTableObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	Table table = (Table) in.readObject(); // 테이블 객체를 역직렬화하여 읽어옴
    	this.orderList = (ArrayList<Order>) in.readObject();
        this.tableName = table.tableName;
        this.capacity = table.capacity;
        this.orderList = table.orderList;
        this.isAvailable = table.isAvailable;
    }
    
    // equals함수 재정의
 	public boolean equals(Object object) {
 		Table table = (Table) object;
 		// 만약 같으면, true를 
 	    if (this.tableName.equals(table.getTableName()))
 	    	return true;
 	    // 다르면 false를 반환 
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