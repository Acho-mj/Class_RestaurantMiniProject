package miniproject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver extends JFrame {
    private JTextField saveLocationField; //파일을 저장할 위치
    private JTextField portNumberField;  //서버 포트 번호를 입력

    public FileReceiver() {
        setTitle("File Receiver");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        saveLocationField = new JTextField(20);
        portNumberField = new JTextField(5);

        JButton startButton = new JButton("서버 시작");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        panel.add(new JLabel("파일 저장 경로 : "));
        panel.add(saveLocationField);
        panel.add(new JLabel("포트 번호 : "));
        panel.add(portNumberField);
        panel.add(startButton);

        add(panel);
        setVisible(true);
    }

    private void startServer() {
    	String saveLocation = saveLocationField.getText();
        int portNumber = Integer.parseInt(portNumberField.getText());

        // 디렉터리 생성
        File saveDir = new File(saveLocation);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        
        // 파일 저장 경로
        String filePath = saveLocation + File.separator + "receivedFile.dat";

    	//클라이언트의 연결을 기다림
    	try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(portNumberField.getText()));
    		     Socket socket = serverSocket.accept();
    		     BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
    		     FileOutputStream fos = new FileOutputStream(filePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);
            }

            JOptionPane.showMessageDialog(this, "성공");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "에러: " + ex.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileReceiver();
            }
        });
    }

}

