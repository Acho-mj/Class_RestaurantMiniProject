package miniproject;

import java.io.*;
import java.util.ArrayList;

public class Restaurant {
    private ArrayList<Menu> menuList;
    private ArrayList<Table> tableList;

    public Restaurant() {
        menuList = new ArrayList<>();
        tableList = new ArrayList<>();
    }
    
    public Restaurant(File file) throws Exception {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            loadFile(dis); // 파일에서 데이터를 읽는 메서드 호출
        } catch (IOException e) {
            System.err.println("파일에서 데이터를 읽어오는 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 메뉴 추가
    public void addMenu(String name, double price) {
        Menu menu = new Menu(name, price);
        menuList.add(menu);
    }

    // 메뉴 삭제
    public boolean deleteMenu(String name) {
        for (Menu menu : menuList) {
            if (menu.getName().equals(name)) {
                menuList.remove(menu);
                return true;
            }
        }
        return false;
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
        Table table = new Table(tableName, capacity);
        tableList.add(table);
    }

    // 테이블 삭제
    public boolean deleteTable(String name) {
        for (Table table : tableList) {
            if (table.getTableName().equals(name)) {
                tableList.remove(table);
                return true;
            }
        }
        return false;
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
