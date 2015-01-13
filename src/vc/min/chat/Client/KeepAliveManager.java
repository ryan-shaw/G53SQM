package vc.min.chat.Client;

import vc.min.chat.Shared.Packets.Packet2KeepAlive;
import vc.min.chat.Shared.Packets.PacketHandler;

public class KeepAliveManager implements Runnable{
	
	private PacketHandler packetHandler;
	private Client client;
	
	public KeepAliveManager(PacketHandler packetHandler, Client client) {
		this.packetHandler = packetHandler;
		this.client = client;
	}
	
	@Override
	public void run(){
		while(client.isRunning()){
			try {
				
				Packet2KeepAlive packet = new Packet2KeepAlive();
				packetHandler.writePacket(packet);
				
				
			} catch (Exception e) {
				System.err.println("Cound not send packet.");
	            e.printStackTrace();
			}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
	}
}
