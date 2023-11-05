package miniproject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AdminGUI extends JFrame{
		private Restaurant restaurant;
		private JTable tableListTable;

	    public AdminGUI(Restaurant restaurant) throws IOException {
	        ArrayList<Table> tableList = restaurant.getTableList(); // 레스토랑 객체로부터 테이블 목록을 가져옴
	        setTitle("관리자 모드");
	        setSize(400, 400);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        JPanel adminPanel = new JPanel();
	        adminPanel.setLayout(null); // 레이아웃 매니저를 null로 설정
	        add(adminPanel);
	        setVisible(true);
	        
	        // 메뉴 관리 버튼
	        JButton MenuButton = new JButton("메뉴 관리");
	        MenuButton.setBounds(60, 70, 100, 30);
	        adminPanel.add(MenuButton);
	         
	        MenuButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	AdminMenuGUI adminMenuGUI = new AdminMenuGUI(restaurant);
	            	adminMenuGUI.setVisible(true);
	            }
	        });
	        
	        // 테이블 관리 버튼
	        JButton tableButton = new JButton("테이블 관리");
	        tableButton.setBounds(240, 70, 100, 30);
	        adminPanel.add(tableButton);	       
	        
	        tableButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	AdminTableGUI adminTableGUI = new AdminTableGUI(restaurant);
	            	adminTableGUI.setVisible(true); 
	            }
	        });
	        
	        
	        String[] columnNames = {"테이블 이름", "수용 인원", "이용 가능 여부"};
			// 데이터 배열
	        Object[][] data = new Object[tableList.size()][3];
	        for (int i = 0; i < tableList.size(); i++) {
	            Table table = tableList.get(i);
	            data[i][0] = table.getTableName();
	            data[i][1] = table.getCapacity();
	            data[i][2] = table.isAvailable();
	        }
	        
	        // 테이블 모델
	        DefaultTableModel model = new DefaultTableModel(data, columnNames);

	        // JTable 생성
	        tableListTable = new JTable(model);
	        tableListTable.setBounds(50, 120, 300, 150);
	        tableListTable.setDefaultEditor(Object.class, null); // 셸 편집 비활성화

	        JScrollPane scrollPane = new JScrollPane(tableListTable);
	        scrollPane.setBounds(50, 120, 300, 150);
	        adminPanel.add(scrollPane);

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

