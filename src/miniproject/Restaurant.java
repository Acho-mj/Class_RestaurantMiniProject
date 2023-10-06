package miniproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private ArrayList<Menu> menuList;
    private ArrayList<Table> tableList;

    public Restaurant() {
        menuList = new ArrayList<>();
        tableList = new ArrayList<>();
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
    public void saveFile(File file) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            // 메뉴 목록 저장
            dos.writeInt(menuList.size()); // ArrayList의 크기 사용
            for (Menu menu : menuList) {
                menu.saveMenu(dos);
            }

            // 테이블 목록 저장
            dos.writeInt(tableList.size()); // ArrayList의 크기 사용
            for (Table table : tableList) {
                table.saveTable(dos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 객체로 읽어오기
    public void loadFile(File file) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            int loadedMenuCount = dis.readInt();

            // 메뉴 객체들을 다시 ArrayList에 저장
            menuList.clear();
            for (int i = 0; i < loadedMenuCount; i++) {
                menuList.add(new Menu().loadMenu(dis));
            }

            int loadedTableCount = dis.readInt();

            // 테이블 객체들을 다시 ArrayList에 저장
            tableList.clear();
            for (int i = 0; i < loadedTableCount; i++) {
                tableList.add(new Table().loadTable(dis, this));
            }
        } catch (EOFException e) {

        } catch (IOException e) {
            e.printStackTrace();
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
