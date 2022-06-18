package treasurehuntermonolith;

import javax.swing.JPanel; 
import javax.swing.JButton; 

import java.awt.Graphics; 
import java.awt.Color; 
import java.awt.Dimension; 
import java.awt.FontMetrics;
import java.awt.Font; 

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author tianq.zheng
 */
// Program to create a game info side panel which will store the current score
// as well as the types of treasures that can be found. The game over screen
// will display here too. 
public class GameInfo extends JPanel implements ActionListener {
    final static private int GAME_INFO_HEIGHT = Board.getBoardHeight();
    final static private int GAME_INFO_WIDTH = 300; 
    
    final static private String SCORE_FONT = "Serif";
    final static private int SCORE_SIZE = 30; 
    
    final static private int ORIGIN_X_TREASURE = GAME_INFO_WIDTH / 2;
    final static private int ORIGIN_Y_TREASURE = GAME_INFO_HEIGHT / 5; 
    
    final static private int ORIGIN_X_TREASURE_BTN = GAME_INFO_WIDTH / 10; 
    final static private int ORIGIN_Y_TREASURE_BTN = ORIGIN_Y_TREASURE;
    final static private int TREASURE_BTN_WIDTH = 60;
    final static private int TREASURE_BTN_HEIGHT = 40; 
    
    private int score; 
    private int currentTreasureType; 
    private boolean gameOver; 
    
    private JButton prevTreasureTypeBtn; 
    private JButton nextTreasureTypeBtn; 
    
    public GameInfo() {
        score = 0; 
        currentTreasureType = 0; 
        gameOver = false; 
        
        // Initialize buttons
        prevTreasureTypeBtn = new JButton("<<");
        prevTreasureTypeBtn.addActionListener(this);
        add(prevTreasureTypeBtn);
        
        nextTreasureTypeBtn = new JButton(">>");
        nextTreasureTypeBtn.addActionListener(this);
        add(nextTreasureTypeBtn);
        
        // Draw buttons
        prevTreasureTypeBtn.setBounds(
            ORIGIN_X_TREASURE_BTN, 
            ORIGIN_Y_TREASURE_BTN, 
            TREASURE_BTN_WIDTH, 
            TREASURE_BTN_HEIGHT
        );
        nextTreasureTypeBtn.setBounds(
            GAME_INFO_WIDTH - ORIGIN_X_TREASURE_BTN - TREASURE_BTN_WIDTH, 
            ORIGIN_Y_TREASURE_BTN, 
            TREASURE_BTN_WIDTH, 
            TREASURE_BTN_HEIGHT
        );
        
        setPreferredSize(new Dimension(GAME_INFO_WIDTH, GAME_INFO_HEIGHT));
        setLayout(null);
        setBackground(Color.WHITE);
    }
    
    public void increaseScore(int amount) {
        score += amount; 
        repaint();
    }
    
    public void setGameOverTrue() {
        gameOver = true; 
    }
    
    @Override 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, GAME_INFO_WIDTH, GAME_INFO_HEIGHT);
        
        if (gameOver) {
            remove(prevTreasureTypeBtn);
            remove(nextTreasureTypeBtn);
            
            // Draw game over
            String textGameOver = "Game Over!";
            Font save = g.getFont();
            g.setFont(new Font(SCORE_FONT, Font.BOLD, SCORE_SIZE)); 
            FontMetrics metricsGameOver = g.getFontMetrics(g.getFont());
            g.setColor(Color.RED);
            g.drawString(
                textGameOver,
                (GAME_INFO_WIDTH - metricsGameOver.stringWidth(textGameOver)) / 2,
                GAME_INFO_HEIGHT / 3
            );
            
            // Draw final score
            String textFinalScore = "Final Score: " + score; 
            g.setFont(new Font(SCORE_FONT, Font.BOLD, SCORE_SIZE)); 
            FontMetrics metricsFinalScore = g.getFontMetrics(g.getFont());
            g.setColor(Color.BLACK);
            g.drawString(
                textFinalScore,
                (GAME_INFO_WIDTH - metricsFinalScore.stringWidth(textFinalScore)) / 2,
                GAME_INFO_HEIGHT / 2
            );
            g.setFont(save);
            
            return; 
        }
        
        // Draw score
        String textScore = String.valueOf(score);
        Font save = g.getFont();
        g.setFont(new Font(SCORE_FONT, Font.ITALIC, SCORE_SIZE)); 
        FontMetrics metricsScore = g.getFontMetrics(g.getFont());
        g.setColor(Color.BLACK);
        g.drawString(
            textScore, 
            (GAME_INFO_WIDTH - metricsScore.stringWidth(textScore)) / 2, 
            GAME_INFO_HEIGHT / 10
        );
        g.setFont(save);
        
        // Draw current treasure type 
        int radius = Treasure.getTreasureTypeRadius(currentTreasureType);
        
        // Draw current treasure type shape
        g.setColor(Treasure.getTreasureTypeColor(currentTreasureType));
        g.fillOval(
            ORIGIN_X_TREASURE - radius,
            ORIGIN_Y_TREASURE,
            radius * 2,
            radius * 2
        );
        g.setColor(Color.BLACK);
        g.drawOval(
            ORIGIN_X_TREASURE - radius,
            ORIGIN_Y_TREASURE,
            radius * 2,
            radius * 2
        );
        
        // Draw current treasure type value
        g.setColor(Color.BLACK);
        String textTreasureValue = String.valueOf(
            Treasure.getTreasureTypeValue(currentTreasureType)
        );
        FontMetrics metricsTreasureValue = g.getFontMetrics(g.getFont());
        g.drawString(
            textTreasureValue,
            (int)(ORIGIN_X_TREASURE - metricsTreasureValue.stringWidth(textTreasureValue) / 2), 
            (int)(ORIGIN_Y_TREASURE + radius + metricsTreasureValue.getAscent() / 2)
        );
        
    } 
    
    @Override 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextTreasureTypeBtn) {
            currentTreasureType++; 
            if (currentTreasureType >= Treasure.getTypeListLength()) {
                currentTreasureType = 0; 
            }
        } else if (e.getSource() == prevTreasureTypeBtn) {
            currentTreasureType--; 
            if (currentTreasureType < 0) {
                currentTreasureType = Treasure.getTypeListLength() - 1; 
            }
        }
        
        repaint();
    }
}
