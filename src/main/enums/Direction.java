package main.enums;

public enum Direction {
    
    SOUTH, EAST, NORTH, WEST;
    
    public static Direction parse(int i) {
        switch(i) {
            case 0:
                return Direction.SOUTH;
            case 1:
                return Direction.EAST;
            case 2:
                return Direction.NORTH;
            case 3:
                return Direction.WEST;
            default:
                return null;
        }
    }
    
}
