import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class Character {
	private List<CObserver> observers;
	private int x_loc;
	private int y_loc;
	private int state;
	private DatagramSocket clientSocket;
	private final int ID;
	
	public Character(int x, int y, int id) {
		observers = new ArrayList<CObserver>();
		state = 0;		
		x_loc = x;
		y_loc = y;
		ID = id;
	}
	
	public Character(int x, int y, DatagramSocket c, int id) {
		observers = new ArrayList<CObserver>();
		state = 0;		
		x_loc = x;
		y_loc = y;
		clientSocket = c;
		ID = id;
	}
	
	public void resetState(String direction){
		if(direction == "Down") {
			state = 0;
		} else if (direction == "Up") {
			state = 3;
		} else if (direction == "Right") {
			state = 6;
		} else if (direction == "Left") {
			state = 9;
		}
		notifyObservers();
	}
	
	public void moveDown() throws Exception {
		if(state == 0 | state == 2) {
			state = 1;
			//start movement by sending data 
			moveViaServer(1);
			notifyObservers();
		} else if (state == 1) {
			state = 2;
			moveViaServer(1);
			notifyObservers();
		} else if (state == 3 | state == 6 | state == 9) {
			state = 0;
		}
	}
	
	public void moveUp() throws Exception {
		if((state == 3 | state == 5)) {
			state = 4;
			moveViaServer(2);
			notifyObservers();
		} else if (state == 4) {
			state = 5;
			moveViaServer(2);
			notifyObservers();
		} else if (state == 0 | state == 6 | state == 9) {
			state = 3;
			notifyObservers();
		}
	}
	
	public void moveRight() throws Exception {
		if((state == 6 | state == 8)) {
			state = 7;
			moveViaServer(3);
			notifyObservers();
		} else if (state == 7) {
			state = 8;
			moveViaServer(3);
			notifyObservers();
		} else if (state == 0 | state == 3 | state == 9) {
			state = 6;
			notifyObservers();
		}
	}
	
	public void moveLeft() throws Exception {
		if((state == 9 | state == 11)) {
			state = 10;
			moveViaServer(4);
			notifyObservers();
		} else if (state == 10) {
			state = 11;
			moveViaServer(4);
			notifyObservers();
		} else if (state == 0 | state == 3 | state == 6) {
			state = 9;
			notifyObservers();
		}
	}
	
	public void moveViaServer(int direction) throws Exception {
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
		byte[] sendData = new byte[1024];
		byte command = (byte) 3;
		sendData[0] = command;
		sendData[1] = (byte) direction;
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9098);
		clientSocket.send(sendPacket);
		
		//receive stuff now
//		byte[] receiveData = new byte[1024]; 
//		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
//		clientSocket.receive(receivePacket);
//		x_loc = (receiveData[1] + receiveData[2]*127);
//		y_loc = (receiveData[3] + receiveData[4]*127);
		
		//clientSocket.close();
	}
	
	public int getXLoc() {
		return x_loc;
	}
	
	public int getYLoc() {
		return y_loc;
	}
	
	public void setXLoc(int x) {
		x_loc = x;
	}
	
	public void setYLoc(int y) {
		y_loc = y;
	}
	
	public int getState() {
		return state;
	}
	
	protected void notifyObservers(){
        for (CObserver o : observers)
            o.update(this);
    }
    
    public void addObserver(CObserver o){
        observers.add(o);
    }
    
    public void RemoveObserver(CObserver o){
        observers.remove(o);
    }

	public int getID() {
		return ID;
	}

}
