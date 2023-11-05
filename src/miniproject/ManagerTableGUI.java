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
        guestCountLabel.setBounds(100, 50, 150, 30);
        panel.add(guestCountLabel);

        guestCountField = new JTextField();
        guestCountField.setBounds(200, 50, 100, 30);
        panel.add(guestCountField);

        JButton showTablesButton = new JButton("입력");
        showTablesButton.setBounds(350, 50, 100, 30);
        panel.add(showTablesButton);

        showTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int guestCount = Integer.parseInt(guestCountField.getText());
                displayAvailableTables(guestCount);
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
                int selectedRow = tableListTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the selected table's row data
                    String tableName = (String) tableModel.getValueAt(selectedRow, 0);
                    int capacity = (int) tableModel.getValueAt(selectedRow, 1);
                    
                    // Find the corresponding table in the restaurant's table list
                    for (Table table : restaurant.getTableList()) {
                        if (table.getTableName().equals(tableName) && table.getCapacity() == capacity) {
                            // Change the table's status to "이용 중" (not available)
                            table.tableCompleted();
                        }
                    }
                    
                    // Refresh the table display
                    displayAllTables();
                }
            }
        });
        // 매니저 모드 이동 버튼
        JButton adminButton = new JButton("매니저 화면으로 이동하기");
        adminButton.setBounds(120, 360, 250, 30);
        panel.add(adminButton);
        
        displayAllTables();
    }
    
    private void displayAllTables() {
    	tableModel.setRowCount(0);
    	ArrayList<Table> tables = restaurant.getTableList();
    	for (Table table : tables) {
        	tableModel.addRow(new Object[]{table.getTableName(), table.getCapacity(), table.isAvailable()});
        }
    }

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
