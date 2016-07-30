
public class Scenery {
	private int x_loc;
	private int y_loc;
	private String type;
	
	public Scenery(int x_loc, int y_loc, String type) {
		this.x_loc = x_loc;
		this.y_loc = y_loc;
		this.type = type;
	}
	
	public int getX() {
		return x_loc;
	}
	
	public int getY() {
		return y_loc;
	}
	
	public String getType() {
		return type;
	}	
}
