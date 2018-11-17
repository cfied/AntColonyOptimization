package main.ants;

import main.Main;
import main.datatype.IValue;
import main.elements.Patch;
import main.enums.Mode;
import main.enums.Direction;

public class Ant {
    protected Integer x;
    protected Integer y;
    public IValue intensity;
    protected boolean back = false;
    protected boolean pheromon = false;
    protected Direction direction;
    protected int moves;
    protected boolean first = true;
    
    public boolean suspended = false;
    
    private Patch[][] field = Main.world.field;
    
    public IValue getIntensity(){
    	return new IValue(intensity.getDouble(),intensity.getInteger());
    }
    
    public Ant(Integer x, Integer y, Direction d) {
        this.x = x;
        this.y = y;
        this.direction = d;
        this.intensity = new IValue(1.0, 50);
        this.moves = 0;
        this.intensity.assimilate();
       
        this.handleIntensity(Mode.NEST);
        new Thread(new Movement(this)).start();
    }
    
    protected void move(Direction d) {
    	this.intensity.assimilate();
    	if((this.intensity.getDouble()==0.0)&&(first==true)) {
    		System.out.println(this.moves);
    		first = false;
    		
    	}
    	
        boolean success = false;
        //System.out.println(d.toString());
        switch(d) {
            case SOUTH:
                if(!field[this.x][this.y+1].isWall()) {
                    field[x][y].removeAnt();
                    this.y++;
                    success = true;
                }
                break;
            case EAST:
                if(!field[this.x+1][this.y].isWall()) {
                    field[x][y].removeAnt();
                    this.x++;
                    success = true;
                }
                break;
            case NORTH:
                if(!field[this.x][this.y-1].isWall()) {
                    field[x][y].removeAnt();
                    this.y--;
                    success = true;
                }
                break;
            case WEST:
                if(!field[this.x-1][this.y].isWall()) {
                    field[x][y].removeAnt();
                    this.x--;
                    success = true;
                }
                break;
        }
        
        if(success) {
        	//commented out to test IValue - essential part of the program
        	this.intensity.div(4);
            this.intensity.assimilate();
            System.out.println(this.intensity.toString());
            if(this.intensity.getDouble() < 1.0){
            	System.out.println("Faulty intensity");
            }
            field[x][y].addAnt();
            this.direction = d;            
            this.moves++;
            
        } else {
            this.direction = Direction.parse((d.ordinal()+2) % 4);
            return;
        }
        
        Patch p = field[x][y];
        
        if(this.back) {
        	this.handleIntensity(Mode.FEED);
            if(p.isNest()){
            	Main.world.broughtFood++;
            }
         
        } else {
        	this.handleIntensity(Mode.NEST);
            if(p.isFeed()) {
            	p.setFeed(p.getFeed() - 1);
            }
        }
        System.out.print("");
    }
    
    private void handleIntensity(Mode m) {
        switch(m) {
            case FEED:
                field[x][y].setFeedIntensity(field[x][y].getFeedIntensity().add(getIntensity())); //here too I guess
                if(!field[x+1][y].isWall()) { field[x+1][y].setFeedIntensity(field[x+1][y].getFeedIntensity().add(IValue.div(intensity, 20))); }
                if(!field[x-1][y].isWall()) { field[x-1][y].setFeedIntensity(field[x-1][y].getFeedIntensity().add(IValue.div(intensity, 20))); }
                if(!field[x][y+1].isWall()) { field[x][y+1].setFeedIntensity(field[x][y+1].getFeedIntensity().add(IValue.div(intensity, 20))); }
                if(!field[x][y-1].isWall()) { field[x][y-1].setFeedIntensity(field[x][y-1].getFeedIntensity().add(IValue.div(intensity, 20))); }
                break;
                
            case NEST:
                field[x][y].setNestIntensity(field[x][y].getNestIntensity().add(getIntensity())); //here the intensity is divided by 10 !!!
                if(!field[x+1][y].isWall()) { field[x+1][y].setNestIntensity(field[x+1][y].getNestIntensity().add(IValue.div(intensity, 20))); } 
                if(!field[x-1][y].isWall()) { field[x-1][y].setNestIntensity(field[x-1][y].getNestIntensity().add(IValue.div(intensity, 20))); }
                if(!field[x][y+1].isWall()) { field[x][y+1].setNestIntensity(field[x][y+1].getNestIntensity().add(IValue.div(intensity, 20))); }
                if(!field[x][y-1].isWall()) { field[x][y-1].setNestIntensity(field[x][y-1].getNestIntensity().add(IValue.div(intensity, 20))); }
                break;
             
		default:
			break;
        }
    }
    
