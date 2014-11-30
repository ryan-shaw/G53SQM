package vc.min.chat.Server.IO;

import java.io.DataInputStream;
import java.io.IOException;

import vc.min.chat.Server.ClientSocket;
import vc.min.chat.Shared.Packets.Login0Packet;
import vc.min.chat.Shared.Packets.Packet;
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
	 * The packet handler
	 */
	private PacketHandler packetHandler;
	
	/** 
	 * Reader thread constructor
	 * 
	 * @param dis
	 * @param clientSocket
	 */
	public ReaderThread(ClientSocket clientSocket){
		this.clientSocket = clientSocket;
		this.dis = clientSocket.getInputStream();
		this.packetHandler = new PacketHandler(dis, clientSocket.getOutputStream());
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
		Packet packet = this.packetHandler.readPacket(packetID);
		if(packet == null){
			clientSocket.setRunning(false);
			return;// Disconnect
		}
		switch(packetID){
		case 0:
			Login0Packet p = (Login0Packet) packet;
			clientSocket.sendPacket(p);
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
