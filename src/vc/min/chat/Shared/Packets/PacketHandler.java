package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import vc.min.chat.Server.Logger.LogLevel;
import vc.min.chat.Server.Logger.Logger;

public class PacketHandler implements IPacketHandler{
	
	/**
	 * Map packet ID to packet classes
	 */
	private static final Map<Integer, Class<? extends Packet>> packets;
	static{
		Map<Integer, Class<? extends Packet>> packets1 = new HashMap<Integer, Class<? extends Packet>>();
		packets1.put(0, Packet0Login.class);
		packets1.put(1, Packet1Disconnect.class);
		packets1.put(2, Packet2KeepAlive.class);
		packets1.put(3, Packet3Message.class);
		packets1.put(4, Packet4ListClients.class);
		packets1.put(5, Packet5PM.class);
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
	
	public void writePacket(Packet packet) throws IOException{
		int packetID = getPacketID(packet.getClass());
		dos.write(packetID);
		packet.write(dos);
		dos.flush();
	}

	public Packet readPacket(int packetID) {
		try {
			Class<? extends Packet> packetClass = getPacketClass(packetID);
			Packet packet = (Packet) packetClass.newInstance(); // Initiate new class
			packet = packet.read(dis); // Run the packets read method
			return packet;
		} catch (IOException | InstantiationException | IllegalAccessException e) {
			Logger.log(LogLevel.ERROR, "Unknown error on packet receive");
			e.printStackTrace();
		} catch(NullPointerException e){
			Logger.log(LogLevel.ERROR, "Received malformed packet");
		}
		return null;
	}
	
	public int getPacketID(Class<? extends Packet> packetClass){
		for(Map.Entry<Integer, Class<? extends Packet>> entry : packets.entrySet()){
			if(packetClass == entry.getValue())
				return entry.getKey();
		}
		return -1;
	}
	
	public Class<? extends Packet> getPacketClass(int id){
		return packets.get(id);
	}
}
