package miniproject;

import java.io.*;
import java.util.ArrayList;

public class Restaurant implements Serializable{
    private ArrayList<Menu> menuList;
    private ArrayList<Table> tableList;

    public Restaurant() {
        menuList = new ArrayList<>();
        tableList = new ArrayList<>();
    }
    
    public Restaurant(File file) throws Exception {
    	if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                Restaurant deserializedRestaurant = (Restaurant) inputStream.readObject();
                // 복원된 객체를 현재 객체로 설정
                this.menuList = deserializedRestaurant.menuList;
                this.tableList = deserializedRestaurant.tableList;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일에서 데이터를 읽어오는 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // 메뉴 추가
    public void addMenu(String name, double price) {
    	// 이미 존재하는 메뉴인지 확인
        if (getMenu(name) != null) {
            throw new IllegalArgumentException("이미 존재하는 메뉴 이름입니다. 다른 이름을 입력해주세요.");
        }
        Menu menu = new Menu(name, price);
        menuList.add(menu);
    }

    // 메뉴 삭제
    public void deleteMenu(String deleteMenuName) {
        Menu menuToDelete = getMenu(deleteMenuName);

        if (menuToDelete == null) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 이름입니다. 다른 이름을 입력해주세요.");
        }

        if (!menuList.remove(menuToDelete)) {
            throw new IllegalStateException("메뉴 삭제에 실패했습니다.");
        }
    }


    // 메뉴 가져오기
    public Menu getMenu(String name) {
        for (Menu menu : menuList) {
            if (menu.getName().equals(name)) {
                return menu;
            }
        }
        return null;
    }

    // 테이블 추가
    public void addTable(String tableName, int capacity) {
    	Table existingTable = getTable(tableName);
        if (existingTable != null) {
            throw new IllegalArgumentException("이미 존재하는 테이블입니다. 다시 입력해주세요.");
        }

        Table newTable = new Table(tableName, capacity);
        tableList.add(newTable);
    }

    // 테이블 삭제
    public void deleteTable(String removeTableName) {
        Table tableToRemove = getTable(removeTableName);

        if (tableToRemove == null) {
            throw new IllegalArgumentException("존재하지 않는 테이블 이름입니다. 다른 이름을 입력해주세요.");
        }

        if (!tableList.remove(tableToRemove)) {
            throw new IllegalStateException("테이블 삭제에 실패했습니다.");
        }
    }


    // 테이블 가져오기
    public Table getTable(String tableName) {
        for (Table table : tableList) {
            if (table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }

    // 테이블 이름으로 테이블 찾기
    public Table findTableByName(String tableName) {
        for (Table table : tableList) {
            if (table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }
    
    
    // 주문하기
    public void takeOrder(Table table, Menu orderMenu, int quantity) {
        if (table == null) {
            throw new IllegalArgumentException("존재하지 않는 테이블입니다. 다시 입력해주세요.");
        }

        if (table.availableTables()) {
            throw new IllegalStateException("이미 주문을 받은 테이블입니다. 다른 테이블을 선택해주세요.");
        }

        int tableCapacity = table.getCapacity();
        if (quantity > tableCapacity) {
            throw new IllegalArgumentException("테이블 수용 인원인 " + tableCapacity + "명을 초과하였습니다. 다른 테이블을 선택해주세요.");
        }

        Order order = new Order(orderMenu.getName(), orderMenu.getPrice(), quantity);
        table.addOrder(order);
    }

    
    // 체크아웃하기
    public boolean checkOutTable(String tableName) {
        Table checkoutTable = getTable(tableName);

        if (checkoutTable == null) {
            throw new IllegalArgumentException("존재하지 않는 테이블입니다. 다시 입력해주세요.");
        }

        checkoutTable.clearOrderList();
        return true;
    }


    public ArrayList<Menu> getMenuList() {
        return new ArrayList<>(menuList);
    }

    public ArrayList<Table> getTableList() {
        return new ArrayList<>(tableList);
    }

    
    // 객체를 파일로 출력
    public void saveFile(DataOutputStream dos) throws Exception{
        // 메뉴 목록 저장
        dos.writeInt(menuList.size());
        for (Menu menu : menuList) {
        	menu.saveMenu(dos);
        }

        // 테이블 목록 저장
        dos.writeInt(tableList.size()); 
        for (Table table : tableList) {
             table.saveTable(dos);
        }
        
    }

    // 파일에서 객체로 읽어오기
    public void loadFile(DataInputStream dis) throws Exception{
    	menuList = new ArrayList<Menu>();
        tableList = new ArrayList<Table>();
    	
    	try {
            int menuCount = dis.readInt();

            // 메뉴 객체들을 다시 ArrayList에 저장
            menuList.clear();
            for (int i = 0; i < menuCount; i++) {
                Menu menu = new Menu();
                menu.loadMenu(dis);
                menuList.add(menu);
            }

            int tableCount = dis.readInt();

            // 테이블 객체들을 다시 ArrayList에 저장
            tableList.clear();
            for (int i = 0; i < tableCount; i++) {
                Table table = new Table();
                table.loadTable(dis);
                tableList.add(table);
            }
        } catch (EOFException e) {
            // EOFException 발생 시, 파일의 끝에 도달한 것으로 처리
            System.out.println("파일의 끝에 도달하였습니다.");
        }
    }
    
    // 객체 직렬화 -> 데이터 저장하기 
 	public void saveData(Restaurant restaurant) {
         try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("restaurant.dat"))) {
             outputStream.writeObject(restaurant); // 레스토랑 객체를 직렬화하여 파일에 저장
             
         } catch (IOException e) {
        	 e.printStackTrace();
        
         }
     }
 	
 	// 객체 역직렬화 -> 데이터 불러오기
 	public Restaurant loadData() {
 	    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("restaurant.dat"))) {
 	        Restaurant deserializedRestaurant = (Restaurant) inputStream.readObject(); 
 	        
 	        return deserializedRestaurant;
 	    } catch (IOException | ClassNotFoundException e) {
 	        System.out.println(e.getMessage());
 	        return null; 
 	    }
 	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n==== 메뉴 목록 ====\n");
        for (Menu menu : menuList) {
            sb.append("메뉴 이름: ").append(menu.getName()).append(", 가격: ").append(menu.getPrice()).append("원\n");
        }
        sb.append("\n==== 테이블 목록 ==== \n");
        for (Table table : tableList) {
            sb.append(table.getTableName()).append(" (수용인원: ").append(table.getCapacity()).append(")\n");

            // 테이블 주문 목록 표시
            ArrayList<Order> orders = table.getOrderList();
            double totalPay = table.totalPay();
            sb.append("   <주문 목록>\n");
            for (Order order : orders) {
                sb.append("    → ").append(order.getMenu().getName()).append(" x")
                        .append(order.getQuantity()).append(": " + order.pay() + "원").append("\n");
            }
            sb.append("    총 주문 금액: ").append(totalPay + "원\n").append("\n");
        }
        return sb.toString();
    }
}
