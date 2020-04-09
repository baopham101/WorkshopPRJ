
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class ChatPanel extends JPanel implements ActionListener {

    //Main Panel
    JPanel controlPanel;
    JPanel panelMessage;
    JScrollPane writePanel;
    JTextArea txtMessage;
    JButton btnSend, btnSendFile;
    JScrollPane viewPanel;
    JTextArea txtMessages;
    //Socket part
    Socket socket = null;
    BufferedReader bf = null;
    DataOutputStream os = null;
    OutputThread t = null;
    String sender;
    String receiver;

    public ChatPanel(Socket s, String sender, String receiver) {
        initComponents();
        socket = s;
        this.sender = sender;
        this.receiver = receiver;
        try {
            bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new DataOutputStream(socket.getOutputStream());
            t = new OutputThread(s, txtMessages, sender, receiver);
            t.start();
        } catch (Exception e) {
        }
    }

    public JTextArea getTxtMessages() {
        return this.txtMessages;
    }

    private void initComponents() {
        setLayout(new GridLayout(2, 1, 5, 5));
        //Chat part
        txtMessage = new JTextArea();
        panelMessage = new JPanel(new FlowLayout());//GridLayout(1, 2, 10, 10));
        writePanel = new JScrollPane(txtMessage);
        writePanel.setPreferredSize(new Dimension(340, 200));
        txtMessage.setEditable(true);
        btnSend = new JButton("Send");
        btnSend.setPreferredSize(new Dimension(170, 100));
        btnSend.addActionListener(this);
        btnSendFile = new JButton("Send File");
        btnSendFile.setPreferredSize(new Dimension(170, 100));
        btnSendFile.addActionListener(this);
        panelMessage.add(writePanel);
        panelMessage.add(btnSend);
        panelMessage.add(btnSendFile);
        //View Part
        txtMessages = new JTextArea();
        viewPanel = new JScrollPane(txtMessages);
        viewPanel.setPreferredSize(new Dimension(700, 200));
        txtMessages.setEditable(false);
        add(viewPanel);
        add(panelMessage);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSend) {
            if (txtMessage.getText().trim().length() == 0) {
                return;
            }
            try {
                os.writeBytes(txtMessage.getText());
                os.write(13);
                os.write(10);
                os.flush();
                this.txtMessages.append("\n" + sender + ": " + txtMessage.getText());
                txtMessage.setText("");
            } catch (Exception e) {
            }
        }
        if (ae.getSource() == btnSendFile) {
            String dir = txtMessage.getText().trim();
            String fileContain = getFileContain(dir);
            try {
                //String str = bf.toString();
                writeFileContain(fileContain);
                os.writeBytes(sender+ " has send a file");
                os.write(13);
                os.write(10);
                os.flush();
                this.txtMessages.append("\n" + sender + " has send a file.");
                txtMessage.setText("");
            } catch (Exception e) {
            }
        }
    }

    private String getFileContain(String filename) {
        String result = null;
        try {
            File file = new File(filename);
            if (!file.exists() || !file.isFile()) {
                JOptionPane.showMessageDialog(this, "File not found");
                return null;
            }
            result = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "@" + result;
    }

    private void createFile(String contain) {
        try {
            PrintWriter pw = new PrintWriter("fileSend.txt");
            pw.println(contain);
            pw.close();
        } catch (Exception e) {
        }
    }

    private void writeFileContain(String x) {
        String result = x.substring(1);
        createFile(result);
    }
}
