package miniproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;

public class ManagerOrderGUI extends JFrame{
	private Restaurant restaurant;
    private JTable menuTable;
    private DefaultTableModel menuTableModel;

    public ManagerOrderGUI(Restaurant restaurant) throws IOException {
        setTitle("주문");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // 메뉴 정보를 표시할 JTable 생성
        String[] menuColumnNames = {"메뉴 이름", "메뉴 가격", "수량"};
        menuTableModel = new DefaultTableModel(menuColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // 수량 필드만 편집 가능하도록 설정
            }
        };
        menuTable = new JTable(menuTableModel);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBounds(50, 50, 400, 200);
        panel.add(scrollPane);

        // 메뉴 목록을 테이블에 추가
        ArrayList<Menu> menuList = restaurant.getMenuList();
        for (Menu menu : menuList) {
            menuTableModel.addRow(new Object[]{menu.getName(), menu.getPrice(), 0}); // 초기 수량은 0
        }

        JButton orderButton = new JButton("주문완료");
        orderButton.setBounds(170, 300, 150, 30);

        panel.add(orderButton);

        add(panel);
        setVisible(true);
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
