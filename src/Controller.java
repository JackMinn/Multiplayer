import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import Server.ServerCharacter;


public class Controller {
	private static Controller singleInstance;
	private static JFrame game;
	private static Display display;
	static private Character me;
	static DatagramSocket clientSocket;
	InetAddress IPAddress;
	
	public Controller() throws Exception {
		game = new JFrame("Character Display");
		
		//instantiate a new world
		//clients having different sockets caused MASSIVE issues, suddenly we were attempting to send
		//to the socket of a diff client, aka use 9090 for katt or my laptop even though its 9095 for the others
		clientSocket = new DatagramSocket(9090);
		IPAddress = InetAddress.getByName("127.0.0.1");
		byte[] sendData = new byte[1024];
		byte command = (byte) 1;
		sendData[0] = command;
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9098);
		clientSocket.send(sendPacket);
		
//		byte[] receiveData = new byte[1024]; 
//		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
//		clientSocket.receive(receivePacket);
//		startingX = (receiveData[0] + receiveData[1]*127);
//		startingY = (receiveData[2] + receiveData[3]*127);
//		ID = receiveData[4];
//      
//		//get a new character ID
//		me = new Character(startingX, startingY, clientSocket, ID);
//		display = new Display(me);
//		
//		me.addObserver(display);
//		display.addCharacter(me);
		
//		display.setFocusable(true);
//		display.requestFocusInWindow();
//      
//		game.add(display);   
//		game.pack();
//		game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		game.setVisible(true);  
	}
	
	public static void main (String args[]) throws Exception{		
		Controller initialize = Controller.getSingleInstance();
		//DatagramSocket movementSocket = new DatagramSocket(9097);
		byte[] receiveData = new byte[1024]; 
		
		while(true)             
		{                 
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);  
			clientSocket.receive(receivePacket);         
			//receiveData in this method should get all movement details on loop and execute them
			//also adding of new characters will be handled here
			
			int command = receiveData[0];
			if(command == 1) {
				//instantiate this clients character
				int startingX = (receiveData[1] + receiveData[2]*127);
				int startingY = (receiveData[3] + receiveData[4]*127);
				int ID = receiveData[5];
		      
				me = new Character(startingX, startingY, clientSocket, ID);
				display = new Display(me);
				
				me.addObserver(display);
				display.addCharacter(me);
				display.setFocusable(true);
				display.requestFocusInWindow();
		      
				game.add(display);   
				game.pack();
				game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				game.setVisible(true);  
			} else if (command == 2) {
				//instantiate a new character
				int startingX = (receiveData[1] + receiveData[2]*127);
				int startingY = (receiveData[3] + receiveData[4]*127);
				int ID = receiveData[5];
				
				Character c = new Character(startingX, startingY, ID);
				c.addObserver(display);
				display.addCharacter(c);
			} else if (command == 3) {
				int ID = receiveData[5];
				boolean exists = false;
				
				for (Character c : display.getActiveCharacters()) {
		            if(c.getID() == ID) {	
		            	//apply movement
		            	c.setXLoc(receiveData[1] + receiveData[2]*127);
		            	c.setYLoc(receiveData[3] + receiveData[4]*127);
		            	exists = true;
		            }
				}				
				//this should add retroactively, if a new client joins and issues a movement command 
				//if the client gets an ID which does not exist, it creates a new character
				if(exists == false) {
					int startingX = (receiveData[1] + receiveData[2]*127);
					int startingY = (receiveData[3] + receiveData[4]*127);
					
					Character c = new Character(startingX, startingY, ID);
					c.addObserver(display);
					display.addCharacter(c);
				}
			}
  
		}    
	} 
	
	private static Controller getSingleInstance() throws Exception{
		if(singleInstance==null){
			singleInstance = new Controller();
        }
        return singleInstance;
	}
	
}
