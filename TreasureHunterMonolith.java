package treasurehuntermonolith;

import javax.swing.BoxLayout;
import javax.swing.JFrame; 
import javax.swing.JPanel; 

/**
 *
 * @author tianq.zheng
 */
// Main program to run the game 
public class TreasureHunterMonolith {
    final static private String GAME_NAME = "Treasure Hunter Monolith";
    
    private JFrame f;
    private JPanel container; 
    private Board board;
    private GameInfo info; 
    
    public TreasureHunterMonolith() {
        f = new JFrame(GAME_NAME);
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        info = new GameInfo(); 
        board = new Board(info);
        
        container.add(board);
        container.add(info); 
        f.add(container); 
        
        f.setVisible(true); 
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    
    public static void main(String[] args) {
        TreasureHunterMonolith game = new TreasureHunterMonolith();
    }
}
