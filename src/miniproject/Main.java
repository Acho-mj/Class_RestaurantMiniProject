package miniproject;

import java.util.*;

public class Main {
	public static void main(String[] args) {
		Restaurant restaurant = new Restaurant();
        Scanner sc = new Scanner(System.in);
        Table[] tables = new Table[5]; // 테이블 개수 5개(일단..)
        
        for (int i = 0; i < tables.length; i++) {
        	tables[i] = new Table(i + 1); // 테이블 번호는 1부터 시작
        }

        System.out.println("<< 음식점 주문 시스템 >>");
        while (true) {
            System.out.println("\n=== 메뉴 목록 ===");
            Menu[] menuList = restaurant.getMenuList();
            
            // 메뉴 출력
            for (int i = 0; i < menuList.length; i++) {
            	Menu menu = menuList[i];
                if (menu != null) {
                    System.out.println(i + 1 + ". " + menu.getName() + " : " + menu.getPrice());
                }
            }
            
            System.out.println("\n1. 메뉴 추가");
            System.out.println("2. 메뉴 삭제");
            System.out.println("3. 주문하기");
            System.out.println("0. 종료");
            System.out.print("원하는 작업을 선택하세요: ");
            int choice = sc.nextInt();
            
            switch (choice) {
            case 0:
                System.out.println("프로그램을 종료합니다.");
                sc.close();
                return;
                
            // 메뉴 추가
            case 1:
                System.out.print("추가할 메뉴의 이름을 입력하세요: ");
                String name = sc.next();
                
                System.out.print("추가할 메뉴의 가격을 입력하세요: ");
                double price = sc.nextDouble();
                
                Menu newMenu = new Menu(name, price);
                
                boolean isAdded = restaurant.addMenu(newMenu);
                if (isAdded) {
                    System.out.println(newMenu.getName() + " 메뉴가 추가되었습니다.\n");
                } else {
                    System.out.println("이미 존재하는 메뉴입니다.\n");
                }
                break;
            
            //메뉴 삭제 
            case 2:
            	System.out.print("삭제할 메뉴의 번호를 입력하세요: ");
                int menuNumber = sc.nextInt();
                
                boolean isDeleted = restaurant.deleteMenu(menuNumber);
                if (isDeleted) {
                    System.out.println("메뉴가 삭제되었습니다.\n");
                } else {
                    System.out.println("유효하지 않은 메뉴 번호입니다.\n");
                }
                break;           
            
            // 테이블 번호 + 주문
            case 3:
            	System.out.println("\n=== 사용 가능한 테이블 목록 ===");
            	int[] availableTables = Table.showAvailableTables(tables);
                
            	for (int i = 0; i < availableTables.length; i++) {
                    System.out.print(availableTables[i] + " ");
                }
            	
            	System.out.print("\n주문할 테이블 번호를 선택하세요: ");
            	int tableNumber = sc.nextInt();
            	
            	if (tableNumber >= 1 && tableNumber <= tables.length) {
            		Table selectedTable = tables[tableNumber - 1];
            	
            		while (true) {
            			System.out.print("주문할 메뉴 번호를 입력하세요 (종료하려면 0 입력): ");
            			int orderChoice = sc.nextInt();
            			
            			if (orderChoice == 0) {
            				break;
            			} else if (orderChoice >= 1 && orderChoice <= menuList.length) {
            				System.out.print("수량을 입력하세요: ");
            				int num = sc.nextInt();
            				Menu menuChoice = menuList[orderChoice - 1];

            				if (menuChoice != null) {
            					for (int i = 0; i < num; i++) {
            						selectedTable.addOrder(menuChoice);
            					}
            					System.out.println(menuChoice.getName() + " " + num + "개가 주문되었습니다.\n");
            				} else {
            					System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
            				}
            				
            			} else {
            				System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
            			}
            		}
            	
            		System.out.println("\n == " + tableNumber + " 번 테이블" + "주문 내역 ==");
            		Menu[] orderList = selectedTable.getOrderList();

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
            	}else {
                    System.out.println("유효하지 않은 테이블 번호입니다. 다시 선택하세요.");
                }
                break;
            
            default:
                System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
                break;
            }
        }
	}
}