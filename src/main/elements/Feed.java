package main.elements;

import main.Main;

public class Feed {
    public Integer x1;
    public Integer x2;
    public Integer y1;
    public Integer y2;
    
    public Feed(int x1, int y1, int x2, int y2) {
        Main.world.createObject(x1, y1, x2, y2, main.enums.Mode.FEED);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}
