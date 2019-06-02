package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import main.ants.Ant;
import main.ants.Movement;
import main.elements.Feed;
import main.elements.Nest;
import main.elements.Patch;
import main.enums.Mode;
import main.misc.Configuration;
import main.misc.ImagePanel;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class World extends JFrame {
    
	private static final long serialVersionUID = 766277020594621205L;
	private final int l;
    private final int h;
    
    public Patch[][] field;
    
    private Nest nest;
    private Feed feed;
    
    public Integer broughtFood;
   
    public List<Ant> list = new ArrayList<Ant>();
    
    public BufferedImage img;
    protected WritableRaster raster;
    protected ColorModel model;
    protected Icon icon;
    protected JButton button;
    protected ImagePanel panel;
    
    public boolean enabled = false;
    private boolean inst = false;
    public boolean suspended = false;
    
    private int wx1;
    private int wy1;
    private int wx2;
    private int wy2;
    
    public World(int l, int h){
    	
    	this.broughtFood = 0;
        
        this.h = h;
        this.l = l;
                
        img = new BufferedImage(l * Configuration.PATCH_SIZE, h * Configuration.PATCH_SIZE, BufferedImage.TYPE_INT_ARGB);
        raster = img.getRaster();
        model = img.getColorModel();
        Color c0 = Color.WHITE;
        int argb = c0.getRGB();
        
        Object data0 = model.getDataElements(argb, null);
        
        for (int i = 0; i < l * Configuration.PATCH_SIZE; i++) {
        	for (int j = 0; j < h * Configuration.PATCH_SIZE; j++) {
        		raster.setDataElements(i, j, data0);
            }
        }
        
        panel = new ImagePanel();
        this.setFocusable(true);
        //panel.setBounds(25, 25, l*5, h*5);
        this.add(panel);
        
        this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {

				if(e.getKeyCode() == KeyEvent.VK_SPACE && Main.world.enabled == false && !inst) {
					Main.world.enabled = true;
					
					Thread tNest = new Thread(new Spawner());
			        tNest.start();
			        
			        Update.cc = Color.WHITE;
					
				} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					Main.world.suspended = !Main.world.suspended;
				} else if(e.getKeyCode() == KeyEvent.VK_D) {
					Movement.bf.close();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) { }

			@Override
			public void keyTyped(KeyEvent arg0) { }
        	
        });
        
        panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if(SwingUtilities.isLeftMouseButton(e)) {
					
					int x = e.getX() / Configuration.PATCH_SIZE;
					int y = e.getY() / Configuration.PATCH_SIZE;

					System.out.println(Main.world.getPatchInfo(x, y));
				}
								
				if(SwingUtilities.isRightMouseButton(e)) {
					if(inst == false) {							
						Main.world.wx1 = e.getX() / Configuration.PATCH_SIZE;
						Main.world.wy1 = e.getY() / Configuration.PATCH_SIZE;
						Main.world.field[Main.world.wx1][Main.world.wy1].setMarker();							
						inst = true;
						
					} else {
						
						Main.world.wx2 = e.getX() / Configuration.PATCH_SIZE;
						Main.world.wy2 = e.getY() / Configuration.PATCH_SIZE;
						Main.world.field[Main.world.wx2][Main.world.wy2].setMarker();
						inst = false;
						
						try {
							Thread.sleep(200L);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
							
						Main.world.createWall(Main.world.wx1, Main.world.wy1, Main.world.wx2, Main.world.wy2);
							
						Main.world.field[Main.world.wx1][Main.world.wy1].removeMarker();
						Main.world.field[Main.world.wx2][Main.world.wy2].removeMarker();
							
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) { }

			@Override
			public void mouseExited(MouseEvent arg0) { }

			@Override
			public void mousePressed(MouseEvent arg0) { }

			@Override
			public void mouseReleased(MouseEvent arg0) { }
        	
        });
        

                 
        //Frame setting
        getContentPane().setPreferredSize(new Dimension(l * Configuration.PATCH_SIZE,h * Configuration.PATCH_SIZE));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.field = new Patch[l][h];
        for(int x=0; x<l; x++){
            for(int y=0; y<h; y++){
                this.field[x][y]= new Patch();
            }
        }
    }
    
    public void initalize(){
        createWall(0,0,0,this.h-1);
        createWall(0,0,this.l-1,0);
        createWall(this.l-1,0,this.l-1,this.h-1);
        createWall(0,this.h-1,this.l-1,this.h-1);
        
        this.interpretWallConfiguration(Configuration.WALLS);

        this.interpretFeedConfiguration(Configuration.FEED);
        this.interpretNestConfiguration(Configuration.NEST);

    }
    
        
    public void createObject(int x1, int y1, int x2, int y2, Mode m){
        
        int xL;
        int xR;
        int yB;
        int yT; 
        
        if(x1>x2){
           xL = x2; 
           xR = x1;
        }else{
           xL = x1; 
           xR = x2; 
        }
        
        if(y1>y2){
           yB = y2; 
           yT = y1;
        }else{
           yB = y1; 
           yT = y2; 
        }
           
        for(int i = xL; i<=xR;i++){
           for(int o = yB; o<=yT;o++){
               switch(m) {
                   case WALL:
                       this.field[i][o].setWall(true);
                       break;
                   case NEST:
                       this.field[i][o].setNest(true);
                       break;
                   case FEED:
                       this.field[i][o].setFeed(); 
                       break;
			default:
				break;
               }
           } 
        }
    } 
    
    private void interpretWallConfiguration(String walls) {
    	
    	if(walls.equals("")) {
    		return;
    	}
    	
    	String[] parts = walls.split("-");
    	int x1; int y1; int x2; int y2;
    	
    	for(String s : parts) {
    		
    		String[] coords = s.split(",");
    		
    		x1 = Integer.parseInt(coords[0]);
    		y1 = Integer.parseInt(coords[1]);
    		x2 = Integer.parseInt(coords[2]);
    		y2 = Integer.parseInt(coords[3]);
    		
    		this.createWall(x1, y1, x2, y2);
    	}
    	
    }
    
    private void interpretNestConfiguration(String nest) {
    	
    	if(nest.equals("")) {
    		System.out.println("ERROR: Missing nest configuration!");
    		System.exit(0);
    	}
    	
    	String[] parts = nest.split(",");
    	
    	this.createNest(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    	
    }
    
    private void interpretFeedConfiguration(String feed) {
    	
    	if(feed.equals("")) {
    		System.out.println("ERROR: Missing feed configuration!");
    		System.exit(0);
    	}
    	
    	String[] parts = feed.split(",");
    	
    	this.createFeed(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
    	
    }
    
    public String getPatchInfo(int x, int y) {
    	if(field[x][y].getMode() == Mode.NEST){
        	return "Patch: X=" + x + " Y=" + y + " | NestIntensity=" + field[x][y].getNestIntensity().toString() + " | FeedIntensity=" + field[x][y].getFeedIntensity().toString() + " | Mode=" + field[x][y].getMode().toString() + " | Brought Food: " + broughtFood;
    	}else if(field[x][y].getMode() == Mode.FEED){
        	return "Patch: X=" + x + " Y=" + y + " | NestIntensity=" + field[x][y].getNestIntensity().toString() + " | FeedIntensity=" + field[x][y].getFeedIntensity().toString() + " | Mode=" + field[x][y].getMode().toString() + " | Left Food: " + field[x][y].getFeed();
    	}else{
        	return "Patch: X=" + x + " Y=" + y + " | NestIntensity=" + field[x][y].getNestIntensity().toString() + " | FeedIntensity=" + field[x][y].getFeedIntensity().toString() + " | Mode=" + field[x][y].getMode().toString();
    	}
    }
       
    public void createNest(int x1, int y1){
        this.nest= new Nest(x1, y1, x1, y1);
    }
   
    public void createWall(int x1, int y1, int x2, int y2){
       this.createObject(x1, y1, x2, y2, Mode.WALL); 
    }
    
    public void createFeed(int x1, int y1, int x2, int y2){
       this.feed = new Feed(x1, y1, x2, y2); 
    }
    
    public Nest getNest(){
        return this.nest;
    }
    
    public Feed getFeed() {
        return this.feed;
    }
    
    public Integer getL(){
        return this.l;
    }
    
    public Integer getH(){
        return this.h;
    }
   
}
