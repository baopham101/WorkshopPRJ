
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class ManagerChatter extends JFrame implements Runnable {

    ServerSocket srvSocket = null;
    BufferedReader br = null;
    Thread t;
    //Components
    JPanel controlPanel, managerPanel;
    JLabel lbMessage;
    JTextField txtServerPort;
    JTabbedPane tp;

    public ManagerChatter() {
        setLayout(new FlowLayout());
        initComponents();
        add(controlPanel);
        add(tp);
        String srcPortText = txtServerPort.getText().trim();
        int serverPort = Integer.parseInt(srcPortText);
        try {
            srvSocket = new ServerSocket(serverPort);
            this.lbMessage.setText("Mng. Server is running at the port ");
        } catch (Exception e) {
        }
        t = new Thread(this);
        t.start();
    }

    private void initComponents() {
        lbMessage = new JLabel("Manager Port:");
        lbMessage.setHorizontalAlignment(SwingConstants.RIGHT);
        lbMessage.setPreferredSize(new Dimension(300, 50));
        txtServerPort = new JTextField("12349");
        txtServerPort.setPreferredSize(new Dimension(300, 50));
        //Manager Panel
        managerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        managerPanel.add(lbMessage);
        managerPanel.add(txtServerPort);
        tp = new JTabbedPane();
        tp.setPreferredSize(new Dimension(700, 650));
        //Control panel
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(managerPanel);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket aStaffSocket = srvSocket.accept();
                if (aStaffSocket != null) {
                    br = new BufferedReader(new InputStreamReader(
                            aStaffSocket.getInputStream()));
                    String s = br.readLine();
                    int pos = s.indexOf(":");
                    String staffName = s.substring(pos + 1);
                    //Create a tab for this connection
                    ChatPanel p = new ChatPanel(aStaffSocket, "Manager", staffName);
                    tp.add(staffName, p);
                    p.updateUI();
                }
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        ManagerChatter c = new ManagerChatter();
        c.setDefaultCloseOperation(EXIT_ON_CLOSE);
        c.setSize(800, 800);
        c.setTitle("Manager");
        c.setResizable(false);
        c.setVisible(true);
    }
}
