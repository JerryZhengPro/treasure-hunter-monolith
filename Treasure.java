package treasurehuntermonolith;

import java.awt.Graphics; 
import java.awt.Color; 

import java.util.ArrayList; 

/**
 *
 * @author tianq.zheng
 */
// Program to create a treasure that will be on the board 
public class Treasure {
    final static private TreasureType[] TYPE_LIST = {
        new TreasureType(25, CustomColor.getBronze()),
        new TreasureType(30, CustomColor.getSilver()),
        new TreasureType(35, CustomColor.getGold()),
        new TreasureType(40, CustomColor.getPlatinum()),
        new TreasureType(45, CustomColor.getDiamond())
    };
    
    private int index; 
    private int originX; // Upper left corner of the enclosing rectangle
    private int originY; // Upper left corner of the enclosing rectangle
    private boolean active; 
    private ArrayList<BoardCoordinate> occupiedSquares;
    
    public Treasure() {
        index = (int)(Math.random() * TYPE_LIST.length);
        originX = (int)(Math.random() * Board.getBoardWidth());
        originY = (int)(Math.random() * Board.getBoardHeight());
        if (originX + TYPE_LIST[index].getRadius() * 2 >= Board.getBoardWidth()) {
            originX -= TYPE_LIST[index].getRadius() * 2;
        }
        if (originY + TYPE_LIST[index].getRadius() * 2 >= Board.getBoardHeight()) {
            originY -= TYPE_LIST[index].getRadius() * 2;
        }
        active = true; 
        occupiedSquares = new ArrayList<BoardCoordinate>();
        
        // Populate occupiedSquares
        int bottomBoundary = originY + TYPE_LIST[index].getRadius() * 2 + Square.getSideLength();
        int rightBoundary = originX + TYPE_LIST[index].getRadius() * 2 + Square.getSideLength();
        for (int i = originY; i < bottomBoundary; i += Square.getSideLength()) {
            for (int j = originX; j < rightBoundary; j += Square.getSideLength()) {
                if (willBeOccupied(
                    j - j % Square.getSideLength(),
                    i - i % Square.getSideLength(),
                    j - j % Square.getSideLength() + Square.getSideLength(),
                    i - i % Square.getSideLength() + Square.getSideLength()
                )) {
                    occupiedSquares.add(
                        new BoardCoordinate(
                            i / Square.getSideLength(), 
                            j / Square.getSideLength()
                        )
                    );
                }
            }
        }
    }
    
    // Helper method to check overlap between two treasures (so that their 
    // closest points are at least a given distance apart from each other) 
    public boolean checkOverlap(Treasure other, int minDistanceApart) {
        double distanceApart = Math.sqrt(
            Math.pow(originX - other.originX, 2) 
            + Math.pow(originY - other.originY, 2)
        ); 
        return distanceApart <= TYPE_LIST[index].getRadius() 
            + TYPE_LIST[other.index].getRadius() 
            + minDistanceApart; 
    }
    
    public void drawTreasure(Graphics g) {
        if (active) {
            g.setColor(TYPE_LIST[index].getColor());
            g.fillOval(
                originX, 
                originY, 
                TYPE_LIST[index].getRadius() * 2, 
                TYPE_LIST[index].getRadius() * 2
            );
        } 
        g.setColor(Color.BLACK);
        g.drawOval(
            originX, 
            originY, 
            TYPE_LIST[index].getRadius() * 2, 
            TYPE_LIST[index].getRadius() * 2
        );
    }
    
    public void removeOccupiedSquare(BoardCoordinate coordinate) {
        occupiedSquares.remove(coordinate);
        if (occupiedSquares.isEmpty()) {
            active = false; 
        }
    }
    
    public void printOccupiedSquares() {
        for (BoardCoordinate coordinate : occupiedSquares) {
            System.out.println(coordinate);
        }
    }
    
    public boolean getActive() {
        return active; 
    }
    
    public int getValue() {
        return TYPE_LIST[index].getValue();
    }
    
    public static int getTypeListLength() {
        return TYPE_LIST.length; 
    }
    
    public static int getTreasureTypeRadius(int index) {
        return TYPE_LIST[index].getRadius();
    }
    
    public static Color getTreasureTypeColor(int index) {
        return TYPE_LIST[index].getColor();
    }
    
    public static int getTreasureTypeValue(int index) {
        return TYPE_LIST[index].getValue();
    }
    
    // Helper method to check overlap between a circle (current treasure) and 
    // rectangle (square on the board), requires rectangle's two corners that 
    // are diagonal to each other 
    private boolean willBeOccupied(int x1, int y1, int x2, int y2) {
        int xC = originX + TYPE_LIST[index].getRadius(); 
        int yC = originY + TYPE_LIST[index].getRadius(); 
        int xClosest = Math.max(x1, Math.min(xC, x2));
        int yClosest = Math.max(y1, Math.min(yC, y2));
        
        int dx = xClosest - xC; 
        int dy = yClosest - yC; 
        
        return Math.pow(dx, 2) + Math.pow(dy, 2) <= Math.pow(TYPE_LIST[index].getRadius(), 2); 
    } 
    
    static private class TreasureType {
        final static private int VALUE_EXPONENT = 2; 
        
        private int radius; 
        private Color color;
        private int value; 
        
        public TreasureType(int radius, Color color) {
            this.radius = radius; 
            this.color = color; 
            value = (int)Math.pow(radius, VALUE_EXPONENT);
        }
        
        public int getRadius() {
            return radius; 
        }
        
        public Color getColor() {
            return color; 
        }
        
        public int getValue() {
            return value; 
        }
    }
}
