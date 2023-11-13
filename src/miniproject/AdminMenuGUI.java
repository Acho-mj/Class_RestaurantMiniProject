package miniproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

public class AdminMenuGUI extends JFrame {

    private Restaurant restaurant;
    private JTable menuTable;
    private DefaultTableModel menuTableModel;
    private JTextField searchField;
    private JTextField menuNameField;
    private JTextField menuPriceField;

    public AdminMenuGUI(Restaurant restaurant) {
        this.restaurant = restaurant;

        setTitle("메뉴 관리");
        setSize(530, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);
        setVisible(true);

        // 메뉴 검색 필드
        JLabel searchLabel = new JLabel("메뉴 이름 검색");
        searchLabel.setBounds(60, 50, 200, 30);
        panel.add(searchLabel);
        
        searchField = new JTextField();
        searchField.setBounds(170, 50, 150, 30);
        panel.add(searchField);

        // 메뉴 검색 버튼
        JButton searchButton = new JButton("검색");
        searchButton.setBounds(360, 50, 80, 30);
        panel.add(searchButton);
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                searchMenu(keyword);
            }
        });

        // 메뉴 Table
        String[] menuColumnNames = {"메뉴 이름", "메뉴 가격"};
        menuTableModel = new DefaultTableModel(null, menuColumnNames);
        menuTable = new JTable(menuTableModel);
        menuTable = new JTable(menuTableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 1; // 두 번째 열(가격)만 편집 가능하도록 설정
            }
        };
        
        JScrollPane menuScrollPane = new JScrollPane(menuTable);
        menuScrollPane.setBounds(50, 100, 400, 300);
        panel.add(menuScrollPane);
      
        // 메뉴 이름 필드
        JLabel menuNameLabel = new JLabel("메뉴 이름");
        menuNameLabel.setBounds(60, 420, 100, 30);
        panel.add(menuNameLabel);
        
        menuNameField = new JTextField();
        menuNameField.setBounds(120, 420, 100, 30);
        panel.add(menuNameField);

        // 메뉴 가격 필드
        JLabel menuPriceLabel = new JLabel("메뉴 가격");
        menuPriceLabel.setBounds(270, 420, 100, 30);
        panel.add(menuPriceLabel);
        
        menuPriceField = new JTextField();
        menuPriceField.setBounds(330, 420, 100, 30);
        panel.add(menuPriceField);

        // 메뉴 추가 버튼
        JButton addButton = new JButton("메뉴 추가");
        addButton.setBounds(60, 460, 100, 30);
        panel.add(addButton);
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String menuName = menuNameField.getText();
                String menuPriceText = menuPriceField.getText();
                try {
                	if (!menuName.isEmpty() && !menuPriceText.isEmpty()) {
                        double menuPrice = Double.parseDouble(menuPriceText);
                        addMenu(menuName, menuPrice);
                    } else {
                        JOptionPane.showMessageDialog(AdminMenuGUI.this, "메뉴 이름과 메뉴 가격을 입력하세요.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminMenuGUI.this, "올바른 메뉴 가격을 입력하세요.");
                }
            }
        });
        
        // 메뉴 가격 수정 버튼 
        JButton editPriceButton = new JButton("메뉴 가격 수정");
        editPriceButton.setBounds(180, 460, 140, 30);
        panel.add(editPriceButton);
        
        editPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = menuTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String newPriceText = menuTable.getValueAt(selectedRow, 1).toString();
                    String menuName = menuTableModel.getValueAt(selectedRow, 0).toString();
                    try {
                        double newPrice = Double.parseDouble(newPriceText);
                        updateMenuPrice(menuName, newPrice);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(AdminMenuGUI.this, "올바른 메뉴 가격을 입력하세요.");
                    }
                }
            }
        });

        // 메뉴 삭제 버튼
        JButton deleteButton = new JButton("메뉴 삭제");
        deleteButton.setBounds(340, 460, 100, 30);
        panel.add(deleteButton);
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String menuName = menuNameField.getText();
                if (!menuName.isEmpty()) {
                    deleteMenu(menuName);
                }
            }
        });

        // 데이터 저장 버튼
        JButton saveDataButton = new JButton("데이터 저장하기");
        saveDataButton.setBounds(50, 500, 130, 30);
        panel.add(saveDataButton);
        
        saveDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = "restaurant.dat";
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                    restaurant.saveData(out);
                    JOptionPane.showMessageDialog(AdminMenuGUI.this, "데이터가 저장되었습니다.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // 관리자 모드 이동 버튼
        JButton adminButton = new JButton("관리자 화면으로 이동하기");
        adminButton.setBounds(230, 500, 230, 30);
        panel.add(adminButton);
        
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                try {
                    new AdminGUI(restaurant); 
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        
        displayAllMenus();

        
    }

    private void searchMenu(String keyword) {
        menuTableModel.setRowCount(0);
        ArrayList<Menu> menus = restaurant.getMenuList();
        for (Menu menu : menus) {
            if (menu.getName().contains(keyword)) {
                menuTableModel.addRow(new Object[]{menu.getName(), menu.getPrice()});
            }
        }
    }

    private void addMenu(String menuName, double menuPrice) {
        Menu menu = restaurant.getMenu(menuName);
        if (menu == null) {
            restaurant.addMenu(menuName, menuPrice);
            JOptionPane.showMessageDialog(this, "메뉴가 추가되었습니다.");
            displayAllMenus();
        } else {
            JOptionPane.showMessageDialog(this, "같은 이름의 메뉴가 이미 존재합니다.");
        }
    }
    
    private void updateMenuPrice(String menuName, double newPrice) {
        Menu menu = restaurant.getMenu(menuName);
        if (menu != null) {
            menu.setPrice(newPrice);
            JOptionPane.showMessageDialog(this, "메뉴 가격이 수정되었습니다.");
            displayAllMenus();
        } else {
            JOptionPane.showMessageDialog(this, "메뉴가 존재하지 않습니다.");
        }
    }

    private void deleteMenu(String menuName) {
        Menu menu = restaurant.getMenu(menuName);
        if (menu != null) {
            restaurant.deleteMenu(menuName);
            JOptionPane.showMessageDialog(this, "메뉴가 삭제되었습니다.");
            displayAllMenus();
        } else {
            JOptionPane.showMessageDialog(this, "해당 이름의 메뉴가 존재하지 않습니다.");
        }
    }

    private void displayAllMenus() {
        menuTableModel.setRowCount(0);
        ArrayList<Menu> menus = restaurant.getMenuList();
        for (Menu menu : menus) {
            menuTableModel.addRow(new Object[]{menu.getName(), menu.getPrice()});
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
                    new AdminMenuGUI(restaurant);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
