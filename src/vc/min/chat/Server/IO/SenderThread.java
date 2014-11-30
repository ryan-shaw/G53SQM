package vc.min.chat.Server.IO;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import vc.min.chat.Server.ClientSocket;
import vc.min.chat.Shared.Packets.Packet;

public class SenderThread extends Thread {
	
	/**
	 * Packets to send
	 */
	private ArrayList<Packet> packets;
	
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
		packets = new ArrayList<Packet>();
	}
	
	@Override
	public void run(){
		while(clientSocket.isRunning()){
			if(packets.size() > 0){
				Packet packet = packets.remove(0);
				try {
					clientSocket.getPacketHandler().writePacket(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			this.dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void queuePacket(Packet packet) {
		packets.add(packet);
	}
}
