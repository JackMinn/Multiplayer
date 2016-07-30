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


public class Controller {
	private static Controller singleInstance;
	private JFrame game;
	private Display display;
	private Character me;
	private int startingX;
	private int startingY;
	
	public Controller() throws Exception {
		game = new JFrame("Character Display");
		
		//instantiate a new world
		DatagramSocket clientSocket = new DatagramSocket(9095);
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
		byte[] sendData = new byte[1024];
		byte command = (byte) 1;
		sendData[0] = command;
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9098);
		clientSocket.send(sendPacket);
		
		byte[] receiveData = new byte[1024]; 
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
		clientSocket.receive(receivePacket);
		startingX = (receiveData[0] + receiveData[1]*127);
		startingY = (receiveData[2] + receiveData[3]*127);
		clientSocket.close();
      
		me = new Character(startingX, startingY);
		display = new Display(me);
		
		me.addObserver(display);
		display.addCharacter(me);
		
		display.setFocusable(true);
		display.requestFocusInWindow();
      
		game.add(display);   
		game.pack();
		game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game.setVisible(true);  
	}
	
	public static void main (String args[]) throws Exception{		
		Controller initialize = Controller.getSingleInstance();
    }
	
	private static Controller getSingleInstance() throws Exception{
		if(singleInstance==null){
			singleInstance = new Controller();
        }
        return singleInstance;
	}
	
}
