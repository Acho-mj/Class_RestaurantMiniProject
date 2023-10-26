package miniproject;

import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		Scanner fileScan = new Scanner(System.in);
		String fileName = "restaurant.dat";
		File file = new File(fileName);
        Restaurant restaurant = new Restaurant();


        try {
            if (file.exists()) {
            	restaurant = restaurant.loadData(file); // 파일 데이터 불러오기
            } else {
                System.out.print("파일이 존재하지 않습니다. 최초 수행인가요? (Y/N): ");
                String createFileChoice = fileScan.nextLine().trim();

                if (createFileChoice.equalsIgnoreCase("Y")) {
                    restaurant = new Restaurant();
                } else {
                    System.out.println("파일이 없음. 프로그램을 종료합니다.");
                    fileScan.close();
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("파일 불러오기 또는 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
		
        Scanner sc = new Scanner(System.in);

        System.out.println("<< 음식점 주문 시스템 >>");
        
        while (true) {
            System.out.println("\n1. 메뉴 관련 작업");
            System.out.println("2. 테이블 관련 작업");
            System.out.println("3. 주문하기");
            System.out.println("4. 주문 목록 보기");
            System.out.println("5. 체크아웃하기");
            System.out.println("6. 데이터 저장하기");
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
                
             // 메뉴 관련 작업
            case 1:
            	while (true) {
            		System.out.println("\n1. 메뉴 추가");
                    System.out.println("2. 메뉴 삭제");
                    System.out.println("3. 메뉴 목록 보기");
                    System.out.println("0. 이전 메뉴로 돌아가기");
                    System.out.print("원하는 작업을 선택하세요: ");
                    int menuChoice = sc.nextInt();
                    sc.nextLine(); // 개행 문자 제거
                    
                    switch (menuChoice) {
                    	// 메뉴 추가
                        case 1:
                        	while (true) {
                        		try {
                                    System.out.print("추가할 메뉴의 이름을 입력하세요: ");
                                    String menuName = sc.nextLine();
                                    
                                    System.out.print("추가할 메뉴의 가격을 입력하세요: ");
                                    double menuPrice = sc.nextDouble();
                                    sc.nextLine();
                                    
                                    restaurant.addMenu(menuName, menuPrice);
                                    System.out.println(menuName + " 메뉴가 추가되었습니다.");
                                    break;
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getMessage());
                                }
                        	}

                            break;
                            
                        // 메뉴 삭제
                        case 2:
                        	while (true) {
                        		try {
                                    System.out.print("삭제할 메뉴의 이름을 입력하세요: ");
                                    String deleteMenuName = sc.nextLine();

                                    restaurant.deleteMenu(deleteMenuName);
                                    System.out.println(deleteMenuName + " 메뉴가 삭제되었습니다.");
                                    break;
                                } catch (IllegalArgumentException | IllegalStateException e) {
                                    System.out.println(e.getMessage());
                                }
                        	}

                            break;
                            
                        // 메뉴 목록 출력
                        case 3:
                            System.out.println("\n=== 메뉴 목록 ===");
                            ArrayList<Menu> menuList = restaurant.getMenuList();
                            for (int i = 0; i < menuList.size(); i++) {
                                Menu menu = menuList.get(i);
                                System.out.println(i + 1 + ". " + menu.getName() + " : " + menu.getPrice());
                            }
                            break;
                        
                        // 이전 메뉴로 돌아가기
                        case 0:
                        	break;
                        	
                        default:
                            System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
                            break;
                    }
                    
                    if (menuChoice == 0) {
                    	break;
                    }
                }
                break;
            
                
             // 테이블 관련 작업
            case 2:
            	while (true) {
            		System.out.println("\n1. 테이블 추가");
                    System.out.println("2. 테이블 삭제");
                    System.out.println("3. 테이블 목록 보기");
                    System.out.println("0. 이전 메뉴로 돌아가기");
                    System.out.print("원하는 작업을 선택하세요: ");
                    int tableChoice = sc.nextInt();
                    sc.nextLine(); // 개행 문자 제거
                    
                    switch (tableChoice) {
                    // 테이블 추가
                    case 1:
                    	try {
                            System.out.print("추가할 테이블 이름을 입력하세요: ");
                            String tableName = sc.nextLine();

                            System.out.print("테이블의 수용 인원을 입력하세요: ");
                            int tableCapacity = sc.nextInt();
                            sc.nextLine();

                            restaurant.addTable(tableName, tableCapacity);
                            System.out.println(tableName + " 테이블이 추가되었습니다.");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }

                        break;
                        
                            
                        // 테이블 삭제
                        case 2:
                        	try {
                                System.out.print("삭제할 테이블 이름을 입력하세요: ");
                                String removeTableName = sc.nextLine();

                                restaurant.deleteTable(removeTableName);
                                System.out.println(removeTableName + " 테이블이 삭제되었습니다.");
                            } catch (IllegalArgumentException | IllegalStateException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                            
                            
                        // 테이블 목록 출력
                        case 3:
                            System.out.println("\n=== 테이블 목록 ===");
                            ArrayList<Table> tableList = restaurant.getTableList();
                            for (int i = 0; i < tableList.size(); i++) {
                                Table table = tableList.get(i);
                                if (table != null) {
                                    System.out.println(i + 1 + ". " + table.getTableName() + " : " + table.getCapacity());
                                }
                            }
                            break;
                        
                            
                        // 이전 메뉴로 돌아가기
                        case 0:
                        	break;
                        	
                        default:
                            System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
                            break;
                    }
                    
                    if (tableChoice == 0) {
                    	break;
                    }
                }
                break;
            
                
            // 테이블 번호 + 주문
            case 3:
            	String orderTableName = null;
                Table orderTable = null;
                
                while (true) {
                	try {
                        System.out.print("주문받을 테이블 이름을 입력하세요: ");
                        orderTableName = sc.nextLine();

                        orderTable = restaurant.getTable(orderTableName);

                        System.out.print("주문할 메뉴 이름을 입력하세요: ");
                        String orderMenuName = sc.nextLine();
                        Menu orderMenu = restaurant.getMenu(orderMenuName);

                        System.out.print("주문할 수량을 입력하세요: ");
                        int quantity = sc.nextInt();
                        sc.nextLine();

                        restaurant.takeOrder(orderTable, orderMenu, quantity);
                        break;
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }

                }

                break;
                
               
            // 주문 목록 보기     
            case 4:
            	String orderListTableName = null;
                Table orderListTable = null;
                while (true) {
                    try {
                        System.out.print("주문 목록을 확인할 테이블 이름을 입력하세요: ");
                        orderListTableName = sc.nextLine();

                        orderListTable = restaurant.findTableByName(orderListTableName);
                        if (orderListTable == null) {
                            throw new IllegalArgumentException("존재하지 않는 테이블입니다. 다시 입력해주세요.");
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                displayOrderList(orderListTable);
                break;

            	
            // 체크아웃하기	
            case 5:
                String checkoutTableName;
                
                while (true) {
                    try {
                        System.out.print("체크아웃할 테이블 이름을 입력하세요: ");
                        checkoutTableName = sc.nextLine();
                        boolean success = restaurant.checkOutTable(checkoutTableName);
                        
                        if (success) {
                            System.out.println(checkoutTableName + " 테이블이 비었습니다.");
                            break;
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                break;
                
                
            case 6:
                boolean exitDataMenu = false; // 루프 종료 조건을 위한 변수 추가
                while (!exitDataMenu) {
                    System.out.println("\n1. 현재 데이터 저장하기");
                    System.out.println("0. 이전 메뉴로 돌아가기");
                    System.out.print("원하는 작업을 선택하세요: ");
                    int dataChoice = sc.nextInt();
                    sc.nextLine(); // 개행 문자 제거         

                    switch (dataChoice) {
                        // 현재 데이터 저장하기
                        case 1:
                        	restaurant.saveData(file);
                            break;

                        case 0:
                            exitDataMenu = true; // 루프 종료 조건 설정
                            break;

                        default:
                            System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
                            break;
                    }
                }          	
                break;
                      
            default:
                System.out.println("유효하지 않은 선택입니다. 다시 선택하세요.");
                break;
            }
        }
	}
	
	
	public static void displayOrderList(Table table) {
	    System.out.println("\n" + table.getTableName() + " 테이블 주문 목록");
	    System.out.println("------------------------------------");
	    System.out.println("메뉴\t가격\t수량\t금액");
	    System.out.println("------------------------------------");

	    ArrayList<Order> orders = table.getOrderList();
	    double totalPrice = table.totalPay();

	    for (Order order : orders) {
	        if (order != null) {
	            Menu menu = order.getMenu();
	            int num = order.getQuantity();
	            double price = order.pay();

	            System.out.println(menu.getName() + "\t" + menu.getPrice() + "\t" + num + "\t" + price);
	        }
	    }

	    System.out.println("------------------------------------");
	    System.out.println("총 금액: " + totalPrice);
	    System.out.println("------------------------------------");
	}
}


