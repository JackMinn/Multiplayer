package Server;

import java.net.InetAddress;

public class ServerCharacter {
	private int x_loc;
	private int y_loc;
	private final int CHAR_SIZE = 24;
	private final int X_MAX;
	private final int Y_MAX;
	private InetAddress myIP;
	private final int ID;
	private int port;
	private long lastMoved;
	private final long MOVE_INTERVAL = 200;
	
	public ServerCharacter(int x, int y, int xMax, int yMax, InetAddress IPAddress, int port, int myID) {		
		x_loc = x;
		y_loc = y;
		X_MAX = xMax;
		Y_MAX = yMax;
		myIP = IPAddress;
		this.port = port;
		ID = myID;
		lastMoved = System.currentTimeMillis();
	}
	
	
	public void moveDown() {
		if(y_loc < (Y_MAX - CHAR_SIZE)) {
			if(System.currentTimeMillis() - lastMoved >= MOVE_INTERVAL) {
				lastMoved = System.currentTimeMillis();
				y_loc += 7;	
			}
		}
	}
	
	public void moveUp() {
		if(y_loc > CHAR_SIZE) {
			if(System.currentTimeMillis() - lastMoved >= MOVE_INTERVAL) {
				lastMoved = System.currentTimeMillis();
				y_loc -= 7;	
			}
		}
	}
	
	public void moveRight() {
		if(x_loc < (X_MAX-CHAR_SIZE)) {
			if(System.currentTimeMillis() - lastMoved >= MOVE_INTERVAL) {
				lastMoved = System.currentTimeMillis();
				x_loc += 7;	
			}
		} 
	}
	
	public void moveLeft() {
		if(x_loc > (CHAR_SIZE)) {
			if(System.currentTimeMillis() - lastMoved >= MOVE_INTERVAL) {
				lastMoved = System.currentTimeMillis();
				x_loc -= 7;		
			}
		} 
	}
	
	public int getXLoc() {
		return x_loc;
	}
	
	public int getYLoc() {
		return y_loc;
	}
	
	public InetAddress getIP() {
		return myIP;
	}
	
	public int getPort() {
		return port;
	}
	
	public int getID() {
		return ID;
	}
}