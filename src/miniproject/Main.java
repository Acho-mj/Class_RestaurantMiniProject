package miniproject;

import java.util.*;

public class Main {
	public static void main(String[] args) {
		Restaurant restaurant = new Restaurant();
        Scanner sc = new Scanner(System.in);

        System.out.println("<< 음식점 주문 시스템 >>");
        
        while (true) {
            System.out.println("\n1. 메뉴 추가");
            System.out.println("2. 메뉴 삭제");
            System.out.println("3. 메뉴 목록 보기");
            System.out.println("4. 테이블 추가");
            System.out.println("5. 테이블 삭제");
            System.out.println("6. 테이블 목록 보기");
            System.out.println("7. 주문하기");
            System.out.println("8. 주문 목록 보기");
            System.out.println("9. 체크아웃하기");
            System.out.println("0. 종료");
            System.out.print("원하는 작업을 선택하세요: ");
            int choice = sc.nextInt();
            sc.nextLine(); // 개행 문자 제거
            
            switch (choice) {
            // 프로그램 종료
            case 0:
                System.out.println("프로그램을 종료합니다.");
                sc.close();
                return;
                
            // 메뉴 추가
            case 1:
            	while (true) {
                    System.out.print("추가할 메뉴의 이름을 입력하세요: ");
                    String menuName = sc.nextLine();
                    
                    if (restaurant.getMenu(menuName) != null) {
                        System.out.println("이미 존재하는 메뉴 이름입니다. 다른 이름을 입력해주세요.");
                    } else {
                        System.out.print("추가할 메뉴의 가격을 입력하세요: ");
                        double menuPrice = sc.nextDouble();
                        sc.nextLine();
                        
                        restaurant.addMenu(menuName, menuPrice);
                        System.out.println(menuName + " 메뉴가 추가되었습니다.");
                        break;
                    }
                }
                break;

            
            //메뉴 삭제 
            case 2:
            	while (true) {
                    System.out.print("삭제할 메뉴의 이름을 입력하세요: ");
                    String deleteMenuName = sc.nextLine();
                    
                    if (restaurant.getMenu(deleteMenuName) == null) {
                        System.out.println("존재하지 않는 메뉴 이름입니다. 다른 이름을 입력해주세요.");
                    } else {
                        if (restaurant.deleteMenu(deleteMenuName)) {
                            System.out.println(deleteMenuName + " 메뉴가 삭제되었습니다.");
                        } else {
                            System.out.println("메뉴 삭제에 실패했습니다.");
                        }
                        break;
                    }
                }
                break;
            
             // 메뉴 목록 출력
            case 3:
                System.out.println("\n=== 메뉴 목록 ===");
                restaurant.printMenuList();
                break;
                
            // 테이블 추가    
            case 4:
            	while (true) {
                    System.out.print("추가할 테이블 이름을 입력하세요: ");
                    String tableName = sc.nextLine();

                    if (restaurant.getTable(tableName) != null) {
                        System.out.println("이미 존재하는 테이블입니다. 다시 입력해주세요.");
                    } else {
                        restaurant.addTable(tableName);
                        System.out.println(tableName + " 테이블이 추가되었습니다.");
                        break;
                    }
                }
                break;
            
            // 테이블 삭제
            case 5:
            	while (true) {
                    System.out.print("삭제할 테이블 이름을 입력하세요: ");
                    String removeTableName = sc.nextLine();

                    if (restaurant.getTable(removeTableName) == null) {
                        System.out.println("존재하지 않는 테이블입니다. 다시 입력해주세요.");
                    } else {
                        if (restaurant.deleteTable(removeTableName)) {
                            System.out.println(removeTableName + " 테이블이 삭제되었습니다.");
                        } else {
                            System.out.println("테이블 삭제에 실패했습니다.");
                        }
                        break;
                    }
                }
                 break;
                 
              // 테이블 목록 출력
            case 6:
                System.out.println("\n=== 테이블 목록 ===");
                restaurant.printTableList();
                break;
            
            // 테이블 번호 + 주문
            case 7:
            	String orderTableName = null;
                Table orderTable = null;
            	while (true) {
                    System.out.print("주문받을 테이블 이름을 입력하세요: ");
                    orderTableName = sc.nextLine();

                    if (restaurant.getTable(orderTableName) == null) {
                        System.out.println("존재하지 않는 테이블입니다. 다시 입력해주세요.");
                    } else
                    	break;
                }
               
                orderTable = restaurant.getTable(orderTableName);

                String orderMenuName = null;
                Menu orderMenu = null;
                boolean validMenu = false;

                while (!validMenu) {
                    System.out.print("주문할 메뉴 이름을 입력하세요: ");
                    orderMenuName = sc.nextLine();

                    orderMenu = restaurant.getMenu(orderMenuName);
                    if (orderMenu != null) {
                        validMenu = true;
                    } else {
                        System.out.println("유효하지 않은 메뉴 이름입니다. 다시 입력해주세요.");
                    }
                }

                System.out.print("주문할 수량을 입력하세요: ");
                int quantity = sc.nextInt();
                sc.nextLine();

                orderTable.addOrder(orderMenu, quantity);

                System.out.print("계속해서 주문을 진행하시겠습니까? (Y/N): ");
                String answer = sc.nextLine();

                if (answer.equalsIgnoreCase("N")) {
                    break;
                }
            
                break;
               
            // 주문 목록 보기     
            case 8:
            	String orderListTableName = null;
                Table orderListTable = null;
            	while (true) {
                    System.out.print("주문 목록을 확인할 테이블 이름을 입력하세요: ");
                    orderListTableName = sc.nextLine();

                    if (restaurant.getTable(orderListTableName) == null) {
                        System.out.println("존재하지 않는 테이블입니다. 다시 입력해주세요.");
                    } else
                    	break;
                }
            	orderListTable = restaurant.getTable(orderListTableName);
                
            	if (orderListTable != null) {
                    System.out.println("주문 목록 - " + orderListTable.getTableName());
                    System.out.println("----------------------------");
                    System.out.println("메뉴\t수량\t금액");
                    System.out.println("-----------------------------");
                    
                    Order[] orders = orderListTable.getOrderList();
                    int totalPrice = 0;

                    for (Order order : orders) {
                        if (order != null) {
                            Menu menu = order.getMenu();
                            int num = order.getQuantity();
                            double price = menu.getPrice() * num;

                            System.out.println(menu.getName() + "\t" + num + "\t" + price);
                            totalPrice += price;
                        }
                    }

                    System.out.println("-----------------------------");
                    System.out.println("총 대금: " + totalPrice);
                } 
                break;

            	
            // 체크아웃하기	
            case 9:
            	
            	break;
            
            default:
                System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
                break;
            }
        }
	}
}