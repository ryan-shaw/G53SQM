package vc.min.chat.Server.IO;

import java.io.DataInputStream;
import java.io.IOException;

import vc.min.chat.Server.ClientSocket;
import vc.min.chat.Shared.Packets.Packet;

/**
 * Reads incoming packets
 * 
 * @author Ryan Shaw
 *
 */
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
				packetID = (byte) dis.read();
				Packet packet = clientSocket.getPacketHandler().readPacket(packetID);
				if(packet == null){
					clientSocket.close("malformed packet received");
					return;
				}else{
					clientSocket.handlePacket(packet);
				}
			} catch (IOException e) {
				// Stream probably closed unexpectedly
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
}
