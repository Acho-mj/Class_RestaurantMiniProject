package miniproject;

import java.util.*;

public class Main {
	public static void main(String[] args) {
		Restaurant restaurant = new Restaurant();
        Scanner sc = new Scanner(System.in);
        Table table = new Table();

        System.out.println("<< 음식점 주문 시스템 >>");
        while (true) {
            System.out.println("메뉴 목록:");
            Menu[] menuList = restaurant.getMenuList();
            
            for (int i = 0; i < menuList.length; i++) {
            	Menu menu = menuList[i];
                if (menu != null) {
                    System.out.println(i + 1 + ". " + menu.getName() + " : " + menu.getPrice());
                }
            }
            
            System.out.print("주문할 메뉴 번호를 입력하세요 (종료하려면 0 입력): ");
            int choice = sc.nextInt();

            if (choice == 0) {
                break;
            } else if (choice >= 1 && choice <= menuList.length) {
                System.out.print("수량을 입력하세요: ");
                int num = sc.nextInt();
                Menu menuChoice = menuList[choice - 1];
                
                if (menuChoice != null) {
                    for (int i = 0; i < num; i++) {
                        table.addOrder(menuChoice);
                    }
                    
                    System.out.println(menuChoice.getName() + " " + num + "개가 주문되었습니다.\n");
                } else {
                    System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
                }
            } else {
                System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
            }
        }

        System.out.println("\n == 주문 내역 ==");
        Menu[] orderList = table.getOrderList();

        // 주문된 메뉴의 개수를 저장하기 위한 배열
        int[] menuCounts = new int[orderList.length];

        // 각 메뉴별로 주문된 개수를 카운트
        for (int i = 0; i < orderList.length; i++) {
        	Menu menu = orderList[i];
        	int count = 1;

        	// 현재 메뉴와 동일한 메뉴가 뒤에 중복해서 주문되었는지 확인
        	for (int j = i + 1; j < orderList.length; j++) {
        		if (menu == orderList[j]) {
        			count++;
        		}
        	}

        	menuCounts[i] = count;
        }

        // 중복을 제외하고 주문 내역 출력
        for (int i = 0; i < orderList.length; i++) {
        	if (i > 0 && orderList[i] == orderList[i - 1]) {
        		continue; // 중복된 메뉴는 출력하지 않음
        	}

        	Menu menu = orderList[i];
        	int count = menuCounts[i];
        	System.out.println(menu.getName() + ", " + count);
        }
        sc.close();
    }
}