    protected Direction checkObject(Mode m) {
        switch(m) {
            case FEED:
                if(field[this.x+1][this.y].isFeed()) {
                    return Direction.EAST;
                } else if(field[this.x-1][this.y].isFeed()) {
                    return Direction.WEST;
                } else if(field[this.x][this.y+1].isFeed()) {
                    return Direction.SOUTH;
                } else if(field[this.x][this.y-1].isFeed()) {
                    return Direction.NORTH;
                } else {
                    return null;
                }
            case NEST:
                if(field[this.x+1][this.y].isNest()) {
                    return Direction.EAST;
                } else if(field[this.x-1][this.y].isNest()) {
                    return Direction.WEST;
                } else if(field[this.x][this.y+1].isNest()) {
                    return Direction.SOUTH;
                } else if(field[this.x][this.y-1].isNest()) {
                    return Direction.NORTH;
                } else {
                    return null;
                }
            case NEST_PHEROMON:
                IValue a1 = field[this.x+1][this.y].getNestIntensity();
                IValue a3 = field[this.x-1][this.y].getNestIntensity();
                IValue a0 = field[this.x][this.y+1].getNestIntensity();
                IValue a2 = field[this.x][this.y-1].getNestIntensity();
                //search for biggest pheromon value
                Direction aDir = Direction.EAST;
                IValue a = a1;
                if(a3.isHigher(a)) {
                    a = a3;
                    aDir = Direction.WEST;
                }
                if(a0.isHigher(a)) {
                    a = a0;
                    aDir = Direction.SOUTH;
                }
                if(a2.isHigher(a)) {
                    aDir = Direction.NORTH;
                }
                if(a.getDouble() != 0.0) {
                	return aDir;
                } else {
                	return null;
                }
            case FEED_PHEROMON:
            	IValue b1 = field[this.x+1][this.y].getFeedIntensity();
            	IValue b3 = field[this.x-1][this.y].getFeedIntensity();
            	IValue b0 = field[this.x][this.y+1].getFeedIntensity();
            	IValue b2 = field[this.x][this.y-1].getFeedIntensity();
                //search for biggest pheromon value:
                Direction bDir = Direction.EAST;
                IValue b = b1;
                if(b3.isHigher(b)) {
                    b = b3;
                    bDir = Direction.WEST;
                }
                if(b0.isHigher(b)) {
                    b = b0;
                    bDir = Direction.SOUTH;
                }
                if(b2.isHigher(b)) {
                    bDir = Direction.NORTH;
                }
                if(b.getDouble() != 0.0){
                    return bDir;
                }else{
                    return null;
                }
            default:
            	System.out.println("No objects around here");
                return null;
        }
    }
    
    protected IValue getNearIntensity(Direction d, Mode m) {
        switch(m) {
            case FEED_PHEROMON:
                switch(d) {
                    case SOUTH:
                        return field[this.x][this.y+1].getFeedIntensity();
                    case EAST:
                        return field[this.x+1][this.y].getFeedIntensity();
                    case NORTH:
                        return field[this.x][this.y-1].getFeedIntensity();
                    case WEST:
                        return field[this.x-1][this.y].getFeedIntensity();
                }
            case NEST_PHEROMON:
                switch(d) {
                    case SOUTH:
                        return field[this.x][this.y+1].getNestIntensity();
                    case EAST:
                        return field[this.x+1][this.y].getNestIntensity();
                    case NORTH:
                        return field[this.x][this.y-1].getNestIntensity();
                    case WEST:
                        return field[this.x-1][this.y].getNestIntensity();
                }
            default:
            	System.out.println("No NearIntensity found");
               return new IValue(0.0,0); 
        }
    }
}