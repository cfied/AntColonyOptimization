package main;

import java.awt.Color;

import main.ants.Ant;
import main.datatype.IValue;
import main.elements.Patch;
import main.enums.Mode;
import main.misc.Configuration;

public class Update implements Runnable{
	
	/*private int ticks = 0;
	private int time1 = Update.getSeconds();
	private int time2;*/
	
	private double pct_nest = 70;
	private double pct_feed = 70;
	private IValue pFeed;
	private IValue pNest;
	
	private final Color c_brown = new Color(198,130,12);
	private final Color c_yellow = new Color(247,247,43);
	private final Color c_purple = Color.BLACK; //new Color(198,12,185);
	private final Color c_red = new Color(198,12,18);
	public static Color cc = Color.LIGHT_GRAY;
	
    @Override
    public void run(){
        while(true){
        	// Frame-Counter
        	/*time2 = Update.getSeconds();
        	if(time1 == time2) {
        		ticks++;
        	} else {
        		time1 = Update.getSeconds();
        		System.out.println(ticks);
        		ticks = 0;
        	}*/
            for(int y=0; y<Main.world.getH(); y++){
                for(int x=0; x<Main.world.getL(); x++){
                    Patch p = Main.world.field[x][y];
                    Mode m = p.getMode();
                    Color c = Color.GRAY;
                    
                    //zeitliche Abnahme
                    //p.setFeedIntensity(p.getFeedIntensity().div(1.5));
                                     
                    
                    switch(m){
                        case ANT: 
                            c= c_brown;
                            break;
                        case FEED:
                            c = c_yellow;
                            break;
                        case NEST:
                            c = c_purple;
                            break;
                        case WALL:
                            c = c_red;
                            break;
                        case FEED_PHEROMON:
                        	p.getFeedIntensity().assimilate();
                        	int i1 = p.getFeedIntensity().getInteger();
                        	
                        	if(i1 < 0) {
                        		
                        		i1 = -i1;
                       			pct_feed += 15 + 15 * i1/310;
                       			
                       		} else {
                       			pct_feed += 15 * i1/310;	
                       		}
                        	c = Color.getHSBColor(1.80F, 1.00F, (float)(pct_feed/100));
                        	pct_feed = 70;
                        	break;
                        case NEST_PHEROMON:
                        	p.getNestIntensity().assimilate();
                        	int i2 = p.getNestIntensity().getInteger();
                        	
                        	if(i2 < 0) {
                        		
                        		i2 = -i2;
                       			pct_nest += 15 + 15 * i2/310;
                       			
                       		} else {
                       			pct_nest += 15 * i2/310;	
                       		}
                       		        	
                        	c = Color.getHSBColor(1.20F, 1.00F, (float)(pct_nest/100));
                        	pct_nest = 70;
                        	break;
                        case MARKER:
                        	c = Color.BLUE;
                        	break;
                        case NOTHING:
                        	c = cc;
                        	break;
                        default:
                        	break;
                        	
                    }
                    int rgb = c.getRGB();
                    Object data = Main.world.model.getDataElements(rgb, null);
                    for(int i = 0; i < Configuration.PATCH_SIZE; i++){
                        for(int j = 0; j < Configuration.PATCH_SIZE; j++){
                            Main.world.raster.setDataElements(x * Configuration.PATCH_SIZE + i, y * Configuration.PATCH_SIZE + j, data);
                        }
                    }                             
                }
            }

            Main.world.add(Main.world.panel);
            Main.world.panel.repaint();
            Main.world.validate();
            
            while(Main.world.suspended) {
            	boolean all = true;
            	for(Ant a : Main.world.list) {
            		if(!a.suspended) { 
            			all = false;
            		}
            	}
            	
            	if(!all) { break; }
            	
            	try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }
    
        
    }
    
    /*private static int getSeconds() {
		Date d = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("ss");
		return Integer.parseInt(ft.format(d).toString());
	}*/
}
