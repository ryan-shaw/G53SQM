package vc.min.chat.Server.IO;

import java.io.DataInputStream;
import java.io.IOException;

import vc.min.chat.Server.ClientSocket;
import vc.min.chat.Shared.Packets.Packet0Login;
import vc.min.chat.Shared.Packets.Packet;
import vc.min.chat.Shared.Packets.Packet1Disconnect;
import vc.min.chat.Shared.Packets.Packet2KeepAlive;
import vc.min.chat.Shared.Packets.PacketHandler;

public class ReaderThread extends Thread {
	
	/**
	 * Client socket
	 */
	private ClientSocket clientSocket;

	/**
	 * The data input stream
	 */
	private DataInputStream dis;
	
	/** 
	 * Reader thread constructor
	 * 
	 * @param dis
	 * @param clientSocket
	 */
	public ReaderThread(ClientSocket clientSocket){
		this.clientSocket = clientSocket;
		this.dis = clientSocket.getInputStream();
	}
	
	@Override
	public void run(){
		while(clientSocket.isRunning()){
			byte packetID;
			try {
				packetID = dis.readByte();
				handlePacket(packetID);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			this.dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handlePacket(byte packetID) {
		Packet packet = this.clientSocket.getPacketHandler().readPacket(packetID);
		if(packet == null){
			clientSocket.setRunning(false);
			return;// Disconnect
		}
		switch(packetID){
		case 0:
			Packet0Login packet0login = (Packet0Login) packet;
			clientSocket.sendPacket(packet0login);
		break;
		case 2:
			Packet2KeepAlive packet2keepalive = (Packet2KeepAlive) packet;
			clientSocket.sendPacket(packet2keepalive);
			clientSocket.lastTimeRead = System.currentTimeMillis();
			
		case 1:
			System.out.println("Closing client");
			Packet1Disconnect packet255disconnect = (Packet1Disconnect) packet;
			clientSocket.close(packet255disconnect.message);
		}
	}

	public void close(){
		try {
			dis.close();
		} catch (IOException e) {
			System.err.println("Failed to close: " + e.getMessage());
		}
	}
}
