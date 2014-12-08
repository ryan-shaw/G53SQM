package vc.min.chat.Shared.Packets;

import java.io.IOException;

public interface IPacketHandler {
	/**
	 * Write a packet the {@link PacketHandler#dos}
	 * @param packet
	 * @throws IOException
	 */
	void writePacket(Packet packet) throws IOException;
	
	/**
	 * Read a packet from {@link PacketHandler#dis} 
	 * @param packetID
	 * @return packet
	 */
	Packet readPacket(int packetID);
	
	/**
	 * Get a packet ID from a packet class 
	 * @param packetClass
	 * @return packetID,
	 * 		-1 if not found
	 */
	int getPacketID(Class<? extends Packet> packetClass);
	
	/**
	 * Get packet class from packet ID
	 * @param id
	 * @return packet class
	 */
	Class<? extends Packet> getPacketClass(int id);
}
