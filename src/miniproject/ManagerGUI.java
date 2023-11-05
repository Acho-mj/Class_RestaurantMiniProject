package miniproject;

import javax.swing.*;
import java.awt.*;


public class ManagerGUI extends JFrame {
    private Restaurant restaurant;
    
    public ManagerGUI(Restaurant restaurant) {
        this.restaurant = restaurant;
        setTitle("매니저 모드");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel managerPanel = new JPanel();
        managerPanel.setLayout(null); // 레이아웃 매니저를 null로 설정
        
        JButton adminButton = new JButton("관리자 모드");
        adminButton.setBounds(250, 20, 100, 30);
        
        JButton tableButton = new JButton("테이블 배정");
        tableButton.setBounds(40, 150, 100, 30);
        
        JButton orderButton = new JButton("주문 및 체크아웃");
        orderButton.setBounds(200, 150, 150, 30);
        
        managerPanel.add(adminButton);
        managerPanel.add(tableButton);
        managerPanel.add(orderButton);

        add(managerPanel);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Restaurant restaurant = new Restaurant();
                new ManagerGUI(restaurant);
            }
        });
    }
}
