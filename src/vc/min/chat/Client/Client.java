package vc.min.chat.Client;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import vc.min.chat.Shared.Packets.PacketHandler;


public class Client {
    
    private Socket connection = null;
    
    private JTextArea output;
    private JScrollPane textPane;
    private JTextField input;
    private JButton sendButton;
    private JButton quitButton;
    private JFrame frame;
    private JComboBox usernames;
    private JDialog aboutDialog;
    private DataOutputStream dos;
    private DataInputStream dis;
    
    public Client() {
        output = new JTextArea(10,50);
        textPane = new JScrollPane(output, 
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        input = new JTextField(50);
        sendButton = new JButton("Send");
        quitButton = new JButton("Quit");
        usernames = new JComboBox();
    }
    
    public void launchFrame() {
        frame = new JFrame("Chat Room");
        
        // Use the Border Layout for the frame
        frame.setLayout(new BorderLayout());
        
        frame.add(textPane, BorderLayout.WEST);
        frame.add(input, BorderLayout.SOUTH);
        
        // Create the button panel
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3,1));
        p1.add(sendButton);
        p1.add(quitButton);
        p1.add(usernames);
        
        // Add the button panel to the center
        frame.add(p1, BorderLayout.CENTER);
        
        // Create menu bar and File menu
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        file.add(quitMenuItem);
        mb.add(file);
        frame.setJMenuBar(mb);
        
        // Add Help menu to menu bar
        JMenu help = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new AboutHandler());
        help.add(aboutMenuItem);
        mb.add(help);
        
        // Attach listener to the appropriate components
        sendButton.addActionListener(new SendHandler());
        input.addActionListener(new SendHandler());
        frame.addWindowListener(new CloseHandler());
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        frame.pack();
        frame.setVisible(true);
        
        doConnect();
    }
    
    private void doConnect() {
        // Initialize server IP and port information
        
        try {
            // Create the connection to the chat server
            connection = new Socket("localhost", 6111);
            
            // Prepare the input stream and store it in an instance variable
            dis = new DataInputStream(connection.getInputStream());
            dos = new DataOutputStream(connection.getOutputStream());
            PacketHandler packetHandler = new PacketHandler(dis, dos);
            // Launch the reader thread
            Thread t = new Thread(new RemoteReader());
            t.start();
            
            Thread kam = new Thread(new KeepAliveManager(packetHandler));
            kam.start();
            
        } catch (Exception e) {
            System.err.println("Unable to connect to server!");
            e.printStackTrace();
        }
    }
    
    private class SendHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = input.getText();
            text = usernames.getSelectedItem() + ": " + text + "\n";
            
            input.setText("");
        }
    }
    
    private class CloseHandler extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            exit();
        }
    }
    
    private void exit() {
        try {
            connection.close();
        } catch (Exception e) {
            System.err.println("Error while shutting down the server connection.");
        }
        System.exit(0);
    }
    
    private class AboutHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Create the aboutDialog when it is requested
            if ( aboutDialog == null ) {
                aboutDialog = new AboutDialog(frame, "About", true);
            }
            aboutDialog.setVisible(true);
        }
    }
    
    private class AboutDialog extends JDialog implements ActionListener  {
        public AboutDialog(Frame parent, String title, boolean modal) {
            super(parent,title,modal);
            add(new JLabel("The ChatClient is a neat tool that allows you to talk " +
                    "to other ChatClients via a ChatServer"),BorderLayout.NORTH);
            JButton b = new JButton("OK");
            add(b,BorderLayout.SOUTH);
            b.addActionListener(this);
            pack();
        }
        // Hide the dialog box when the OK button is pushed
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }
    
    private class RemoteReader implements Runnable {
        public void run() {
            try {
                while ( true ) {
                   
                 
                }
            } catch (Exception e) {
                System.err.println("Error while reading from server.");
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        Client c = new Client();
        c.launchFrame();
    }
}