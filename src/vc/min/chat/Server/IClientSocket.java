package vc.min.chat.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import vc.min.chat.Shared.Packets.Packet;
import vc.min.chat.Shared.Packets.PacketHandler;

public interface IClientSocket {

	/**
	 * Add a packet to the send queue
	 * @param packet
	 */
	public void sendPacket(Packet packet);
	
	/** 
	 * Close the client connection
	 * @param message
	 */
	public void close(String message);
	
	/* Getters and setters */
	public PacketHandler getPacketHandler();
	
	public DataOutputStream getOutputStream();
	
	public DataInputStream getInputStream();
	
	public String getUsername();
	
	public void setUsername(String username);
	
	public boolean isRunning();
	
	public void setRunning(boolean running);
	
	public ArrayList<Packet> getPacketQueue();
}
