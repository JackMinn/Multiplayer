import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class Map {
	private static Map singleInstance;
	private List<Scenery> mapData;
	int X_MAX;
	int Y_MAX;
	
	public Map() {
		mapData = new ArrayList<Scenery>();
		
		try {
			populateMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Map getSingleInstance(){
		if(singleInstance==null){
			singleInstance = new Map();
        }
        return singleInstance;
	}
	
	public void populateMap() throws Exception{
		DatagramSocket clientSocket = new DatagramSocket(9095); 
		
		//This is the destination IP address
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
		
		//These are the byte arrays which hold the send data and recieve data for packets
		byte[] sendData = new byte[1024];      
		byte[] receiveData = new byte[1024];  
		
		//These are the bytes we intend to send 
		byte command = (byte) 2;
		sendData[0] = command;
		
		//This is the packet that we intend to send, with the data/targetIP/targetPort information
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9098); 

		//This sends the packet out of the client socket
		clientSocket.send(sendPacket);      
		
		//This is the packet that we intend to receive back
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);   
		
		//This receives the packet from the clientSocket
		clientSocket.receive(receivePacket);  

		//the recieveData byte array should now have the map information stored
		clientSocket.close();  
		
		//int arrayCounter = 5;   this will be dealt with later, for now just add grass
		X_MAX = ((receiveData[0] * 127) + (receiveData[1]));
		Y_MAX = ((receiveData[2] * 127) + (receiveData[3]));
		
		for(int x = 0; x < X_MAX; x+=receiveData[4]) {
        	for(int y = 0; y < Y_MAX; y+=receiveData[4]) {
        		//populate first with grass
        		mapData.add(new Grass(x, y));
        	}
        }
	}
	
	public int getX() {
		return X_MAX;
	}
	public int getY() {
		return Y_MAX;
	}	
	public List<Scenery> getMapData() {
		return mapData;
	}
}
