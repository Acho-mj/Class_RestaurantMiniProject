package miniproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

public class AdminTableGUI extends JFrame {

    private Restaurant restaurant;
    private JTable tableTable;
    private DefaultTableModel tableTableModel;
    private JTextField searchField;
    private JTextField tableNameField;
    private JTextField capacityField;

    public AdminTableGUI(Restaurant restaurant) {
        this.restaurant = restaurant;

        setTitle("테이블 관리");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
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

        // 테이블 Table
        String[] tableColumnNames = {"테이블 이름", "수용 인원"};
        tableTableModel = new DefaultTableModel(null, tableColumnNames);
        tableTable = new JTable(tableTableModel);
        JScrollPane tableScrollPane = new JScrollPane(tableTable);
        tableScrollPane.setBounds(50, 100, 400, 300);
        panel.add(tableScrollPane);


        // 테이블 이름 필드
        JLabel tableNameLabel = new JLabel("테이블 이름");
        tableNameLabel.setBounds(60, 420, 100, 30);
        panel.add(tableNameLabel);
        
        tableNameField = new JTextField();
        tableNameField.setBounds(140, 420, 100, 30);
        panel.add(tableNameField);

        // 테이블 수용인원 필드
        JLabel capacityLabel = new JLabel("수용인원");
        capacityLabel.setBounds(270, 420, 100, 30);
        panel.add(capacityLabel);
        
        capacityField = new JTextField();
        capacityField.setBounds(330, 420, 100, 30);
        panel.add(capacityField);
        
        // 테이블 추가 버튼
        JButton addButton = new JButton("테이블 추가");
        addButton.setBounds(60, 460, 100, 30);
        panel.add(addButton);

        // 테이블 삭제 버튼
        JButton deleteButton = new JButton("테이블 삭제");
        deleteButton.setBounds(180, 460, 100, 30);
        panel.add(deleteButton);

        // 데이터 저장 버튼
        JButton saveDataButton = new JButton("데이터 저장하기");
        saveDataButton.setBounds(300, 460, 130, 30);
        panel.add(saveDataButton);
        
        // 관리자 모드 이동 버튼
        JButton adminButton = new JButton("관리자 화면으로 이동하기");
        adminButton.setBounds(120, 500, 250, 30);
        panel.add(adminButton);

        // Search Button Listener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                searchTables(keyword);
            }
        });

        // Add Table Button Listener
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = tableNameField.getText();
                String capacityText = capacityField.getText();
                if (!tableName.isEmpty() && !capacityText.isEmpty()) {
                    int capacity = Integer.parseInt(capacityText);
                    addTable(tableName, capacity);
                }
            }
        });

        // Delete Table Button Listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = tableNameField.getText();
                if (!tableName.isEmpty()) {
                    deleteTable(tableName);
                }
            }
        });

        // Load and display all tables
        displayAllTables();

        add(panel);
        setVisible(true);
    }

    private void searchTables(String keyword) {
        tableTableModel.setRowCount(0);
        ArrayList<Table> tables = restaurant.getTableList();
        for (Table table : tables) {
            if (table.getTableName().contains(keyword)) {
                String isAvailable = table.availableTables() ? "이용 가능" : "이용 불가";
                tableTableModel.addRow(new Object[]{table.getTableName(), table.getCapacity(), isAvailable});
            }
        }
    }

    private void addTable(String tableName, int capacity) {
        Table table = restaurant.getTable(tableName);
        if (table == null) {
            restaurant.addTable(tableName, capacity);
            JOptionPane.showMessageDialog(this, "테이블이 추가되었습니다.");
            displayAllTables();
        } else {
            JOptionPane.showMessageDialog(this, "같은 이름의 테이블이 이미 존재합니다.");
        }
    }

    private void deleteTable(String tableName) {
        Table table = restaurant.getTable(tableName);
        if (table != null) {
            restaurant.deleteTable(tableName);
            JOptionPane.showMessageDialog(this, "테이블이 삭제되었습니다.");
            displayAllTables();
        } else {
            JOptionPane.showMessageDialog(this, "해당 이름의 테이블이 존재하지 않습니다.");
        }
    }

    private void displayAllTables() {
        tableTableModel.setRowCount(0);
        ArrayList<Table> tables = restaurant.getTableList();
        for (Table table : tables) {
            String isAvailable = table.availableTables() ? "이용 가능" : "이용 불가";
            tableTableModel.addRow(new Object[]{table.getTableName(), table.getCapacity(), isAvailable});
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
                    new AdminGUI(restaurant);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
