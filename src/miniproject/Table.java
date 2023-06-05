package miniproject;

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
    
//    public void addOrder(Menu menu) {
//    	if (orderCount < orderList.length) {
//            orderList[orderCount] = menu;
//            orderCount++;
//        } else {
//            System.out.println("더 이상 주문을 추가할 수 없습니다.");
//        }
//    }
//    public void addOrder(Menu menu, int quantity) {
//        Order order = new Order(menu.getName(), menu.getPrice(), quantity);
//        orderList[orderCount++] = order;
//    }
    
    // 각 메뉴의 주문 수량을 누적 !
    public void addOrder(Menu menu, int quantity) {
        // 이미 주문된 메뉴인지 확인하고, 중복된 경우 해당 메뉴의 주문 수량을 누적합니다.
        for (int i = 0; i < orderCount; i++) {
            if (orderList[i].getMenu().equals(menu)) {
                orderList[i].setQuantity(orderList[i].getQuantity() + quantity);
                return;
            }
        }

        // 중복된 메뉴가 없는 경우 새로운 주문을 추가합니다.
        Order newOrder = new Order(menu.getName(), menu.getPrice(), quantity);
        orderList[orderCount] = newOrder;
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
    

    // equals 정의할 것
    public boolean equals(Table t) {
    	if (this.tableName == t.getTableName())
    		return true;
    	else
    		return false;
    }
}