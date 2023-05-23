package miniproject;

public class Restaurant {
    private Menu[] menuList;
    int max = 100;
    
    
    public Restaurant() {
        menuList = new Menu[max]; //메뉴 100개로 제한
        
        menuList[0] = new Menu("햄버거", 5600);
        menuList[1] = new Menu("피자", 12000);
        menuList[2] = new Menu("마라탕", 10000);
        menuList[3] = new Menu("닭갈비", 15000);
        menuList[4] = new Menu("떡볶이", 14000);
    }
    
    // 메뉴 목록 반환
    public Menu[] getMenuList() {
    	// 삭제된 메뉴를 제외한 메뉴 개수 계산
        int count = 0;
        for (int i = 0; i < menuList.length; i++) {
            if (menuList[i] != null) {
            	count++;
            }
        }

        // 삭제된 메뉴를 제외하고 순차적으로 메뉴 목록 재배열
        Menu[] sortedMenuList = new Menu[count];
        int index = 0;
        for (int i = 0; i < menuList.length; i++) {
            if (menuList[i] != null) {
                sortedMenuList[index] = menuList[i];
                index++;
            }
        }
        // 재배열된 메뉴 목록 반환
        return sortedMenuList;
    }
    
    public boolean addMenu(Menu menu) {
        for (int i = 0; i < menuList.length; i++) {
            if (menuList[i] != null && menuList[i].getName().equals(menu.getName())) {
                return false; // 이미 존재하는 메뉴
            }
        }

        for (int i = 0; i < menuList.length; i++) {
            if (menuList[i] == null) {
                menuList[i] = menu;
                return true; // 메뉴 추가 성공
            }
        }
        return false; // 더 이상 메뉴를 추가할 수 없음
    }

    public boolean deleteMenu(int menuNumber) {
        if (menuNumber >= 1 && menuNumber <= menuList.length) {
            int index = menuNumber - 1;
            if (menuList[index] != null) {
                menuList[index] = null;
                return true; // 메뉴 삭제 성공
            }
        }
        return false; // 유효하지 않은 메뉴 번호 또는 이미 삭제된 메뉴
    }
}
