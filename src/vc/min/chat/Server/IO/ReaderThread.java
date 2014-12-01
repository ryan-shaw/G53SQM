package vc.min.chat.Server.IO;

import java.io.DataInputStream;
import java.io.IOException;

import vc.min.chat.Server.ClientSocket;
import vc.min.chat.Shared.Packets.Packet0Login;
import vc.min.chat.Shared.Packets.Packet;
import vc.min.chat.Shared.Packets.Packet1Disconnect;
import vc.min.chat.Shared.Packets.Packet2KeepAlive;

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
				// Stream probably closed
				clientSocket.setRunning(false);
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
		clientSocket.lastTimeRead = System.currentTimeMillis();
		Packet packet = null;
		try{
			packet = this.clientSocket.getPacketHandler().readPacket(packetID);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(packet == null){
			Packet1Disconnect packet255disconnect = new Packet1Disconnect("malformed packet received");
			clientSocket.sendPacket(packet255disconnect);
			return;// Disconnect
		}
		switch(packetID){
		case 0:
			Packet0Login packet0login = (Packet0Login) packet;
			System.out.println(packet0login.username + " has joined");
			clientSocket.setUsername(packet0login.username);
			clientSocket.sendPacket(packet0login);
		break;
		case 2:
			Packet2KeepAlive packet2keepalive = (Packet2KeepAlive) packet;
			clientSocket.sendPacket(packet2keepalive);
		break;
		case 1:
			System.out.println("Client disconnecting...");
			Packet1Disconnect packet255disconnect = (Packet1Disconnect) packet;
			clientSocket.close(packet255disconnect.message);
		}
	}
}
