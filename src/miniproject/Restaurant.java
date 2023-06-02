package miniproject;

public class Restaurant {
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
    


    public Menu[] getMenuList() {
        return menuList;
    }

    public Table[] getTableList() {
        return tableList;
    }
    
    public void printMenuList() {
        for (int i = 0; i < menuCount; i++) {
            Menu menu = menuList[i];
            if (menu != null) {
                System.out.println(i + 1 + ". " + menu.getName() + " : " + menu.getPrice());
            }
        }
    }
    
    public void printTableList() {
        for (int i = 0; i < tableCount; i++) {
            Table table = tableList[i];
            if (table != null) {
                System.out.println(i + 1 + ". " + table.getTableName());
            }
        }
    }
    
}
