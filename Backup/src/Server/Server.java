package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
class Server {
	static List<World> worlds = new ArrayList<World>();
	
	public static void main(String args[]) throws Exception       {
		DatagramSocket serverSocket = new DatagramSocket(9098);  
		byte[] receiveData = new byte[1024];   
		byte[] sendData = new byte[1024];
				
		while(true)             
		{                 
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);  
			serverSocket.receive(receivePacket);  			         
			InetAddress IPAddress = receivePacket.getAddress();   
			int port = receivePacket.getPort();     
			
			byte command = receiveData[0];			
			if (command == 1) {
				instantiateWorld(serverSocket, IPAddress, port);
			} else if(command == 2) {
				instantiateMap(serverSocket, IPAddress, port); 
			}  
		}    
	} 
	
	public static void instantiateWorld(DatagramSocket serverSocket, InetAddress IPAddress, int port) throws Exception { 
		byte[] sendData = new byte[1024];	
		worlds.add(new World(IPAddress));
		sendData = new byte[1024];
		sendData[0] = (byte)(300%127);
		sendData[1] = (byte)(300/127);
		sendData[2] = (byte)(200%127);
		sendData[3] = (byte)(200/127);
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);  
		serverSocket.send(sendPacket);
	}
	public static void instantiateMap(DatagramSocket serverSocket, InetAddress IPAddress, int port) throws Exception {
		byte[] sendData = new byte[1024];	
		sendData = SendMapData.populateMap();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);  
		serverSocket.send(sendPacket); 
	}
}