package miniproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class RestaurantGUI extends JFrame {

    private Restaurant restaurant;
    private JTextArea orderDetailsTextArea; // 주문 내역을 표시하는 JTextArea
    private Table selectedTable; // 선택된 테이블을 저장하는 변수

    public RestaurantGUI(Restaurant restaurant) {
        this.restaurant = restaurant;

        // GUI 컴포넌트 생성
        JPanel tablePanel = new JPanel(new GridLayout(0, 5)); // 테이블을 표시할 패널
        orderDetailsTextArea = new JTextArea(10, 20); 
        orderDetailsTextArea.setEditable(false);

        JButton assignTableButton = new JButton("테이블배정");
        assignTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RestaurantTableGUI ex1 = null;
                try {
                    ex1 = new RestaurantTableGUI(restaurant);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                ex1.setVisible(true);
            }
        });

        JButton orderButton = new JButton("주문");
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 테이블을 선택하지 않은 경우 메시지를 표시하고 종료
                if (selectedTable == null) {
                    JOptionPane.showMessageDialog(RestaurantGUI.this, "테이블을 선택하세요.");
                    return;
                }

                // 테이블 배정이 완료되지 않은 경우
                if (selectedTable.isAvailable() == true) {
                    JOptionPane.showMessageDialog(RestaurantGUI.this, "테이블 배정 먼저 진행해주세요.");
                    return;
                }

                // ex2 화면으로 이동
                try {
                    RestaurantOrderGUI rog = new RestaurantOrderGUI(restaurant, RestaurantGUI.this, selectedTable);
                    rog.setVisible(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton checkOutButton = new JButton("체크아웃");
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 테이블 배정이 완료되지 않은 경우
                if (selectedTable.isAvailable() == true) {
                    JOptionPane.showMessageDialog(RestaurantGUI.this, "테이블 배정 먼저 진행해주세요.");
                    return;
                }

                if (selectedTable != null) {
                    double totalSum = calculateTotalSum(selectedTable);

                    // 확인 다이얼로그 표시
                    int option = JOptionPane.showConfirmDialog(RestaurantGUI.this,
                            "테이블 총 합계 " + totalSum + " 원 체크아웃하시겠습니까?",
                            "체크아웃 확인", JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        selectedTable.guestDeparted();
                        orderDetailsTextArea.setText("");
                        // 테이블 버튼 색상 업데이트
                        updateTableButtonColors();
                    }
                }
            }
        });

        JButton saveDataButton = new JButton("데이터저장");
        saveDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = "restaurant.dat";
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                    restaurant.saveData(out);
                    JOptionPane.showMessageDialog(RestaurantGUI.this, "데이터가 저장되었습니다.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton adminButton = new JButton("관리자 모드");
        adminButton.setBounds(250, 20, 100, 30);

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputPassword = JOptionPane.showInputDialog("비밀번호를 입력하세요:");
                if (inputPassword != null && inputPassword.equals("1234")) {
                    dispose();
                    try {
                        new AdminGUI(restaurant);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(RestaurantGUI.this, "비밀번호가 일치하지 않습니다.");
                }
            }
        });

        // 사이드 버튼을 담을 패널 생성
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        // 사이드 버튼을 패널에 추가
        sidePanel.add(assignTableButton);
        sidePanel.add(orderButton);
        sidePanel.add(checkOutButton);
        sidePanel.add(saveDataButton);
        sidePanel.add(adminButton);

        // 프레임에 컴포넌트 추가
        add(tablePanel, BorderLayout.CENTER);
        add(orderDetailsTextArea, BorderLayout.SOUTH);
        add(sidePanel, BorderLayout.EAST);

        // 프레임 속성 설정
        setTitle("레스토랑 관리 시스템");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 테이블 버튼 생성
        populateTableButtons(tablePanel);

        // 테이블 가용성에 따라 테이블 버튼 색상 업데이트
        updateTableButtonColors();

        // 프레임 표시
        setVisible(true);
    }

    // ex2 화면에서 주문이 완료되었을 때 호출될 메서드
    public void orderCompleted(String selectedMenu, int selectedQuantity) {
        if (selectedMenu != null && selectedQuantity > 0 && selectedTable != null) {
            Order newOrder = new Order(selectedMenu, restaurant.getMenu(selectedMenu).getPrice(), selectedQuantity);
            selectedTable.addOrder(newOrder);

            System.out.println("주문이 완료되었습니다.");

            // 주문이 완료되면 RestaurantGUI의 updateOrderDetails 메서드를 호출하여 주문 내역을 갱신
            updateOrderDetails(selectedTable);
        } else {
            System.out.println("메뉴와 수량을 선택해주세요.");
        }
    }

    private void populateTableButtons(JPanel tablePanel) {
        ArrayList<Table> tableList = restaurant.getTableList();

        for (Table table : tableList) {
            JButton tableButton = new JButton(table.getTableName());
            tableButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 테이블 버튼 클릭 처리
                    updateOrderDetails(table);
                }
            });

            tablePanel.add(tableButton);
        }
    }

    private void updateOrderDetails(Table selectedTable) {
        this.selectedTable = selectedTable; // 선택된 테이블 저장

        StringBuilder orderDetails = new StringBuilder();
        double totalSum = 0.0;

        // 폰트 크기 조절
        Font largerFont = new Font(orderDetailsTextArea.getFont().getName(), Font.PLAIN, 16);
        orderDetailsTextArea.setFont(largerFont);

        orderDetails.append("주문 내역:\n");
        for (Order order : selectedTable.getOrderList()) {
            orderDetails.append(order.getMenu().getName())
                    .append(" x").append(order.getQuantity())
                    .append(": ").append(order.pay()).append("원\n");

            totalSum += order.pay();
        }

        orderDetails.append("\n총 주문 금액: ").append(totalSum).append("원\n");

        // 행과 열의 개수 조절
        orderDetailsTextArea.setRows(8);
        orderDetailsTextArea.setColumns(20);

        orderDetailsTextArea.setText(orderDetails.toString());
    }

    private void updateTableButtonColors() {
        ArrayList<Table> tableList = restaurant.getTableList();
        Component[] components = ((JPanel) getContentPane().getComponent(0)).getComponents();

        for (int i = 0; i < tableList.size(); i++) {
            Table table = tableList.get(i);
            JButton tableButton = (JButton) components[i];

            if (table.isAvailable() == false) {
                tableButton.setBackground(Color.YELLOW);
            } else {
                tableButton.setBackground(null); // 기본 색상으로 설정
            }
        }
    }

    // 선택된 테이블의 주문 총액을 계산
    private double calculateTotalSum(Table table) {
        double totalSum = 0.0;
        for (Order order : table.getOrderList()) {
            totalSum += order.pay();
        }
        return totalSum;
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
                    new RestaurantGUI(restaurant);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
