package main.ants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
            
            //debug
            if(Configuration.DEBUG == 1) {  	
                bf.write("#" + ant.moves + "# | Intensity: " + this.ant.intensity.toString() + " | " +  Main.world.getPatchInfo(this.ant.x, this.ant.y) + "\r\n");
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
            	pheromonMove();
            	
            } else {
            	
            	if(ant.checkObject(Mode.FEED)!= null){
            		ant.move(ant.checkObject(Mode.FEED));
            		ant.back = true;
            		ant.intensity = new IValue(1.0, 50);
            		continue;
            	}
            	pheromonMove();   
            	
            }
            
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
        //for test purpose always go in one direction
    	//ant.move(ant.direction);
        int rand = rnd.nextInt(100)+1;
        //System.out.println(rand);            
        if(rand<=d1){
            ant.move(ant.direction);
        } else if(d1 < rand && rand <= d2){
            ant.move(Direction.parse((ant.direction.ordinal() + 1) % 4));
        } else if(d2 < rand && rand <= d3) {
            ant.move(Direction.parse((ant.direction.ordinal() + 2) % 4));
        } else {
            ant.move(Direction.parse((ant.direction.ordinal() + 3) % 4));
        }  
    }
    
    private void pheromonMove() {
    	if(ant.back) {
    		IValue i0 = ant.getNearIntensity(ant.direction, Mode.NEST_PHEROMON); 
    		IValue i1 = ant.getNearIntensity(Direction.parse((ant.direction.ordinal() + 1) % 4), Mode.NEST_PHEROMON); 
    		IValue i2 = ant.getNearIntensity(Direction.parse((ant.direction.ordinal() + 2) % 4), Mode.NEST_PHEROMON); 
    		IValue i3 = ant.getNearIntensity(Direction.parse((ant.direction.ordinal() + 3) % 4), Mode.NEST_PHEROMON); 

    		IValue biggest = i0;
    		int dirBiggest = 0;
    		int counter = 0;
    		
    		IValue[] array = {i0,i1,i2,i3};
    		
    		for(int i = 0; i < array.length; i++) {
    			if(array[i].getDouble() == 0.0) {
    				counter++;
    			}
    			if(array[i].isHigher(biggest)) {
    				biggest = array[i];
    				dirBiggest = i;
    			}
    		}
    		
    		if(counter == 4) {
    			randomMove(50,70,75);
    			counter = 0;
    			return;
    		}
    		
    		counter = 0;
    		

    		/*double i = Math.floor(i0 + i1 + i2 + i3);
    		array[0] += i*0.2;
    		array[1] += i*0.1;
    		array[2] += i*0.05;
    		array[3] += i*0.1;
    		array[i__] += i*0.7;
    		i += i*1.15;
    		set.clear();
    		randomMove((int) Math.floor((i0/i)*100), (int) Math.floor((i1/i)*100), (int) Math.floor((i2/i)*100));*/
    		Direction d_ = Direction.parse((ant.direction.ordinal() + dirBiggest) % 4);
    		ant.move(d_);

    	} else {   
    		
    		IValue i0 = ant.getNearIntensity(ant.direction, Mode.FEED_PHEROMON); 
    		IValue i1 = ant.getNearIntensity(Direction.parse((ant.direction.ordinal() + 1) % 4), Mode.FEED_PHEROMON); 
    		IValue i2 = ant.getNearIntensity(Direction.parse((ant.direction.ordinal() + 2) % 4), Mode.FEED_PHEROMON); 
    		IValue i3 = ant.getNearIntensity(Direction.parse((ant.direction.ordinal() + 3) % 4), Mode.FEED_PHEROMON); 

    		IValue biggest = i0;
    		int dirBiggest = 0;
    		int counter = 0;
    		
    		IValue[] array = {i0,i1,i2,i3};
    		
    		for(int i = 0; i < array.length; i++) {
    			if(array[i].getDouble() == 0.0) {
    				counter++;
    			}
    			if(array[i].isHigher(biggest)) {
    				biggest = array[i];
    				dirBiggest = i;
    			}
    		}
    		
    		if(counter == 4) {
    			randomMove(50,70,75);
    			counter = 0;
    			return;
    		}
    		
    		counter = 0;
    		

    		Direction d_ = Direction.parse((ant.direction.ordinal() + dirBiggest) % 4);
    		ant.move(d_);
    		
    		
    		
    		
    		
    		/*IValue i0 = ant.getNearIntensity(ant.direction, Mode.FEED_PHEROMON); 
    		IValue i1 = ant.getNearIntensity(Direction.parse((ant.direction.ordinal() + 1) % 4), Mode.FEED_PHEROMON); 
    		IValue i2 = ant.getNearIntensity(Direction.parse((ant.direction.ordinal() + 2) % 4), Mode.FEED_PHEROMON); 
    		IValue i3 = ant.getNearIntensity(Direction.parse((ant.direction.ordinal() + 3) % 4), Mode.FEED_PHEROMON); 

    		IValue[] array = {i0,i1,i2,i3};
    		for(int i = 0; i < array.length; i++) {
    			if(array[i].getDouble() == 0.0) {
    				set.add(Direction.parse((ant.direction.ordinal() + i) % 4));
    			}
    		}
            
            if(set.size() == 4) {
            	randomMove(50,70,75);
                set.clear();
            	return;
            }

            IValue i = new IValue(0.0, 0);
            try { i.add(i0); i.add(i1); i.add(i2); i.add(i3); } catch (Exception ex) { i.set(Double.MAX_VALUE, Integer.MIN_VALUE); }
            
            try { i0 = i0.add(IValue.div(i, 5)); } catch (Exception ex) { i0.set(Double.MAX_VALUE, Integer.MIN_VALUE); } 
            try { i1 = i1.add(IValue.div(i, 10)); } catch (Exception ex) { i1.set(Double.MAX_VALUE, Integer.MIN_VALUE); }
            try { i2 = i2.add(IValue.div(i, 20)); } catch (Exception ex) { i2.set(Double.MAX_VALUE, Integer.MIN_VALUE); }
    		try { i3 = i3.add(IValue.div(i, 10)); } catch (Exception ex) { i3.set(Double.MAX_VALUE, Integer.MIN_VALUE); }
    		
    		IValue i_ = new IValue(0.0, 0);
    		try { i_.add(i0); i_.add(i1); i_.add(i2); i_.add(i3); } catch (Exception ex) { i_.set(Double.MAX_VALUE, Integer.MIN_VALUE); }
    		
            set.clear();
            randomMove((int) Math.round(((i0.frac(i_)).parseToDouble())*100), (int) Math.round(((i1.frac(i_)).parseToDouble())*100), (int) Math.round(((i2.frac(i_)).parseToDouble())*100));
        */}
    }
    
    
}
