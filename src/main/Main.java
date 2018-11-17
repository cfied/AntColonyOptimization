//walls 1,49,99,49-1,51,99,51
//walls only food & nest 1,24,49,24-1,26,49,26-18,25,18,25-21,25,21,25

package main;

import main.misc.Configuration;

public class Main{
    
    public static World world;
   
    public static void main(String[] args) throws InterruptedException {
    	Configuration.init();
    	Thread.sleep(500L);
        world = new main.World(Configuration.WORLD_HEIGHT, Configuration.WORLD_LENGTH);
        world.initalize();
        Thread updating = new Thread(new Update());
        updating.start();
        world.setVisible(true);
    }
               
}

