package vc.min.chat.Server.IO;

import java.io.DataOutputStream;
import java.io.IOException;

import vc.min.chat.Server.ClientSocket;
import vc.min.chat.Shared.Packets.Packet;

public class SenderThread extends Thread {
	
	/**
	 * The client socket
	 */
	private ClientSocket clientSocket;

	/**
	 * The output stream
	 */
	private DataOutputStream dos;
	
	/**
	 * Constructor for sender thread
	 * 
	 * @param dos
	 * @param clientSocket
	 */
	public SenderThread(ClientSocket clientSocket){
		this.clientSocket = clientSocket;
		this.dos = clientSocket.getOutputStream();
	}
	
	@Override
	public void run(){
		try{
			while(clientSocket.isRunning()){
				if(clientSocket.getPacketQueue().size() > 0){
					Packet packet = clientSocket.getPacketQueue().remove(0);
					clientSocket.getPacketHandler().writePacket(packet);
				}
				Thread.sleep(50);
			}
			this.dos.close();
		}catch(IOException | InterruptedException e){
			e.printStackTrace();
		}
	}
}
