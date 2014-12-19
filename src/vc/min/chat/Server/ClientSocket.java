package vc.min.chat.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import vc.min.chat.Server.Logger.LogLevel;
import vc.min.chat.Server.Logger.Logger;
import vc.min.chat.Server.IO.ReaderThread;
import vc.min.chat.Server.IO.SenderThread;
import vc.min.chat.Shared.Packets.Packet;
import vc.min.chat.Shared.Packets.Packet0Login;
import vc.min.chat.Shared.Packets.Packet127Greeting;
import vc.min.chat.Shared.Packets.Packet1Disconnect;
import vc.min.chat.Shared.Packets.Packet2KeepAlive;
import vc.min.chat.Shared.Packets.Packet3Message;
import vc.min.chat.Shared.Packets.Packet4ListClients;
import vc.min.chat.Shared.Packets.Packet5PM;
import vc.min.chat.Shared.Packets.PacketHandler;

/**
 * 
 * @author Ryan Shaw
 *
 */
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
		/* Send the greeting packet to new client */
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
	 * Read and then decide what to do with the incoming packet
	 * @param Packet
	 * 		The packet object to process
	 */
	public void handlePacket(Packet packet) {
		setLastTimeRead(System.currentTimeMillis());
		int packetID = getPacketHandler().getPacketID(packet.getClass());
		// Check packets are able to be used by client.
		if(getUsername() == null && packetID > 2){
			close("only login, dc and ping packet available when not logged in");
			return;
		}
		switch(packetID){

			case 0:
				Packet0Login packet0login = (Packet0Login) packet;
				Logger.log(LogLevel.INFO, packet0login.username + " has joined");
				if(server.getClientSocketByUsername(packet0login.username) == null){
					setUsername(packet0login.username);
					sendPacket(packet0login);
				}else{
					close("already connected with that username");
				}
			break;
			case 1:
				Logger.log(LogLevel.INFO, getUsername() + " is disconnecting");
				Packet1Disconnect packet255disconnect = (Packet1Disconnect) packet;
				close(packet255disconnect.message);
			break;
			case 2:
				Packet2KeepAlive packet2keepalive = (Packet2KeepAlive) packet;
				sendPacket(packet2keepalive);
			break;
			case 3:
				Packet3Message packet3message = (Packet3Message) packet;
				sendBroadcast(packet3message.message);
			break;
			case 4:
				Packet4ListClients packet4listclients = (Packet4ListClients) packet;
				sendListClients(packet4listclients.fullList);
			break;
			case 5:
				Packet5PM packet5pm =(Packet5PM) packet;
				IClientSocket sock = server.getClientSocketByUsername(packet5pm.username);
				if(sock == null){
					sendMessage("user not found");
				}else{
					sock.sendMessage(packet5pm.message);
				}
			break;
		}
	}
	
	/**
	 * Send a the client list to the connected client
	 * @param fullList
	 * 			if true send usernames else send client count
	 */
	private void sendListClients(boolean fullList) {
		ArrayList<IClientSocket> clients = server.getClients();
		ArrayList<String> usernames = new ArrayList<String>();
		for(IClientSocket c : clients){
			if(c.getUsername() != null){
				usernames.add(c.getUsername());
			}
		}
		Packet4ListClients packet = new Packet4ListClients(fullList, usernames);
		sendPacket(packet);
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
	
	public void sendBroadcast(String message) {
		server.sendBroadcast(message);
	}
	
	public void sendMessage(String message){
		Packet3Message packet3message = new Packet3Message(message);
		sendPacket(packet3message);
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
