package main.ants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import main.Main;
import main.datatype.IValue;
import main.enums.Direction;
import main.enums.Mode;
import main.misc.Configuration;

public class Movement extends Thread {
    
    private final Ant ant;
    private final Random rnd;   
    public static PrintWriter bf = null;
     private HashSet<Direction> set = new HashSet<Direction>();
    
    public Movement(Ant ant) {
        this.ant = ant;
        rnd = new Random();
    }
    
	@Override
    public void run() {
    	File f = new File("C:\\Users\\Carina\\Desktop\\debug.txt");
		try {
			bf = new PrintWriter(new BufferedWriter(new FileWriter(f)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
        while(true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
            
            //write status in debug file
            if(Configuration.DEBUG == 1) {  	
                bf.write("#" + ant.moves + "# | Intensity: " + this.ant.intensity.toString() + " | " 
                			 +  Main.world.getPatchInfo(this.ant.x, this.ant.y) + "\r\n");
            }
            
            if(ant.back) {
            	if(ant.checkObject(Mode.NEST) != null) {
            		//change intensity before the move
            		//check if changes need to be made to pickup food
            		ant.move(ant.checkObject(Mode.NEST));
            		ant.back = false;
            		ant.intensity = new IValue(1.0, 50);
            		continue;
            	}            	
            } else {
            	if(ant.checkObject(Mode.FEED)!= null){
            		ant.move(ant.checkObject(Mode.FEED));
            		ant.back = true;
            		ant.intensity = new IValue(1.0, 50);
            		continue;
            	}  
            }
            pheromonMove();
            
            while(Main.world.suspended) {
            	ant.suspended = true;
            	try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            } 
        }
    }
    
    private void randomMove(int d1, int d2, int d3) {

        int rand = rnd.nextInt(100)+1;
        int dir = ant.direction.ordinal();          
        if(rand<=d1){
            ant.move(ant.direction);
        } else if(d1 < rand && rand <= d2){
            ant.move(Direction.parse((dir + 1) % 4));
        } else if(d2 < rand && rand <= d3) {
            ant.move(Direction.parse((dir + 2) % 4));
        } else {
            ant.move(Direction.parse((dir + 3) % 4));
        }  
    }
    
    //relies on never reaching MAX_VALUE :(
    private void pheromonMove() {
    	Mode m = null;
    	if(ant.back) {
    		m = Mode.NEST_PHEROMON;
    	} else {
    		m = Mode.FEED_PHEROMON;
    	}
    	
    	int dir = ant.direction.ordinal();	
    	IValue i0, i1, i2, i3;
    	double b0, b1, b2, b3;
    	
    	//parse intestity values dealing with wall neighbours
    	try {
    		i0 = ant.getNearIntensity(ant.direction, m);
    		b0 = (double) i0.getInteger();
    	} catch(Exception e) {
    		i0 = new IValue(0.0, Integer.MAX_VALUE);
    		b0 = Double.MAX_VALUE;
    	}
		try {
			i1 = ant.getNearIntensity(Direction.parse((dir + 1) % 4), m); 
			b1 = (double) i1.getInteger();
    	} catch(Exception e) {
    		i1 = new IValue(0.0, Integer.MAX_VALUE);
    		b1 = Double.MAX_VALUE;
		}
		try {
			i2 = ant.getNearIntensity(Direction.parse((dir + 2) % 4), m); 
			b2 = (double) i2.getInteger();
		}catch(Exception e){
			i2 = new IValue(0.0, Integer.MAX_VALUE);
			b2 = Double.MAX_VALUE;
		}
		try {
			i3 = ant.getNearIntensity(Direction.parse((dir + 3) % 4), m); 
			b3 = (double) i3.getInteger();
		} catch(Exception e) {
    		 i3 = new IValue(0.0, Integer.MAX_VALUE);
    		 b3 = Double.MAX_VALUE;
    	}
		
		//randomMove if no Intensity on neigbour patches
		IValue[] array = {i0,i1,i2,i3};
		int counter = 0;
		for(int i = 0; i < array.length; i++) {
			if(array[i].getDouble() == 0.0) {
				counter++;
			}
		}
		if(counter == 4) {
			randomMove(50,70,75);
			counter = 0;
			return;
		}
		
		ArrayList<Double> bs = new ArrayList<>(Arrays.asList(b0, b1, b2, b3));
		double add = - Collections.min(bs) + 10;
		
		//make all values non-negative
		for(int i = 0; i < bs.size(); i++) {
			if(bs.get(i) == Double.MAX_VALUE) {
				bs.set(i, 0.0);
			} else if(bs.get(i) == 0.0) {
				bs.set(i, 1.0);
			} else {
				bs.set(i, bs.get(i) + add);
			}
		}
		
		//prioritize highest values
		double max = Collections.max(bs);
		int occurences = Collections.frequency(bs, max);
		for(int i = 0; i < bs.size(); i++) {
			if(bs.get(i) == max) {
				bs.set(i, max * 100 / occurences);
			}
		}
		
		//calculate percentages for each direction
		double b = 0.0;
		for(double d : bs) { b += d; }
		for(int i = 0; i < bs.size(); i++) {
			bs.set(i, bs.get(i) / b * 100);
		}
        
		b0 = bs.get(0);
		b1 = bs.get(1);
		b2 = bs.get(2);
        set.clear();
        randomMove((int) Math.ceil(b0),(int) Math.ceil(b0 + b1),(int) Math.ceil(b0 + b1 + b2));	
    }
}
