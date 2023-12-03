package miniproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;

public class RestaurantTableGUI extends JFrame {

    private Restaurant restaurant;
    private JTextField guestCountField;
    private JSlider guestCountSlider;
    private JPanel tablePanel; 
    private JScrollPane scrollPane; 
    private Table selectedTable;

    public RestaurantTableGUI(Restaurant restaurant) throws IOException {
        this.restaurant = restaurant;

        setTitle("테이블 배정");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);
        setVisible(true);

        JLabel guestCountLabel = new JLabel("손님 수 :");
        guestCountLabel.setBounds(150, 50, 150, 30);
        panel.add(guestCountLabel);

        guestCountSlider = new JSlider(1, 10);
        guestCountSlider.setBounds(200, 50, 200, 50);
        guestCountSlider.setMajorTickSpacing(1);
        guestCountSlider.setPaintTicks(true);
        guestCountSlider.setPaintLabels(true);
        panel.add(guestCountSlider);

        JButton showTablesButton = new JButton("입력");
        showTablesButton.setBounds(400, 50, 80, 30);
        panel.add(showTablesButton);

        
        tablePanel = new JPanel();
        tablePanel.setLayout(null);

       
        scrollPane = new JScrollPane(tablePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(50, 100, 500, 350);
        panel.add(scrollPane);

        showTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int guestCount = guestCountSlider.getValue();
                displayAvailableTables(guestCount);
            }
        });

        // 테이블 Buttons
        updateTableButtons();

        JButton assignButton = new JButton("배정 완료");
        assignButton.setBounds(250, 500, 100, 30);
        panel.add(assignButton);

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedTable != null) {
                    selectedTable.tableCompleted();
                    updateTableButtons(); 
                }
            }
        });

        JButton saveDataButton = new JButton("데이터 저장하기");
        saveDataButton.setBounds(50, 500, 130, 30);
        panel.add(saveDataButton);

        saveDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = "restaurant.dat";
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                    restaurant.saveData(out);
                    JOptionPane.showMessageDialog(RestaurantTableGUI.this, "데이터가 저장되었습니다.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton managerButton = new JButton("매니저 화면으로 이동하기");
        managerButton.setBounds(400, 500, 180, 30);
        panel.add(managerButton);

        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	RestaurantGUI restaurantGUI = new RestaurantGUI(restaurant);
            	restaurantGUI.setVisible(true);
            }
        });

        displayAllTables();
    }

    private void updateTableButtons() {
        tablePanel.removeAll();
        ArrayList<Table> tables = restaurant.getTableList();
        int x = 50;
        int y = 0;

        for (Table table : tables) {
            JButton tableButton = new JButton(table.getTableName() + " (" + table.getCapacity() + ")");
            tableButton.setBounds(x, y, 120, 60);
            tableButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedTable = table;
                    updateOrderDetails(table);
                }
            });

            if (!table.isAvailable()) {
                tableButton.setBackground(Color.YELLOW);
            }

            tablePanel.add(tableButton);

            x += 130;

            if (x > 400) {
                x = 50;
                y += 70;
            }
        }

        tablePanel.setPreferredSize(new Dimension(500, y + 70));
        scrollPane.setViewportView(tablePanel);
        repaint();
        revalidate();
    }

    private void displayAvailableTables(int guestCount) {
        ArrayList<Table> tables = restaurant.getTableList();
        for (Table table : tables) {
            JButton tableButton = findTableButton(table.getTableName());
            if (table.getCapacity() >= guestCount && table.isAvailable() == true) {
                tableButton.setVisible(true);
            } else {
                tableButton.setVisible(false);
            }
        }
    }

    private JButton findTableButton(String tableName) {
        Component[] components = tablePanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().startsWith(tableName)) {
                    return button;
                }
            }
        }
        return null;
    }

    private void updateOrderDetails(Table selectedTable) {
    }

    private void displayAllTables() {
        updateTableButtons();
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
                    new RestaurantTableGUI(restaurant);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
