package vc.min.chat.Shared.Packets;

import java.io.IOException;

/** 
 * Handles the packets for incoming and outgoing packets
 * 
 * @author Ryan Shaw
 *
 */
public interface IPacketHandler {
	/**
	 * Write a packet the {@link PacketHandler#dos}
	 * @param packet
	 * 			the packet
	 * @throws IOException
	 * 			if connection issue
	 */
	void writePacket(Packet packet) throws IOException;
	
	/**
	 * Read a packet from {@link PacketHandler#dis} 
	 * @param packetID 
	 * 			the packet id to read
	 * @return packet
	 * 			the read packet
	 */
	Packet readPacket(int packetID);
	
	/**
	 * Get a packet ID from a packet class 
	 * @param packetClass 
	 * 			the packet.class
	 * @return packetID
	 * 			-1 if not found
	 */
	int getPacketID(Class<? extends Packet> packetClass);
	
	/**
	 * Get packet class from packet ID
	 * @param id
	 * 			the packet id
	 * @return packet
	 * 			the packet class
	 */
	Class<? extends Packet> getPacketClass(int id);
}
