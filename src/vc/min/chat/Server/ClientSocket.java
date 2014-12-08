package vc.min.chat.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import vc.min.chat.Server.IO.ReaderThread;
import vc.min.chat.Server.IO.SenderThread;
import vc.min.chat.Shared.Packets.Packet;
import vc.min.chat.Shared.Packets.Packet127Greeting;
import vc.min.chat.Shared.Packets.Packet1Disconnect;
import vc.min.chat.Shared.Packets.PacketHandler;

public class ClientSocket implements IClientSocket {
	
	/**
	 * Clients user name
	 */
	private String username;
	
	/**
	 * The client socket
	 */
	private Socket socket;

	/**
	 * Socket is running
	 */
	private boolean running;
	
	/**
	 * Input thread
	 */
	private ReaderThread rt;
	
	/**
	 * Output thread
	 */
	private SenderThread st;
	
	/**
	 * Main server instance
	 */
	public Server server;
	
	/**
	 * Input stream
	 */
	private DataInputStream dis;

	/**
	 * Output stream
	 */
	private DataOutputStream dos;

	/**
	 * Packet handler
	 */
	private PacketHandler packetHandler;
	
	private Long lastTimeRead;
	
	private ArrayList<Packet> packets;
	
	/**
	 * Constructor to create the client
	 * @param socket
	 */
	public ClientSocket(Socket socket, Server server){
		this.socket = socket;
		this.server = server;
		running = true;
		lastTimeRead = System.currentTimeMillis();
		packets = new ArrayList<Packet>();
		sendPacket(new Packet127Greeting());
		initReader();
		initSender();
		packetHandler = new PacketHandler(dis, dos);
	}

	/**
	 * Start the packet reader thread
	 */
	private void initReader(){
		try {
			dis = new DataInputStream(this.socket.getInputStream());
			rt = new ReaderThread(this);
			rt.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Start the packet sender thread
	 */
	private void initSender(){
		try {
			dos = new DataOutputStream(this.socket.getOutputStream());
			st = new SenderThread(this);
			st.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a packet to the send queue
	 * @param packet
	 */
	public void sendPacket(Packet packet){
		packets.add(packet);
	}
	
	public void close(String message){
		Packet1Disconnect packet = new Packet1Disconnect(message);
		sendPacket(packet);
		try {
			Thread.sleep(100); // Wait for packet to be sent
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = false;
	}
	
	// Getters and setters, self explanatory
	
	public PacketHandler getPacketHandler(){
		return packetHandler;
	}
	
	public DataOutputStream getOutputStream(){
		return dos;
	}
	
	public DataInputStream getInputStream(){
		return dis;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public ArrayList<Packet> getPacketQueue(){
		return packets;
	}
	
	public long getLastTimeRead(){
		return lastTimeRead;
	}
	
	public void setLastTimeRead(long time){
		this.lastTimeRead = time;
	}
}
