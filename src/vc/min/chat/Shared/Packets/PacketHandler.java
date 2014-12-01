package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler {
	
	/**
	 * Map packet ID to packet classes
	 */
	public static final Map<Integer, Class<? extends Packet>> packets;
	static{
		Map<Integer, Class<? extends Packet>> packets1 = new HashMap<Integer, Class<? extends Packet>>();
		packets1.put(0, Packet0Login.class);
		packets1.put(1, Packet1Disconnect.class);
		packets1.put(2, Packet2KeepAlive.class);
		packets1.put(127, Packet127Greeting.class);
		packets = Collections.unmodifiableMap(packets1);
	}
	
	/**
	 * Data input stream
	 */
	private DataInputStream dis;
	
	/**
	 * Data output stream
	 */
	private DataOutputStream dos;
	
	/**
	 * Packet handler constructor
	 * @param dis
	 * @param dos
	 */
	public PacketHandler(DataInputStream dis, DataOutputStream dos){
		this.dis = dis;
		this.dos = dos;
	}
	
	/**
	 * Write a packet the {@link #dos}
	 * @param packet
	 * @throws IOException
	 */
	public void writePacket(Packet packet) throws IOException{
		int packetID = getPacketID(packet.getClass());
		dos.writeByte(packetID);
		packet.write(dos);
		dos.flush();
	}

	/**
	 * Read a packet from {@link #dis} 
	 * @param packetID
	 * @return packet
	 */
	public Packet readPacket(int packetID){
		try {
			Class<? extends Packet> packetClass = getPacketClass(packetID);
			Packet packet = (Packet) packetClass.newInstance();
			packet = packet.read(dis);
			return packet;
		} catch (IOException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.err.println("Unknown error on packet receive");
		} catch(NullPointerException e){
			System.err.println("Received malformed packet!");
		}
		return null;
	}
	
	/**
	 * Get a packet ID from a packet class 
	 * @param packetClass
	 * @return packetID,
	 * 		-1 if not found
	 */
	public int getPacketID(Class<? extends Packet> packetClass){
		for(Map.Entry<Integer, Class<? extends Packet>> entry : packets.entrySet()){
			if(packetClass == entry.getValue())
				return entry.getKey();
		}
		return -1;
	}
	
	/**
	 * Get packet class from packet ID
	 * @param id
	 * @return packet class
	 */
	public Class<? extends Packet> getPacketClass(int id){
		return packets.get(id);
	}
}
