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
import java.util.ArrayList;

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
import javax.swing.text.StyledDocument;

import vc.min.chat.Server.IO.ClientSocket;
import vc.min.chat.Shared.Packets.Packet;
import vc.min.chat.Shared.Packets.Packet0Login;
import vc.min.chat.Shared.Packets.Packet1Disconnect;
import vc.min.chat.Shared.Packets.Packet3Message;
import vc.min.chat.Shared.Packets.Packet4ListClients;
import vc.min.chat.Shared.Packets.Packet5PM;
import vc.min.chat.Shared.Packets.PacketHandler;


public class Client {
    
	static Client c = new Client();
	
    private Socket connection = null;
    
    private JLabel text;
    private JTextArea output;
    private JScrollPane scrollPane;
    private JTextField input;
    private JTextField inputPM;
    private JButton sendButton;
    private JButton sendButton2;
    private JButton sendButton3;
    private JButton listClientsButton;
    private JFrame frame;
    private String currentName;
    private JDialog aboutDialog;
    
    private JTextField input2;
    

    
    private DataOutputStream dos;
    private DataInputStream dis;

	private boolean running;

	private PacketHandler packetHandler;
    
	private ArrayList<String> clients;
    public Client() {
    	text = new JLabel();
        output = new JTextArea(10,50);
        scrollPane = new JScrollPane(output, 
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        input = new JTextField(50);
        input2 = new JTextField(20);
        sendButton = new JButton("Send");
        sendButton2 = new JButton("Login");
        sendButton3 = new JButton("Send PM to:");
        listClientsButton = new JButton("List Users");
       
        inputPM = new JTextField(10);
        
    }
    
    
    public void launchFrame() {
        frame = new JFrame("Chat Room");
        
        // Use the Border Layout for the frame
        frame.setLayout(new BorderLayout());
        
        frame.add(scrollPane, BorderLayout.WEST);
        frame.add(input, BorderLayout.SOUTH);
        frame.add(input2, BorderLayout.SOUTH);
        
        // Create the button panel
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(5,1));
        p.add(sendButton);
        p.add(sendButton2);
        p.add(listClientsButton);
        p.add(sendButton3);
        p.add(inputPM);
        
        sendButton.setVisible(false);
        sendButton3.setVisible(false);
        
        // Add the button panel to the center
        frame.add(p, BorderLayout.CENTER);
        
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
        sendButton2.addActionListener(new SendUsername());
        sendButton3.addActionListener(new SendPMHandler());
        inputPM.addActionListener(new SendPMHandler());
        listClientsButton.addActionListener(new SendClientsHandler());
        input.addActionListener(new SendHandler());
        input2.addActionListener(new SendUsername());
        frame.addWindowListener(new CloseHandler());
        
        
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
            packetHandler = new PacketHandler(dis, dos);
            //Packet packet = clientSocket.getPacketHandler().readPacket(packetID);
            
            
            // Launch the reader thread
            Thread t = new Thread(new RemoteReader(dis, packetHandler, this));
            t.start();
            setRunning(true);
            Thread kam = new Thread(new KeepAliveManager(packetHandler, this));
            kam.start();
            
        } catch (Exception e) {
            System.err.println("Unable to connect to server!");
            e.printStackTrace();
        }
    }
    
    private class SendHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = input2.getText();

            Packet3Message packetm = new Packet3Message(text, "");
            try {
            	packetHandler.writePacket(packetm);
            } catch (IOException e1) {
            	System.err.print("Send hanler message error!");
            }

            input2.setText("");
        }
    }
    
    private class SendUsername implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = input2.getText();
            //text = usernames.getSelectedItem() + ": " + text + "\n";
            currentName = text;
            Packet0Login packet0login = new Packet0Login(text);
            
            try {
            	packetHandler.writePacket(packet0login);
            } catch(IOException e1) {
            	System.err.print("Write packet error!");
            }
            sendButton.setVisible(true);
            sendButton2.setVisible(false);
            sendButton3.setVisible(true);
            input2.setText("");
        }
    }
    
    private class SendClientsHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = input2.getText();
            //text = usernames.getSelectedItem() + ": " + text + "\n";
            Packet4ListClients packet4listclients = new Packet4ListClients(true);
            
            try {
            	packetHandler.writePacket(packet4listclients);
            } catch(IOException e1) {
            	System.err.print("Write packet error!");
            }
 
    	    System.out.println("List Clients");
            
        }
    }
    
    private class SendPMHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = input2.getText();
            String name = inputPM.getText();
            //text = usernames.getSelectedItem() + ": " + text + "\n";
            
            Packet5PM packet5pm = new Packet5PM(name, currentName, text);
            try {
            	packetHandler.writePacket(packet5pm);
            } catch(IOException e1) {
            	System.err.print("Write packet error!");
            }
            
            input2.setText("");
            inputPM.setText("");
 
    	    //System.out.println("List Clients");
            
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
    	
    	// Packet packet = clientSocket.getPacketHandler().readPacket(packetID);
    	DataInputStream dis;
    	PacketHandler packetHandler;
    	int read;
    	Client client;
    	public RemoteReader(DataInputStream dis, PacketHandler packetHandler, Client client) {
    		this.dis = dis;
    		this.packetHandler = packetHandler;
    		this.client = client;
    	}
    	
        public void run() {
            try {
                while ( client.isRunning()) {
                   read = dis.read();
                   Packet packet = this.packetHandler.readPacket(read);
                   
                   switch (read) {
	                   case 0: 
	                	    Packet0Login packet0login = (Packet0Login) packet;
	                	    output.append("Welcome " + packet0login.username + "\n");
	                   		break;
	                   case 127: 
	                   	    //Packet0Login packet0login = (Packet0Login) packet;
	                   	    
	                   	    //System.out.println("Greetings");
	                	    output.append("Greetings, type name and login\n");
	                   		break;
	                   case 1: 
	                	    Packet1Disconnect packet1disconnect = (Packet1Disconnect) packet;
	                	    output.append(packet1disconnect.message);
	                	    //System.out.println("Disconnect");
	                	    client.setRunning(false);
	                   		break;
	                   case 2: 
	                	    //System.out.println("Keep-Alive");
                  			break;
	                   case 3:
	                	    Packet3Message packet3 = (Packet3Message) packet;
	                	    output.append(packet3.from + "\n");
	                	    output.append(packet3.message + "\n");
	                	    
	                	   // System.out.println("Got a message");
	                	    break;
	                   case 4: 
	                	    
	                	   	Packet4ListClients packet1 = (Packet4ListClients) packet;
	                	   	clients = packet1.clients;
	                	   	
	                	   	for (int i = 0; i < clients.size(); i++) {
	                	   		output.append((i+1) + " " + clients.get(i) + "\n");
	                	   	}
	                	   
	                	    System.out.println("List Clients");
             				break;
	                   case 5: 
	                	   
	                	   	Packet5PM packet5pm = (Packet5PM) packet;
	                	   	//output.append(packet5pm.fromUsername + " -> " + packet5pm.toUsername + "\n" + packet5pm.message + "\n");
	                	    //System.out.println("Personal Message");
                 			break;                 
                   }
                }
            } catch (Exception e) {
                System.err.println("Error while reading from server.");
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        
        c.launchFrame();
    }

	public void setRunning(boolean b) {
		this.running = b;
		
	}

	public boolean isRunning() {
		return running;
	}
}