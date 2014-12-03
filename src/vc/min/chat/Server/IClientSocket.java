package vc.min.chat.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import vc.min.chat.Shared.Packets.Packet;
import vc.min.chat.Shared.Packets.PacketHandler;

public interface IClientSocket {
	
	void sendPacket(Packet packet);
	
	void close(String message);
	
	PacketHandler getPacketHandler();
	
	DataOutputStream getOutputStream();
	
	DataInputStream getInputStream();
	
	String getUsername();
	
	void setUsername(String username);
	
	boolean isRunning();
	
	void setRunning(boolean running);
	
	ArrayList<Packet> getPacketQueue();
}
