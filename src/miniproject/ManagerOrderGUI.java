package miniproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import miniproject.Menu;

public class ManagerOrderGUI extends JFrame{

	private Restaurant restaurant;
	private JTextField searchField;
	
	// 주문 내역
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    
    // 테이블 리스트
    private JTable tableListTable;
    private DefaultTableModel tableModel;
    
    // 메뉴판(주문)
    private JTable menuTable;
    private DefaultTableModel menuTableModel;
    
    public ManagerOrderGUI(Restaurant restaurant) throws IOException {
    	this.restaurant = restaurant; // restaurant 필드 초기화

        ArrayList<Table> tableList = restaurant.getTableList();
        
        setTitle("테이블 별 주문 및 체크아웃");
        setSize(520, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);
        setVisible(true);
        
        // 테이블 검색 필드
        JLabel searchLabel = new JLabel("테이블 이름 검색");
        searchLabel.setBounds(60, 50, 200, 30);
        panel.add(searchLabel);
        
        searchField = new JTextField();
        searchField.setBounds(180, 50, 150, 30);
        panel.add(searchField);

        // 테이블 검색 버튼
        JButton searchButton = new JButton("검색");
        searchButton.setBounds(360, 50, 80, 30);
        panel.add(searchButton);
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                searchTables(keyword);
            }
        });
        
        // 테이블 Table
        String[] columnNames = {"테이블 이름", "수용인원", "이용 가능 여부"};
        tableModel = new DefaultTableModel(null, columnNames);
        tableListTable = new JTable(tableModel);
        JScrollPane scrollPane1 = new JScrollPane(tableListTable);
        scrollPane1.setBounds(50, 100, 400, 200);
        panel.add(scrollPane1);
        
       
        // 메뉴판 주문 Table
        JLabel orderLabel = new JLabel("메뉴판");
        orderLabel.setBounds(50, 280, 100, 30);
        panel.add(orderLabel);
        
        String[] menuColumnNames = {"메뉴 이름", "메뉴 가격", "수량"};
        menuTableModel = new DefaultTableModel(menuColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // 수량 필드만 편집 가능하도록 설정
            }
        };
        menuTable = new JTable(menuTableModel);
        JScrollPane scrollPane2 = new JScrollPane(menuTable);
        scrollPane2.setBounds(50, 300, 400, 200);
        panel.add(scrollPane2);

        // 메뉴 목록을 테이블에 추가
        ArrayList<Menu> menuList = restaurant.getMenuList();
        for (Menu menu : menuList) {
            menuTableModel.addRow(new Object[]{menu.getName(), menu.getPrice(), 0}); // 초기 수량은 0
        }
        
        // 주문 내역 테이블 생성
        JLabel orderListLabel = new JLabel("주문 내역");
        orderListLabel.setBounds(50, 520, 100, 30);
        panel.add(orderListLabel);
        
        tableListTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableListTable.getSelectedRow();
                if (selectedRow != -1) {
                    // 선택한 테이블에 대한 주문 내역을 가져와서 표시
                    Table selectedTable = tableList.get(selectedRow);

                    if (!selectedTable.isAvailable()) {
                        // 테이블이 이용 가능한 경우에만 주문 내역 표시 메서드 호출
                        displayOrderInfo(selectedTable);
                    } else {
                        // 테이블이 이용 불가능한 경우에는 메시지 출력
                        JOptionPane.showMessageDialog(null, "배정받지 못한 테이블입니다.", "주문 불가", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        
        String[] orderColumns = {"메뉴 이름", "메뉴 가격", "수량", "합계"};
        orderTableModel = new DefaultTableModel(orderColumns, 0);
        orderTable = new JTable(orderTableModel);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderScrollPane.setBounds(50, 550, 400, 200);
        panel.add(orderScrollPane);
        
        
        // 주문하기 버튼
        JButton orderButton = new JButton("주문하기");
        orderButton.setBounds(180, 510, 150, 30);
        panel.add(orderButton);
        
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 주문 버튼을 클릭하면 선택한 메뉴를 주문 내역에 추가
                int selectedRow = menuTable.getSelectedRow();
                if (selectedRow != -1) {
                    String menuName = menuTableModel.getValueAt(selectedRow, 0).toString();
                    double menuPrice = Double.parseDouble(menuTableModel.getValueAt(selectedRow, 1).toString());
                    int quantity = Integer.parseInt(menuTableModel.getValueAt(selectedRow, 2).toString());

                    if (quantity > 0) {
                        // 선택한 테이블 가져오기
                        Table selectedTable = tableList.get(selectedRow);
                        if (selectedTable != null) {
                            Order newOrder = new Order(menuName, menuPrice, quantity);
                            selectedTable.addOrder(newOrder);

                            // 업데이트된 주문 정보를 테이블에 반영
                            displayOrderInfo(selectedTable);
                        } 
                    } else {
                        JOptionPane.showMessageDialog(null, "수량은 1 이상이어야 합니다.", "주문 오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // 체크아웃 버튼
        JButton checkOutButton = new JButton("체크아웃하기");
        checkOutButton.setBounds(180, 760, 150, 30);
        panel.add(checkOutButton); 
        
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableListTable.getSelectedRow();
                if (selectedRow != -1) {
                    Table selectedTable = tableList.get(selectedRow);

                    if (selectedTable.isAvailable()) {
                        JOptionPane.showMessageDialog(null, "배정받지 못한 테이블입니다.", "체크아웃 불가", JOptionPane.WARNING_MESSAGE);
                    } else {
                        int option = JOptionPane.showConfirmDialog(null, "체크아웃 하시겠습니까?", "체크아웃 확인", JOptionPane.YES_NO_OPTION);
                        
                        if (option == JOptionPane.YES_OPTION) {
                            selectedTable.guestDeparted();
                            JOptionPane.showMessageDialog(null, "체크아웃이 완료되었습니다.", "체크아웃 완료", JOptionPane.INFORMATION_MESSAGE);

                            displayOrderInfo(selectedTable);
                        }
                    }
                }
            }
        });

        
        // 데이터 저장 버튼
        JButton saveDataButton = new JButton("데이터 저장하기");
        saveDataButton.setBounds(50, 800, 130, 30);
        panel.add(saveDataButton);
        
        saveDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = "restaurant.dat";
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                    restaurant.saveData(out);
                    JOptionPane.showMessageDialog(ManagerOrderGUI.this, "데이터가 저장되었습니다.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // 매니저 모드 이동 버튼
        JButton managerButton = new JButton("매니저 화면으로 이동하기");
        managerButton.setBounds(220, 800, 230, 30);
        panel.add(managerButton);
            
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ManagerGUI managerGUI = new ManagerGUI(restaurant);
            	managerGUI.setVisible(true); 
            }
        });

        displayAllTables();
    }
    
    private void displayAllTables() {
    	tableModel.setRowCount(0);
    	ArrayList<Table> tables = restaurant.getTableList();
    	for (Table table : tables) {
        	tableModel.addRow(new Object[]{table.getTableName(), table.getCapacity(), table.isAvailable()});
        }
    }

    private void displayMenu() {
    	menuTableModel.setRowCount(0);
        ArrayList<Menu> menuList = restaurant.getMenuList();
        for (Menu menu : menuList) {
            menuTableModel.addRow(new Object[]{menu.getName(), menu.getPrice(), 0}); // 초기 수량은 0
        }
    }
    
    private void displayOrderInfo(Table table) {
        orderTableModel.setRowCount(0); // 주문 내역 초기화
        ArrayList<Order> orderList = table.getOrderList();
        for (Order order : orderList) {
            orderTableModel.addRow(new Object[]{order.getMenu().getName(), order.getMenu().getPrice(), order.getQuantity(), order.pay()});
        }
    }

    
    // 테이블 조회
    private void searchTables(String keyword) {
        tableModel.setRowCount(0);
        ArrayList<Table> tables = restaurant.getTableList();
        for (Table table : tables) {
            if (table.getTableName().contains(keyword)) {
                tableModel.addRow(new Object[]{table.getTableName(), table.getCapacity(), table.isAvailable()});
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String fileName = "restaurant.dat";
                File file = new File(fileName);
                Restaurant restaurant = new Restaurant();

                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    restaurant.loadData(in);
                    new ManagerOrderGUI(restaurant);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
