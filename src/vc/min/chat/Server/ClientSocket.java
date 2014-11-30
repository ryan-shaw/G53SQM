package vc.min.chat.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import vc.min.chat.Server.IO.ReaderThread;
import vc.min.chat.Server.IO.SenderThread;
import vc.min.chat.Shared.Packets.Packet;
import vc.min.chat.Shared.Packets.Packet127Disconnect;
import vc.min.chat.Shared.Packets.PacketHandler;

public class ClientSocket{
	
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

	private DataInputStream dis;

	private DataOutputStream dos;

	private PacketHandler packetHandler;
	
	/**
	 * Constructor to create the client
	 * @param socket
	 */
	public ClientSocket(Socket socket, Server server){
		this.socket = socket;
		this.server = server;
		running = true;
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
	
	public void sendPacket(Packet packet){
		st.queuePacket(packet);
	}
	
	public void close(String message){
		Packet127Disconnect packet = new Packet127Disconnect(message);
		sendPacket(packet);
		try {
			Thread.sleep(100); // Wait for packet to be sent
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = false;
	}
	
	// Getters and setters
	
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
}
