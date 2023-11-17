package miniproject;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileGUI {
    private Restaurant restaurant;

    public FileGUI(Restaurant restaurant) {
        this.restaurant = restaurant;
        String fileName = "restaurant.dat";
        File file = new File(fileName);

        if (!file.exists()) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "파일이 존재하지 않습니다. 파일을 생성하시겠습니까?",
                    "파일 생성 확인",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                try {
                    if (file.createNewFile()) {
                        JOptionPane.showMessageDialog(null, "파일이 정상적으로 생성되었습니다.");
                    } else {
                        JOptionPane.showMessageDialog(null, "파일 생성 중 오류가 발생했습니다.");
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "파일 생성 중 오류가 발생했습니다.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "프로그램을 종료합니다.");
                System.exit(0);
            }
        }

        // 파일이 없을 때도 ManagerGUI를 생성하고 프로그램을 계속 실행
        ManagerGUI managerGUI = new ManagerGUI(restaurant);
        managerGUI.setVisible(true);
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
                    new FileGUI(restaurant);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
