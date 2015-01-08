package vc.min.chat.Server.IO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	 * @throws IOException 
	 */
	public void sendPacket(Packet packet) throws IOException;
	
	/** 
	 * Close the client connection
	 * @param message
	 * 			the message to send to the client
	 * @throws IOException 
	 */
	public void close(String message) throws IOException;
	
	/**
	 * Process the incoming packet (all been read from the stream at this point)
	 * @param packet
	 * 			the packet object
	 * @throws IOException 
	 */
	public void handlePacket(Packet packet) throws IOException;
	
	/**
	 * Send a message to this client
	 * @param message
	 * 			message to send
	 * @param from
	 * 			who from
	 * @throws IOException 
	 */
	public void sendMessage(String from, String message) throws IOException;
	
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
