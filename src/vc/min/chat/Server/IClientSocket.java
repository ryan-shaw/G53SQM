package vc.min.chat.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import vc.min.chat.Shared.Packets.Packet;
import vc.min.chat.Shared.Packets.PacketHandler;

/**
 * The interface for the client socket. Manages and processes data between the client and server
 * 
 * @author Ryan Shaw
 *
 */
public interface IClientSocket {

	/**
	 * Add a packet to the send queue
	 * @param packet
	 * 		the packet object
	 */
	public void sendPacket(Packet packet);
	
	/** 
	 * Close the client connection
	 * @param message
	 * 			the message to send to the client
	 */
	public void close(String message);
	
	/**
	 * Process the incoming packet (all been read from the stream at this point)
	 * @param packet
	 * 			the packet object
	 */
	public void handlePacket(Packet packet);
	
	/**
	 * Send a message to this client
	 * @param message
	 * 			message to send
	 * @param from
	 * 			who from
	 */
	public void sendMessage(String from, String message);
	
	/* Getters and setters */
	public PacketHandler getPacketHandler();
	
	public DataOutputStream getOutputStream();
	
	public DataInputStream getInputStream();
	
	public String getUsername();
	
	public void setUsername(String username);
	
	public boolean isRunning();
	
	public void setRunning(boolean running);
	
	public ArrayList<Packet> getPacketQueue();
	
	public long getLastTimeRead();
}
