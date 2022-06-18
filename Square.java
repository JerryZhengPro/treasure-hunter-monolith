package treasurehuntermonolith;

import java.awt.Color; 
import java.awt.FontMetrics; 
import java.awt.Graphics; 

/**
 *
 * @author tianq.zheng
 */
// Program to create a square that will be on the board
public class Square {
    final static private int SIDE_LENGTH = 35; 
    final static private SquareType[] TYPE_LIST = {
        new SquareType(1, Color.GREEN),
        new SquareType(2, Color.MAGENTA),
        new SquareType(3, Color.YELLOW),
        new SquareType(4, Color.CYAN),
        new SquareType(5, Color.RED)
    };
    
    private int index; 
    private boolean active; 
    
    public Square() {
        this.index = (int)(Math.random() * TYPE_LIST.length);
        active = true; 
    }
    
    public static int getSideLength() {
        return SIDE_LENGTH; 
    }
    
    public int getID() {
        return TYPE_LIST[index].getID(); 
    }
    
    public boolean getActive() {
        return active; 
    }
    
    public void setInactive() {
        active = false; 
    }
    
    public void incrementType() {
        index++;
        if (index > TYPE_LIST.length - 1) {
            index = 0; 
        }
    }
    
    public int getValue() {
        return TYPE_LIST[index].getValue();
    }
    
    public void drawSquare(Graphics g, int row, int col) {
        if (!active) return;

        // Draw the square itself
        g.setColor(TYPE_LIST[index].getColor());
        g.fillRect(
            col * SIDE_LENGTH, 
            row * SIDE_LENGTH, 
            SIDE_LENGTH, 
            SIDE_LENGTH
        );
        g.setColor(Color.BLACK);
        g.drawRect(
            col * SIDE_LENGTH, 
            row * SIDE_LENGTH, 
            SIDE_LENGTH, 
            SIDE_LENGTH
        );
        
        // Draw text on square
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String text = String.valueOf(TYPE_LIST[index].getID());
        g.setColor(Color.BLACK);
        g.drawString(
            text, 
            (int)(SIDE_LENGTH * (col + 0.5) - metrics.stringWidth(text) / 2), 
            (int)(SIDE_LENGTH * (row + 0.5) + metrics.getAscent() / 2)
        );
    }
    
    @Override 
    public String toString() {
        return String.valueOf(TYPE_LIST[index].getID()); 
    }
    
    static private class SquareType {        
        private int ID; 
        private Color color; 
        private int value; 
        
        public SquareType(int ID, Color color) {
            this.ID = ID; 
            this.color = color; 
            value = ID;
        }
        
        public int getID() {
            return ID; 
        }
        
        public Color getColor() {
            return color; 
        }
        
        public int getValue() {
            return value; 
        }
    }
}
