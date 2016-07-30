import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class Character {
	private List<CObserver> observers;
	private int x_loc;
	private int y_loc;
	private int state;
	private final int X_MAX = 640;
	private final int Y_MAX = 320;
	private final int MOVEMENT_DELAY = 200;
	
	public Character(int x, int y) {
		observers = new ArrayList<CObserver>();
		state = 0;		
		x_loc = x;
		y_loc = y;
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
	
	public void moveDown() {
		if((state == 0 | state == 2) && y_loc < (Y_MAX - 25)) {
			state = 1;
			y_loc += 7;	
			notifyObservers();
		} else if (state == 1 && y_loc < (Y_MAX - 25)) {
			state = 2;
			y_loc += 7;
			notifyObservers();
		} else if (state == 3 | state == 6 | state == 9 | y_loc >= (Y_MAX - 25)) {
			state = 0;
			notifyObservers();
		}
	}
	
	public void moveUp() {
		if((state == 3 | state == 5) && y_loc > 25) {
			state = 4;
			y_loc -= 7;	
			notifyObservers();
		} else if (state == 4 && y_loc > 25) {
			state = 5;
			y_loc -= 7;
			notifyObservers();
		} else if (state == 0 | state == 6 | state == 9 | y_loc <= 25) {
			state = 3;
			notifyObservers();
		}
	}
	
	public void moveRight() {
		if((state == 6 | state == 8) && x_loc < (X_MAX-25)) {
			state = 7;
			x_loc += 7;	
			notifyObservers();
		} else if (state == 7 && x_loc < (X_MAX-25)) {
			state = 8;
			x_loc += 7;	
			notifyObservers();
		} else if (state == 0 | state == 3 | state == 9 | x_loc >= (X_MAX - 25)) {
			state = 6;
			notifyObservers();
		}
	}
	
	public void moveLeft() {
		if((state == 9 | state == 11) && x_loc > (25)) {
			state = 10;
			x_loc -= 7;	
			notifyObservers();
		} else if (state == 10 && x_loc > (25)) {
			state = 11;
			x_loc -= 7;	
			notifyObservers();
		} else if (state == 0 | state == 3 | state == 6 | x_loc <= (25)) {
			state = 9;
			notifyObservers();
		}
	}
	
	public int getXLoc() {
		return x_loc;
	}
	
	public int getYLoc() {
		return y_loc;
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

}
