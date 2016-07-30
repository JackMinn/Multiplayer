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
		//byte[] sendData = new byte[1024];
		
		//need to thread this
		while(true)             
		{                 
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);  
			serverSocket.receive(receivePacket);  			         
			InetAddress IPAddress = receivePacket.getAddress();   
			//System.out.println(IPAddress);
			int port = receivePacket.getPort();     
			
			byte command = receiveData[0];	
			//these commands being carried out on the same thread mean no following packet can be accepted
			//until the previous outbound packet as a result of the command has been carried out
			//for some reason my laptop specifically lags causing the entire process to lag
			if (command == 1) {
				System.out.println("Connection Established");
				instantiateCharacter(serverSocket, IPAddress, port);
			} else if(command == 2) {
				instantiateMap(serverSocket, IPAddress, port); 
			} else if(command == 3) {
				moveCharacter(serverSocket, IPAddress, port, receiveData); 
			}  
		}    
	} 
	
	public static void instantiateCharacter(DatagramSocket serverSocket, InetAddress IPAddress, int port) throws Exception { 
		byte[] sendData = new byte[1024];	
		
		//this segment below must later be moved to support several worlds
		if(worlds.isEmpty()) {
			worlds.add(new World());
		}
		World current = worlds.get(0);
		System.out.println(IPAddress);
		
		ServerCharacter newChar = new ServerCharacter(300, 200, MapData.getXMAX(), MapData.getYMAX(), IPAddress, port, (current.getCharacters().size()));
		current.addCharacter(newChar);
		
		for (ServerCharacter sc : worlds.get(0).getCharacters()) {
			//existing clients should see new clients which come in, new clients which join wont see old ones though
			if(sc.getIP().getHostName().equals(IPAddress.getHostName())) {
				sendData = new byte[1024];
				sendData[0] = (byte) 1;
				sendData[1] = (byte)(300%127);
				sendData[2] = (byte)(300/127);
				sendData[3] = (byte)(200%127);
				sendData[4] = (byte)(200/127);
				sendData[5] = (byte)(newChar.getID());
				//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sc.getIP(), 9095);  
				//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sc.getIP(), port);  
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sc.getIP(), sc.getPort());
				serverSocket.send(sendPacket);
			} else {
				sendData = new byte[1024];
				sendData[0] = (byte) 2;
				sendData[1] = (byte)(300%127);
				sendData[2] = (byte)(300/127);
				sendData[3] = (byte)(200%127);
				sendData[4] = (byte)(200/127);
				sendData[5] = (byte)(newChar.getID());
				//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sc.getIP(), 9095);  
				//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sc.getIP(), port);  
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sc.getIP(), sc.getPort());
				serverSocket.send(sendPacket);
			}
		}
	}
	public static void instantiateMap(DatagramSocket serverSocket, InetAddress IPAddress, int port) throws Exception {
		byte[] sendData = new byte[1024];	
		sendData = MapData.populateMap();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);  
		serverSocket.send(sendPacket); 
	}
	public static void moveCharacter(DatagramSocket serverSocket, InetAddress IPAddress, int port, byte[] receiveData) throws Exception {
		ServerCharacter c = worlds.get(0).findCharacter(IPAddress);

		if(receiveData[1] == 1) {
			c.moveDown();
		} else if(receiveData[1] == 2) {
			c.moveUp();
		}  else if(receiveData[1] == 3) {
			c.moveRight();
		}  else if(receiveData[1] == 4) {
			c.moveLeft();
		}
		
		//System.out.println(c.getYLoc());
		for (ServerCharacter sc : worlds.get(0).getCharacters()) {
			byte[] sendData = new byte[1024];
			sendData[0] = (byte) 3;
			sendData[5] = (byte)(c.getID());
			sendData[1] = (byte)(c.getXLoc()%127);
			sendData[2] = (byte)(c.getXLoc()/127);
			sendData[3] = (byte)(c.getYLoc()%127);
			sendData[4] = (byte)(c.getYLoc()/127);
			//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sc.getIP(), 9095);  
			//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sc.getIP(), port); 
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sc.getIP(), sc.getPort());
			serverSocket.send(sendPacket);
		}
	}
}
	