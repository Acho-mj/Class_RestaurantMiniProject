package miniproject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class FileSender extends JFrame {
    private JTextField fileNameField;
    private JTextField ipAddressField;
    private JTextField portNumberField;

    public FileSender() {
        setTitle("File Sender");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        fileNameField = new JTextField(20);
        ipAddressField = new JTextField(15);
        portNumberField = new JTextField(5);

        JButton sendButton = new JButton("파일 전송");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendFile();
            }
        });

        panel.add(new JLabel("파일명 : "));
        panel.add(fileNameField);
        panel.add(new JLabel("IP 주소 : "));
        panel.add(ipAddressField);
        panel.add(new JLabel("포트 번호 : "));
        panel.add(portNumberField);
        panel.add(sendButton);

        add(panel);
        setVisible(true);
    }

    private void sendFile() {
    	try (Socket socket = new Socket(ipAddressField.getText(), Integer.parseInt(portNumberField.getText()));
    		     BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
    		     FileInputStream fis = new FileInputStream(fileNameField.getText())) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) > 0) {
                bos.write(buffer, 0, bytesRead);
            }

            JOptionPane.showMessageDialog(this, "성공");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "에러 : " + ex.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileSender();
            }
        });
    }

}
