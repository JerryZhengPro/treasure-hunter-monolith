package treasurehuntermonolith;

import java.awt.Color; 

/**
 *
 * @author tianq.zheng
 */
// Program to create a custom color class to store custom colors that aren't directly available
// in java.awt.Color 
public class CustomColor {
    final static private Color DIAMOND  = new Color(185, 242, 255); 
    final static private Color PLATINUM = new Color(229, 228, 226);
    final static private Color GOLD = new Color(255, 215, 0);
    final static private Color SILVER = new Color(192, 192, 192);
    final static private Color BRONZE = new Color(205, 127, 50);
    
    static public Color getDiamond() {
        return DIAMOND; 
    }
    
    static public Color getPlatinum() {
        return PLATINUM; 
    }
        
    static public Color getGold() {
        return GOLD; 
    }
            
    static public Color getSilver() {
        return SILVER; 
    }
                
    static public Color getBronze() {
        return BRONZE; 
    }
}
