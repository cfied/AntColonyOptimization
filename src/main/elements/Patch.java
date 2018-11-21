package main.elements;

import main.datatype.IValue;
import main.enums.Mode;

public class Patch {
    
    private IValue pFeed;
    private IValue pNest;
    private Boolean wall;
    private Integer feed;
    private Boolean nest;
    private Integer ant;
    private Boolean marker;
    
    public Patch(){
        this.pFeed = new IValue(0.0, 0);  
        this.wall = false;  
        this.feed = 0;  
        this.nest = false;  
        this.pNest = new IValue(0.0, 0);   
        this.ant = 0; 
        this.marker = false;
    }
    
    public void addAnt() {
        this.ant += 1;
    }
    
    public void removeAnt() {
    	this.ant -= 1;
    }
    
    public void setWall(boolean b) {
    	this.wall = b;
    }
    
    public void setFeed() {
    	this.feed = 50;
    }
    
    public void setFeed(int i) {
    	this.feed = i;
    }
    public int getFeed() {
        return this.feed;
    }
    
    public void setNest(boolean b) {
    	this.nest = b;
    }
    
    public boolean isWall() {
        return this.wall;
    }
    
    public void setNestIntensity(IValue intensity) {
        this.pNest = intensity;
    }
    
    public void setFeedIntensity(IValue intensity) {
        this.pFeed = intensity; 
    }
    
    public IValue getFeedIntensity() {
        return new IValue(pFeed.getDouble(),pFeed.getInteger());
    }
    
    public IValue getNestIntensity() {
        return new IValue(pNest.getDouble(),pNest.getInteger());
    }
    
    public boolean isFeed() {
        return this.feed > 0;
    }
    
    public boolean isNest() {
        return this.nest;
    }
    
    public boolean isAnt() {
        return this.ant > 0;
    }
    
    public void setMarker() {
    	this.marker = true;
    }
    
    public void removeMarker() {
    	this.marker = false;
    }
    
    public main.enums.Mode getMode() {
        if(this.ant > 0) {
            return Mode.ANT;
        } else if(this.marker) {
           	return Mode.MARKER;
        } else if(this.wall) {
            return Mode.WALL;
        } else if(this.nest) {
            return Mode.NEST;
        } else if(this.feed>0) {
            return Mode.FEED;
        } else if(!getFeedIntensity().isZero()) {
            return Mode.FEED_PHEROMON;
        } else if(!getNestIntensity().isZero()) {
            return Mode.NEST_PHEROMON;   
        } else {
            return Mode.NOTHING;
        }
    }

}
