import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Display extends JPanel implements CObserver{
	private List<Character> activeCharacters;
	private List<BufferedImage> characterImages;
	private final int MOVEMENT_DELAY = 200;
	final private Character myCharacter;
	private Timer moveDown;
	private Timer moveUp;
	private Timer moveRight;
	private Timer moveLeft;
	private Map myMap;
	
	public Display(final Character myCharacter) {		
		this.myCharacter = myCharacter;
		this.myMap = Map.getSingleInstance();

		this.setPreferredSize(new Dimension(myMap.getX(), myMap.getY()));
		this.setBackground(Color.white);
		
		activeCharacters = new ArrayList<Character>();
		characterImages = new ArrayList<BufferedImage>();
		
		try {
			initializeTimers();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
            	int keyCode = e.getKeyCode();
            	if(keyCode == KeyEvent.VK_DOWN) {
            		moveDown.stop();
            		myCharacter.resetState("Down");
            	} else if (keyCode == KeyEvent.VK_UP) {
            		moveUp.stop();
            		myCharacter.resetState("Up");
            	} else if (keyCode == KeyEvent.VK_RIGHT) {
            		moveRight.stop();
            		myCharacter.resetState("Right");
            	} else if (keyCode == KeyEvent.VK_LEFT) {
            		moveLeft.stop();
            		myCharacter.resetState("Left");
            	}
            }

            @Override
            public void keyPressed(KeyEvent e) {
            	int keyCode = e.getKeyCode();
            	if(keyCode == KeyEvent.VK_DOWN) {
            		moveDown.start();
            	} else if (keyCode == KeyEvent.VK_UP) {
            		moveUp.start();
            	} else if (keyCode == KeyEvent.VK_RIGHT) {
            		moveRight.start();
            	} else if (keyCode == KeyEvent.VK_LEFT) {
            		moveLeft.start();
            	}
            }
        });
	}
	
	public void initializeTimers() throws Exception {
		//timers for movement
				moveDown = new Timer(MOVEMENT_DELAY,new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e){
		                try {
							myCharacter.moveDown();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		        });
				moveDown.setInitialDelay(0);
				
				moveUp = new Timer(MOVEMENT_DELAY,new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e){
		            	 try {
							myCharacter.moveUp();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		        });
				moveUp.setInitialDelay(0);
				
				moveRight = new Timer(MOVEMENT_DELAY,new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e){
		            	 try {
							myCharacter.moveRight();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		        });
				moveRight.setInitialDelay(0);
				
				moveLeft = new Timer(MOVEMENT_DELAY,new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e){
		            	 try {
							myCharacter.moveLeft();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		        });
				moveLeft.setInitialDelay(0);
	}
	
	public void addCharacter(Character c) {
		activeCharacters.add(c);
		 BufferedImage character = null;
		 try {
        	character = ImageIO.read(new File("character_idle.png"));
        	
		 } catch (IOException e) {
		 }
		BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
		characterImages.add(filteredCharacter);
	}
	
   @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);             
        Graphics2D g2d = (Graphics2D) g;
        
        BufferedImage grass = null;
        try {
        	grass = ImageIO.read(new File("grass.png"));
        } catch (IOException e) {
        }
        
//        BufferedImage house = null;
//		try {
//			house = ImageIO.read(new File("house1.png"));				
//		} catch (IOException e) {
//        }
//		BufferedImage filteredHouse = getTransparentImage(house, Color.WHITE);
        
        List<Scenery> mapData = myMap.getMapData();
        for(int i = 0; i < mapData.size(); i++) {
        	if(mapData.get(i).getType() == "Grass") {
        		g2d.drawImage(grass, null, mapData.get(i).getX(), mapData.get(i).getY());
        	} else if(mapData.get(i).getType() == "House") {
        		//g2d.drawImage(filteredHouse, mapData.get(i).getX(), mapData.get(i).getY(), this);
        	}
        }
        
        for(int i = 0; i < activeCharacters.size(); i++) {
        	g2d.drawImage(characterImages.get(i), activeCharacters.get(i).getXLoc(), activeCharacters.get(i).getYLoc(), this);  
        }
    }
   
   //methods to aid with drawing, need to learn them
   public static BufferedImage getTransparentImage(BufferedImage image, Color transparent) {
        BufferedImage img = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        int r1 = transparent.getRed();
        int g1 = transparent.getGreen();
        int b1 = transparent.getBlue();
        
        for (int x=0; x<img.getWidth(); x++) {
            for (int y=0; y<img.getHeight(); y++) {
            	Color c = new Color(image.getRGB(x, y));
            	int r2 = c.getRed();
            	int g2 = c.getGreen();
            	int b2 = c.getBlue();
            	int leeway = 15;
            	if((r2 < (r1-leeway) | r2 > (r1+leeway))  &&  (g2 < (g1-leeway) | g2 > (g1+leeway))  &&  (b2 < (b1-leeway) | b2 > (b1+leeway))) {
            		img.setRGB(x, y, image.getRGB(x, y));
            	}
            }
        }
        	        
        g.dispose();
        return img;
    }
   
	@Override
	public void update(Character c) {
		for(int i = 0; i < activeCharacters.size(); i++) {
			if(activeCharacters.get(i) == c) {
				if(c.getState() == 0) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_idle.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);	
					} catch (IOException e) {
			        }
				} else if(c.getState() == 1) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_leftleg_moving_walk.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);	
					} catch (IOException e) {
			        }
				} else if(c.getState() == 2) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_rightleg_moving_walk.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);		
					} catch (IOException e) {
					}
				} else if(c.getState() == 3) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_idle_up.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);	
					} catch (IOException e) {
			        }
				} else if(c.getState() == 4) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_leftleg_moving_walkup.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);	
					} catch (IOException e) {
			        }
				} else if(c.getState() == 5) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_rightleg_moving_walkup.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);		
					} catch (IOException e) {
					}
				} else if(c.getState() == 6) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_idle_right.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);	
					} catch (IOException e) {
			        }
				} else if(c.getState() == 7) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_leftleg_moving_walkright.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);	
					} catch (IOException e) {
			        }
				} else if(c.getState() == 8) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_rightleg_moving_walkright.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);		
					} catch (IOException e) {
					}
				} else if(c.getState() == 9) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_idle_left.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);	
					} catch (IOException e) {
			        }
				} else if(c.getState() == 10) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_leftleg_moving_walkleft.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);	
					} catch (IOException e) {
			        }
				} else if(c.getState() == 11) {
					BufferedImage character = null;
					try {
					character = ImageIO.read(new File("character_rightleg_moving_walkleft.png"));
					
					BufferedImage filteredCharacter = getTransparentImage(character, Color.WHITE);
					
					characterImages.set(i, filteredCharacter);		
					} catch (IOException e) {
					}
				}
			}
		}				
		this.repaint();
	}
	
	public List<Character> getActiveCharacters() {
		return activeCharacters;
	}
}
