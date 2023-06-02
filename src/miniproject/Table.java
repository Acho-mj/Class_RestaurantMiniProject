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
    public void addOrder(Menu menu, int quantity) {
        Order order = new Order(menu.getName(), menu.getPrice(), quantity);
        orderList[orderCount++] = order;
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