package vc.min.chat.Client;

import vc.min.chat.Shared.Packets.Packet2KeepAlive;
import vc.min.chat.Shared.Packets.PacketHandler;

public class KeepAliveManager implements Runnable{
	
	private PacketHandler packetHandler;
	
	public KeepAliveManager(PacketHandler packetHandler) {
		this.packetHandler = packetHandler;
	}
	
	@Override
	public void run(){
		while(true){
			try {

				Packet2KeepAlive packet = new Packet2KeepAlive();
				packetHandler.writePacket(packet);
				Thread.sleep(800);
				
			} catch (Exception e) {
				System.err.println("Cound not send packet.");
	            e.printStackTrace();
			}
		} 
		
	}
}
