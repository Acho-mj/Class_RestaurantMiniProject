package miniproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class ManagerGUI extends JFrame {
    private Restaurant restaurant;
    private static final String ADMIN_PASSWORD = "1234";
    
    public ManagerGUI(Restaurant restaurant) {
        this.restaurant = restaurant;
        setTitle("매니저 모드");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel managerPanel = new JPanel();
        managerPanel.setLayout(null); // 레이아웃 매니저를 null로 설정
        
        JButton adminButton = new JButton("관리자 모드");
        adminButton.setBounds(250, 20, 100, 30);
        
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 비밀번호 입력 다이얼로그 표시
                String inputPassword = JOptionPane.showInputDialog("비밀번호를 입력하세요:");
                if (inputPassword != null && inputPassword.equals(ADMIN_PASSWORD)) {
                    // 비밀번호가 일치하면 AdminGUI로 이동
                    dispose();
                    try {
                        new AdminGUI(restaurant);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // 비밀번호가 일치하지 않을 경우 메시지 표시
                    JOptionPane.showMessageDialog(ManagerGUI.this, "비밀번호가 일치하지 않습니다.");
                }
            }
        });
        
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
