
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
public class ClientChatter extends JFrame implements ActionListener {

    Socket mngSocket = null;
    String mngIP = "";
    int mngPort = 0;
    String staffName = "";
    BufferedReader bf = null;
    DataOutputStream os = null;
    OutputThread t = null;
    //Components
    JPanel controlPanel,main;
    JLabel lbStaff, lbMngIP, lbPort;
    JTextField txtStaff, txtServerIP, txtServerPort;
    JButton btnConnect;

    public ClientChatter() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new FlowLayout());
        //Staff
        lbStaff = new JLabel("Staff: ");
        lbStaff.setHorizontalAlignment(SwingConstants.RIGHT);
        lbStaff.setPreferredSize(new Dimension(100, 30));
        txtStaff = new JTextField();
        txtStaff.setPreferredSize(new Dimension(100, 30));
        //IP
        lbMngIP = new JLabel("Mng IP: ");
        lbMngIP.setHorizontalAlignment(SwingConstants.RIGHT);
        lbMngIP.setPreferredSize(new Dimension(100, 30));
        txtServerIP = new JTextField();
        txtServerIP.setPreferredSize(new Dimension(100, 30));
        //Port
        lbPort = new JLabel("Port");
        lbPort.setHorizontalAlignment(SwingConstants.RIGHT);
        lbPort.setPreferredSize(new Dimension(100, 30));
        txtServerPort = new JTextField();
        txtServerPort.setPreferredSize(new Dimension(100, 30));
        //Button
        btnConnect = new JButton("Connect");
        btnConnect.setPreferredSize(new Dimension(100, 30));
        btnConnect.addActionListener(this);
        //Control
        controlPanel = new JPanel(new GridLayout(1, 7, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Staff and Server Info."));
        controlPanel.add(lbStaff);
        controlPanel.add(txtStaff);
        controlPanel.add(lbMngIP);
        controlPanel.add(txtServerIP);
        controlPanel.add(lbPort);
        controlPanel.add(txtServerPort);
        controlPanel.add(btnConnect);
        main=new JPanel(new BorderLayout());
        main.setPreferredSize(new Dimension(700, 650));
        add(controlPanel);
        add(main);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnConnect) {
            mngIP = this.txtServerIP.getText();
            mngPort = Integer.parseInt(this.txtServerPort.getText());
            staffName = this.txtStaff.getText();
            try {
                mngSocket = new Socket(mngIP, mngPort);
                if (mngSocket != null) {
                    ChatPanel p = new ChatPanel(mngSocket, staffName, "Manager");
                    main.add(p);
                    p.getTxtMessages().append("Managet is running\n");
                    p.updateUI();
                    //Get the socket input and output
                    bf = new BufferedReader(
                            new InputStreamReader(mngSocket.getInputStream()));
                    os = new DataOutputStream(mngSocket.getOutputStream());                  
                    //Annouce to manager
                    os.writeBytes("Staff: " + staffName);
                    os.write(12);
                    os.write(10);
                    os.flush();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Managet is not running");
                System.exit(0);
            }
        }
        btnConnect.setVisible(false);
    }
    public static void main(String[] args) {
        ClientChatter c = new ClientChatter();
        c.setDefaultCloseOperation(EXIT_ON_CLOSE);
        c.setSize(800, 800);
        c.setResizable(false);
        c.setTitle("Staff");
        c.setVisible(true);
    }
}
