package miniproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;

public class ManagerTableGUI extends JFrame {

    private Restaurant restaurant;
    private JTable tableListTable;
    private DefaultTableModel tableModel;
    private JTextField guestCountField;
    
    public ManagerTableGUI(Restaurant restaurant) throws IOException {
        this.restaurant = restaurant;

        setTitle("테이블 배정");
        setSize(500, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);
        setVisible(true);

        // 손님 수 입력 필드
        JLabel guestCountLabel = new JLabel("손님 수 입력:");
        guestCountLabel.setBounds(70, 50, 150, 30);
        panel.add(guestCountLabel);

        guestCountField = new JTextField();
        guestCountField.setBounds(170, 50, 100, 30);
        panel.add(guestCountField);
        
        JButton allTablesButton = new JButton("전체");
        allTablesButton.setBounds(280, 50, 60, 30);
        panel.add(allTablesButton);
        
        allTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	displayAllTables();
            }
        });

        JButton showTablesButton = new JButton("입력");
        showTablesButton.setBounds(350, 50, 60, 30);
        panel.add(showTablesButton);
       

        showTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// 손님 수 <= 수용인원
                try {
                    int guestCount = Integer.parseInt(guestCountField.getText());
                    displayAvailableTables(guestCount);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "올바른 숫자를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // 테이블 Table
        String[] columnNames = {"테이블 이름", "수용인원", "이용 가능 여부"};
        tableModel = new DefaultTableModel(null, columnNames);
        tableListTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableListTable);
        scrollPane.setBounds(50, 100, 400, 200);
        panel.add(scrollPane);
        
        // 테이블 배정 버튼
        JButton assignButton = new JButton("배정 완료");
        assignButton.setBounds(190, 320, 100, 30);
        panel.add(assignButton);
        
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// 행을 클릭해서 테이블 배정하기
                int selectedRow = tableListTable.getSelectedRow();
                if (selectedRow != -1) {
                    String tableName = (String) tableModel.getValueAt(selectedRow, 0);
                    int capacity = (int) tableModel.getValueAt(selectedRow, 1);
                    
                    for (Table table : restaurant.getTableList()) {
                        if (table.getTableName().equals(tableName) && table.getCapacity() == capacity) {
                            table.tableCompleted();
                        }
                    }
                    // 배정된 테이블 이름 창 출력
                    JOptionPane.showMessageDialog(null, "테이블 '" + tableName + "'에 배정이 완료되었습니다.", "배정 완료", JOptionPane.INFORMATION_MESSAGE);

                    displayAllTables();
                }
            }
        });
        
        // 데이터 저장 버튼
        JButton saveDataButton = new JButton("데이터 저장하기");
        saveDataButton.setBounds(50, 370, 130, 30);
        panel.add(saveDataButton);
        
        saveDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = "restaurant.dat";
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                    restaurant.saveData(out);
                    JOptionPane.showMessageDialog(ManagerTableGUI.this, "데이터가 저장되었습니다.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // 매니저 모드 이동 버튼
        JButton managerButton = new JButton("매니저 화면으로 이동하기");
        managerButton.setBounds(200, 370, 250, 30);
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
    
    // 모든 테이블 목록을 출력
    private void displayAllTables() {
    	tableModel.setRowCount(0);
    	ArrayList<Table> tables = restaurant.getTableList();
    	for (Table table : tables) {
        	tableModel.addRow(new Object[]{table.getTableName(), table.getCapacity(), table.isAvailable()});
        }
    }

    // 이용 가능한 (손님 수 <= 수용인원)테이블 목록을 출력
    private void displayAvailableTables(int guestCount) {
        tableModel.setRowCount(0); 
        for (Table table : restaurant.getTableList()) {
            if (table.getCapacity() >= guestCount && table.isAvailable()) {
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
                    new ManagerTableGUI(restaurant);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
