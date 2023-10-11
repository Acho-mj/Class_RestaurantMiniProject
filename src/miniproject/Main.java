package miniproject;

import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		Scanner fileScan = new Scanner(System.in);
	    Restaurant restaurant = null;
	    DataInputStream in = null;
	    String fileName = "restaurant.dat";
	    File file = new File(fileName);

        try {
            if (!file.exists()) {
                // 파일이 존재하지 않을 경우, 사용자에게 생성 여부 묻기
                System.out.print("파일이 존재하지 않습니다. 최초 수행인가요? (Y/N): ");
                String createFileChoice = fileScan.nextLine().trim();

                if (createFileChoice.equalsIgnoreCase("N")) {
                    System.out.println("파일이 없음. 프로그램을 종료합니다.");
                    fileScan.close();
                    return;
                } else if (createFileChoice.equalsIgnoreCase("Y")) {
                    if (file.createNewFile()) {
                        System.out.println("파일이 생성되었습니다.");
                    } else {
                        System.out.println("파일 생성 중 오류가 발생했습니다.");
                        fileScan.close();
                        return;
                    }
                }
            }
            // 파일이 존재할 경우 데이터 불러오기
            restaurant = new Restaurant(file);
        } catch (Exception e) {
            System.out.println("파일 불러오기 또는 생성 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
		
        Scanner sc = new Scanner(System.in);

        System.out.println("<< 음식점 주문 시스템 >>");
        
        while (true) {
            System.out.println("\n1. 메뉴 관련 작업");
            System.out.println("2. 테이블 관련 작업");
            System.out.println("3. 주문하기");
            System.out.println("4. 주문 목록 보기");
            System.out.println("5. 체크아웃하기");
            System.out.println("6. 데이터 저장 및 불러오기");
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

                        	        if (restaurant.getMenu(menuName) != null) {
                        	            throw new IllegalArgumentException("이미 존재하는 메뉴 이름입니다. 다른 이름을 입력해주세요.");
                        	        }

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

                        	        Menu deleteMenu = restaurant.getMenu(deleteMenuName);
                        	        if (deleteMenu == null) {
                        	            throw new IllegalArgumentException("존재하지 않는 메뉴 이름입니다. 다른 이름을 입력해주세요.");
                        	        }

                        	        if (restaurant.deleteMenu(deleteMenuName)) {
                        	            System.out.println(deleteMenuName + " 메뉴가 삭제되었습니다.");
                        	        } else {
                        	            throw new IllegalStateException("메뉴 삭제에 실패했습니다.");
                        	        }

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
                    	while (true) {
                    	    try {
                    	        System.out.print("추가할 테이블 이름을 입력하세요: ");
                    	        String tableName = sc.nextLine();

                    	        if (restaurant.getTable(tableName) != null) {
                    	            throw new IllegalArgumentException("이미 존재하는 테이블입니다. 다시 입력해주세요.");
                    	        }

                    	        System.out.print("테이블의 수용 인원을 입력하세요: ");
                    	        int tableCapacity = sc.nextInt();
                    	        sc.nextLine();

                    	        restaurant.addTable(tableName, tableCapacity);
                    	        System.out.println(tableName + " 테이블이 추가되었습니다.");
                    	        break;
                    	    } catch (IllegalArgumentException e) {
                    	        System.out.println(e.getMessage());
                    	    }
                    	}

                        break;
                            
                        // 테이블 삭제
                        case 2:
                        	while (true) {
                        	    try {
                        	        System.out.print("삭제할 테이블 이름을 입력하세요: ");
                        	        String removeTableName = sc.nextLine();

                        	        Table removeTable = restaurant.getTable(removeTableName);
                        	        if (removeTable == null) {
                        	            throw new IllegalArgumentException("존재하지 않는 테이블입니다. 다시 입력해주세요.");
                        	        }

                        	        if (restaurant.deleteTable(removeTableName)) {
                        	            System.out.println(removeTableName + " 테이블이 삭제되었습니다.");
                        	        } else {
                        	            throw new IllegalStateException("테이블 삭제에 실패했습니다.");
                        	        }

                        	        break;
                        	    } catch (IllegalArgumentException | IllegalStateException e) {
                        	        System.out.println(e.getMessage());
                        	    }
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
                        if (orderTable == null) {
                            throw new IllegalArgumentException("존재하지 않는 테이블입니다. 다시 입력해주세요.");
                        }

                        if (orderTable.availableTables()) {
                            throw new IllegalStateException("이미 주문을 받은 테이블입니다. 다른 테이블을 선택해주세요.");
                        }

                        int tableCapacity = orderTable.getCapacity();
                        System.out.print("주문할 인원 수를 입력하세요: ");
                        int numberOfPeople = sc.nextInt();
                        sc.nextLine();

                        if (numberOfPeople > tableCapacity) {
                            throw new IllegalArgumentException("테이블 수용 인원인 " + tableCapacity + "명을 초과하였습니다. 다른 테이블을 선택해주세요.");
                        }

                        break; 
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                }
              
                // 주문 진행
                while (true) {
                    try {
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
                                throw new IllegalArgumentException("유효하지 않은 메뉴 이름입니다. 다시 입력해주세요.");
                            }
                        }

                        System.out.print("주문할 수량을 입력하세요: ");
                        int quantity = sc.nextInt();
                        sc.nextLine();

                        Order order = new Order(orderMenu.getName(), orderMenu.getPrice(), quantity);
                        orderTable.addOrder(order);

                        System.out.print("계속해서 주문을 진행하시겠습니까? (N 입력시 주문 끝남): ");
                        String answer = sc.nextLine().trim();

                        if (answer.equalsIgnoreCase("N")) {
                            break;
                        }
                    } catch (IllegalArgumentException e) {
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

                        // 주어진 테이블 이름으로 테이블 조회
                        orderListTable = restaurant.findTableByName(orderListTableName);

                        // 테이블이 존재하지 않는 경우 예외를 발생
                        if (orderListTable == null) {
                            throw new IllegalArgumentException("존재하지 않는 테이블입니다. 다시 입력해주세요.");
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            	orderListTable = restaurant.getTable(orderListTableName);
                
            	if (orderListTable != null) {
                    System.out.println("\n" + orderListTable.getTableName() + " 테이블 주문 목록");
                    System.out.println("------------------------------------");
                    System.out.println("메뉴\t가격\t수량\t금액");
                    System.out.println("------------------------------------");
                    
                    ArrayList<Order> orders = orderListTable.getOrderList();
                    double totalPrice = orderListTable.totalPay(); 

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
                break;

            	
            // 체크아웃하기	
            case 5:
            	String checkoutTableName = null;
                Table checkoutTable = null;
                while (true) {
                    try {
                        System.out.print("체크아웃할 테이블 이름을 입력하세요: ");
                        checkoutTableName = sc.nextLine();
                        // 입력된 테이블 이름으로 테이블 객체저장
                        checkoutTable = restaurant.getTable(checkoutTableName);

                        // 테이블 객체가 null인 경우, 존재하지 않는 테이블 이름임을 출력
                        if (checkoutTable == null) {
                            throw new IllegalArgumentException("존재하지 않는 테이블입니다. 다시 입력해주세요.");
                        }

                        // 체크아웃 후 해당 테이블의 주문 목록 초기화
                        checkoutTable.clearOrderList();
                        System.out.println(checkoutTable.getTableName() + " 테이블이 비었습니다.");
                        break;
                        
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
                        	try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
                                restaurant.saveFile(dos); // 데이터를 파일에 저장
                                System.out.println("데이터 저장이 완료되었습니다.");
                            } catch (Exception e) {
                                System.out.println("파일 저장에 오류가 발생했습니다.");
                                e.printStackTrace();
                            }
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
}