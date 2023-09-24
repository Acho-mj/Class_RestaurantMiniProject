package miniproject;

import java.io.*;

class Table {
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
    
    // 각 테이블의 주문 수 반환
    public int getOrderCount() {
        return orderCount;
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
    
    // Table 객체를 파일로 저장
    public void saveTable(DataOutputStream dos) throws IOException {
        dos.writeUTF(tableName); // 테이블 이름 저장
        dos.writeInt(capacity); // 수용 인원 저장
        
        // 주문 정보 저장
        dos.writeInt(orderCount); // 현재 주문 수 저장
        for (int i = 0; i < orderCount; i++) {
            orderList[i].saveOrder(dos);
        }
    }
    
    public static Table loadTable(DataInputStream dis, Restaurant restaurant) throws IOException {
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
            Order order = Order.loadOrder(dis);
            table.addOrder(order); // 주문 추가
        }

        return table;
    }
    
    // 주문 목록을 파일에 저장하는 메서드
    public void saveOrderList(DataOutputStream dos) throws IOException {
        dos.writeInt(orderCount); // 주문 수량 저장

        // 주문 목록에 있는 각 주문을 파일에 저장
        for (int i = 0; i < orderCount; i++) {
            orderList[i].saveOrder(dos);
        }
    }


    public void loadOrderList(DataInputStream dis) throws IOException {
        // 주문 정보를 데이터 스트림에서 읽어와서 주문 목록에 추가
        int loadedOrderCount = dis.readInt();
        orderCount = loadedOrderCount; // 주문 수 업데이트
        for (int i = 0; i < loadedOrderCount; i++) {
            orderList[i] = Order.loadOrder(dis);
        }
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

        // 테이블 주문 목록 표시
        Order[] orders = getOrderList();
        double totalPay = totalPay();
        if (orders != null) {
            sb.append("   <주문 목록>\n");
            for (Order order : orders) {
                if (order != null) {
                    sb.append("    → ").append(order.getMenu().getName()).append(" x")
                    .append(order.getQuantity()).append(": " + order.getMenu().getPrice() * order.getQuantity()+"원").append("\n");
                }
            }
            sb.append("    총 주문 금액: ").append(totalPay + "원").append("\n\n");
        }

        return sb.toString();
    }
}