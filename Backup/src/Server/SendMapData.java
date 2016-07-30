package Server;

public class SendMapData {
	private static final int X_MAX = 960;
	private static final int Y_MAX = 320;
	private static final int TILE_SIZE = 16;
	private static final int GRASS = 1;
	private static byte[] mapData = new byte[((X_MAX/TILE_SIZE) * (Y_MAX/TILE_SIZE))+5];
	
	public static byte[] populateMap() {
		//first hexadecimal should represent map width, second should represent map height
		//3rd should represent tile size, rest should be data
		mapData[0] = (byte)(X_MAX/127); //this value * 127 = 635
		mapData[1] = (byte)(X_MAX%127); //this value + previous so 5+635 = 640 width
		//not that its possible to represent up to 255 (which is -1) but converting this back to int is a pain
		mapData[2] = (byte)(Y_MAX/127); //this value is * 127 = 254
		mapData[3] = (byte)(Y_MAX%127);//this value + previous so 66
		//now the width and height are defined, set the tile size
		mapData[4] = (byte)(TILE_SIZE);
		
		for(int x = 0; x < X_MAX; x+=TILE_SIZE) {
        	for(int y = 0; y < Y_MAX; y+=TILE_SIZE) {
        		//potentially do more complex stuff
        	}
        }
		
		return mapData;
	}
}
