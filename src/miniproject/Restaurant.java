package miniproject;

import java.io.*;
import java.util.*;

public class Restaurant{
    private Menu[] menuList;
    private Table[] tableList;

    private int menuCount;
    private int tableCount;
        
    public Restaurant() {
    	menuList = new Menu[10]; 
        tableList = new Table[10];
        menuCount = 0;
        tableCount = 0;
    }

    // 메뉴 추가 
    public void addMenu(String name, double price) {
        Menu menu = new Menu(name, price);
        menuList[menuCount] = menu;
        menuCount++;
    }
    
    // 메뉴 삭제 
    public boolean deleteMenu(String name) {
        for (int i = 0; i < menuCount; i++) {
            Menu menu = menuList[i];
            if (menu.getName().equals(name)) {
                // 해당 인덱스 이후의 메뉴들을 한 칸씩 앞으로 당김
                for (int j = i; j < menuCount - 1; j++) {
                    menuList[j] = menuList[j + 1];
                }
                menuList[menuCount - 1] = null; // 마지막 인덱스 null로 초기화
                menuCount--;
                return true;
            }
        }
        return false;
    }
    
    public Menu getMenu(String name) {
        for (int i = 0; i < menuCount; i++) {
            Menu menu = menuList[i];
            if (menu.getName().equals(name)) {
                return menu;
            }
        }
        return null;
    }


    public void addTable(String tableName, int capacity) {
        Table table = new Table(tableName, capacity);
        tableList[tableCount] = table;
        tableCount++;
    }

    public boolean deleteTable(String name) {
        for (int i = 0; i < tableCount; i++) {
            Table table = tableList[i];
            if (table.getTableName().equals(name)) {
                // 해당 인덱스 이후의 테이블들을 한 칸씩 앞으로 당김
                for (int j = i; j < tableCount - 1; j++) {
                    tableList[j] = tableList[j + 1];
                }
                tableList[tableCount - 1] = null; // 마지막 인덱스 null로 초기화
                tableCount--;
                return true;
            }
        }
        return false;
    }
    
    public Table getTable(String tableName) {
        for (int i = 0; i < tableCount; i++) {
            Table table = tableList[i];
            if (table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }
    
    // 테이블 이름으로 테이블을 찾아 반환
    public Table findTableByName(String tableName) {
        for (Table table : tableList) {
            if (table != null && table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }
    

    public Menu[] getMenuList() {
        return menuList;
    }

    public Table[] getTableList() {
        return tableList;
    }
    
    // 객체를 파일로 출력
    public void saveFile(String filename) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename))) {
            // 메뉴 목록 저장
            dos.writeInt(menuCount);
            for (int i = 0; i < menuCount; i++) {
                menuList[i].saveMenu(dos);
            }

            // 테이블 목록 저장
            dos.writeInt(tableCount);
            for (int i = 0; i < tableCount; i++) {
                tableList[i].saveTable(dos);
            }

            // 주문 목록 저장
            for (int i = 0; i < tableCount; i++) {
                Table table = tableList[i];
                if (table != null && table.getOrderCount() > 0) {
                    table.saveOrderList(dos);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 객체로 읽어오기
    public void loadFile(String filename) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filename))) {
            int loadedMenuCount = dis.readInt();
            menuCount = loadedMenuCount;

            // 메뉴 객체들을 다시 배열에 저장
            menuList = new Menu[loadedMenuCount];
            for (int i = 0; i < loadedMenuCount; i++) {
                menuList[i] = Menu.loadMenu(dis);
            }

            int loadedTableCount = dis.readInt();
            tableCount = loadedTableCount;

            // 테이블 객체들을 다시 배열에 저장
            tableList = new Table[loadedTableCount];
            for (int i = 0; i < loadedTableCount; i++) {
                tableList[i] = Table.loadTable(dis, this);
            }

            // 주문 목록 읽어오기
            for (int i = 0; i < tableCount; i++) {
                int orderCount = dis.readInt(); // 주문 수량 읽어오기
                Table table = tableList[i];
                if (table != null) {
                    for (int j = 0; j < orderCount; j++) {
                        Order order = Order.loadOrder(dis);
                        table.addOrder(order); // 주문을 테이블의 주문 목록에 추가
                    }
                }
            }
        } catch (EOFException e) {
            // EOFException 처리
            System.out.println("파일의 끝에 도달했습니다.");
        } catch (FileNotFoundException e) {
            // 파일이 존재하지 않는 경우에 대한 예외 처리
            System.out.println("데이터 파일이 존재하지 않습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }

//         // 데이터를 콘솔에 출력
//         System.out.println("==== 메뉴 목록 ====");
//         for (Menu menu : menuList) {
//        	 if (menu != null) {
//        		 System.out.println(menu); // Menu 클래스의 toString() 메서드를 사용하여 출력
//             }
//         }
//
//         System.out.println("\n==== 테이블 목록 ====");
//         for (Table table : tableList) {
//        	 if (table != null) {
//    		   System.out.println(table); // Table 클래스의 toString() 메서드를 사용하여 출력
//        	 }
//         }	
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("==== 메뉴 목록 ====\n");
        for (Menu menu : getMenuList()) {
            if (menu != null) { // null 체크
                sb.append("메뉴 이름: ").append(menu.getName()).append(", 가격: ").append(menu.getPrice()).append("원\n");
            }
        }
        sb.append("==== 테이블 목록 ==== \n");
        for (Table table : getTableList()) {
            if (table != null) { // null 체크
                sb.append(table.getTableName()).append(" (수용인원: ").append(table.getCapacity()).append(")\n");

                // 테이블 주문 목록 표시
                Order[] orders = table.getOrderList();
                double totalPay = table.totalPay();
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
            }
        }
        return sb.toString();
    }
}
