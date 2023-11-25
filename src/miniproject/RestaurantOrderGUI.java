package miniproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RestaurantOrderGUI extends JFrame {

    private Restaurant restaurant;
    private String selectedMenu;
    private int selectedQuantity;
    
    private RestaurantGUI restaurantGUI;
    private Table selectedTable;

    public RestaurantOrderGUI(Restaurant restaurant, RestaurantGUI restaurantGUI, Table selectedTable) throws IOException {
    	this.restaurant = restaurant;
        this.restaurantGUI = restaurantGUI;
        this.selectedTable = selectedTable;

        setTitle("주문");
        setSize(630, 430); // 크기 조정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null); // 레이아웃 매니저를 null로 설정

        // 메뉴 버튼 패널
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 4));

        ArrayList<Menu> menuList = restaurant.getMenuList();
        int buttonSize = 100;

        for (int i = 0; i < menuList.size(); i++) {
            Menu menu = menuList.get(i);
            JButton menuButton = new JButton(menu.getName());
            menuButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
            menuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedMenu = menu.getName();
                    System.out.println("선택된 메뉴: " + selectedMenu);
                }
            });

            menuPanel.add(menuButton);
        }

        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        menuScrollPane.setBounds(10, 10, 500, 200); // 추가
        panel.add(menuScrollPane);

        // 수량 버튼 패널
        JPanel quantityPanel = new JPanel();
        quantityPanel.setBounds(10, 250, 300, 100);
        quantityPanel.setLayout(new GridLayout(0, 5)); // GridLayout 사용

        for (int i = 1; i <= 10; i++) {
            JButton quantityButton = new JButton(Integer.toString(i));
            quantityButton.setPreferredSize(new Dimension(40, 40));
            quantityButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedQuantity = Integer.parseInt(quantityButton.getText());
                    System.out.println("선택된 수량: " + selectedQuantity);
                }
            });

            int x = 10 + (i - 1) * (40 + 10);
            int y = 10;
            quantityButton.setBounds(x, y, 40, 40);

            quantityPanel.add(quantityButton);
        }

        JScrollPane quantityScrollPane = new JScrollPane(quantityPanel);
        quantityScrollPane.setBounds(10, 250, 300, 100); // 추가
        panel.add(quantityScrollPane);

        // 주문 및 매니저 버튼 패널
        JButton orderButton = new JButton("주문 완료");
        orderButton.setBounds(350, 250, 100, 30);
        panel.add(orderButton);
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderCompleted();
            }
        });
        
        JButton saveDataButton = new JButton("데이터저장");
        saveDataButton.setBounds(330, 300, 150, 30);
        panel.add(saveDataButton);
        saveDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = "restaurant.dat";
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                    restaurant.saveData(out);
                    JOptionPane.showMessageDialog(RestaurantOrderGUI.this, "데이터가 저장되었습니다.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton managerButton = new JButton("매니저 모드로 이동");
        managerButton.setBounds(330, 350, 150, 30);
        panel.add(managerButton);
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RestaurantGUI restaurantGUI = new RestaurantGUI(restaurant);
                restaurantGUI.setVisible(true);
            }
        });

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void orderCompleted() {
        if (selectedMenu != null && selectedQuantity > 0 && restaurantGUI != null) {
            // 주문이 완료되면 RestaurantGUI의 orderCompleted 메서드를 호출하여 정보를 전달
            restaurantGUI.orderCompleted(selectedMenu, selectedQuantity);
            
        } else {
            System.out.println("메뉴와 수량을 선택해주세요.");
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
                    
                    Table defaultTable = restaurant.getTableList().get(0);
                    
                    RestaurantGUI restaurantGUI = new RestaurantGUI(restaurant);
                    
                    new RestaurantOrderGUI(restaurant, restaurantGUI, defaultTable);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
