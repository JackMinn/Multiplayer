package Server;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class World {
	List<ServerCharacter> characters;
	
	public World() {
		characters = new ArrayList<ServerCharacter>();
	}
	
	public void addCharacter(ServerCharacter toAdd) {
		characters.add(toAdd);	
	}
	
	public ServerCharacter findCharacter(InetAddress IP) {
		ServerCharacter toReturn = null;
		for (ServerCharacter c : characters) {
            if(c.getIP().getHostName().equals(IP.getHostName())) {
            	//System.out.println(c.getIP().getHostName());
            	toReturn = c;
            }
		}
		return toReturn;
	}
	public List<ServerCharacter> getCharacters() {
		return characters;
	}

}
