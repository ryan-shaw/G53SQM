package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler {
	
	public static final Map<Integer, Class> packets;
	static{
		Map<Integer, Class> packets1 = new HashMap<Integer, Class>();
		packets1.put(0, Packet0Login.class);
		packets1.put(1, Packet1Disconnect.class);
		packets1.put(2, Packet2KeepAlive.class);
		packets = Collections.unmodifiableMap(packets1);
	}
	
	DataInputStream dis;
	DataOutputStream dos;
	
	public PacketHandler(DataInputStream dis, DataOutputStream dos){
		this.dis = dis;
		this.dos = dos;
	}
	
	public void writePacket(Packet packet) throws IOException{
		int packetID = getPacketID(packet.getClass());
		dos.writeByte(packetID);
		packet.write(dos);
	}
	
	public Packet readPacket(int packetID){
		try {
			Class<Packet> packetClass = getPacketClass(packetID);
			Packet packet = (Packet) packetClass.newInstance();
			packet = packet.read(dis);
			return packet;
		} catch (IOException | InstantiationException | IllegalAccessException e) {
			System.err.println("Unknown error on packet receive");
		} catch(NullPointerException e){
			System.err.println("Received malformed packet!");
		}
		return null;
	}
	
	public int getPacketID(Class clazz){
		for(Map.Entry<Integer, Class> entry : packets.entrySet()){
			if(clazz == entry.getValue())
				return entry.getKey();
		}
		return -1;
	}
	
	public Class<Packet> getPacketClass(int id){
		return packets.get(id);
	}
}
