package treasurehuntermonolith;
 
import javax.swing.JPanel; 

import java.awt.Graphics; 
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent; 

import java.util.ArrayList; 
import java.util.HashSet; 
import java.util.Set;

/**
 *
 * @author tianq.zheng
 */
// Program to create a board that holds the squares and treasures of the game
public class Board extends JPanel implements MouseListener {
    final static private int ROWS = 22;
    final static private int COLS = 13; 
    final static private int BOARD_WIDTH = COLS * Square.getSideLength();
    final static private int BOARD_HEIGHT = ROWS * Square.getSideLength(); 
    
    final static private int TOTAL_TREASURES = 5; 
    final static private int MAX_TREASURE_GEN_ATTEMPTS = 10000;
    final static private int MIN_DIST_APART_TREASURES = 3 * Square.getSideLength();
    
    final static private String TREASURE_ALERT_FONT = "Serif";
    final static private int TREASURE_ALERT_SIZE = 75; 
    
    private Square[][] board;
    private ArrayList<Treasure> treasures;
    private GameInfo info; 

    public Board(GameInfo info) {
        board = new Square[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = new Square(); 
            }
        }
        treasures = new ArrayList<Treasure>();
        this.info = info; 
        
        // Add treasures to the board so that no two of them share a common
        // square 
        int attempts = 0; 
        while (treasures.size() < TOTAL_TREASURES) {
            Treasure current = new Treasure(); 
            
            boolean overlap = false;
            for (int i = 0; i < treasures.size(); i++) {
                if (current.checkOverlap(treasures.get(i), MIN_DIST_APART_TREASURES)) {
                    overlap = true; 
                    break; 
                } 
            }
            
            if (!overlap) {
                treasures.add(current);
            }
            
            attempts++; 
            if (attempts > MAX_TREASURE_GEN_ATTEMPTS) {
                System.out.println("WARNING: MAX TREASURE GENERATION ATTEMPTS EXCEEDED!");
                System.out.println("THERE ARE LESS TREASURES THAN EXPECTED.");
                break; 
            }
        }

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.WHITE);
        addMouseListener(this);
    }
    
    public static int getBoardWidth() {
        return BOARD_WIDTH; 
    } 
    
    public static int getBoardHeight() {
        return BOARD_HEIGHT; 
    }
    
    public void printBoard() {
        for (Square[] squareList : board) {
            for (Square square : squareList) {
                System.out.print(square + " ");
            }
            System.out.println();
        }
    }
    
    @Override 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw treasure if it is still active, otherwise just draw the outline
        for (Treasure t : treasures) {
            t.drawTreasure(g);
        }
        
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // Draw grid lines
                g.setColor(Color.BLACK);
                g.drawRect(
                    j * Square.getSideLength(), 
                    i * Square.getSideLength(), 
                    Square.getSideLength(), 
                    Square.getSideLength()
                );
                
                // Draw square if it is still active 
                board[i][j].drawSquare(g, i, j);       
            }
        }
    } 
    
    private boolean canBeRemoved(BoardCoordinate coordinate, int ID) {   
        if (!board[coordinate.getRow()][coordinate.getCol()].getActive()) {
            return false; 
        }
        
        if (!(coordinate.getRow() - 1 < 0)) {
            Square top = board[coordinate.getRow() - 1][coordinate.getCol()]; 
            if (top.getActive() && top.getID() == ID) {
                return true; 
            }
        }
        if (!(coordinate.getCol() + 1 >= COLS)) {
            Square right = board[coordinate.getRow()][coordinate.getCol() + 1]; 
            if (right.getActive() && right.getID() == ID) {
                return true; 
            }
        }
        if (!(coordinate.getRow() + 1 >= ROWS)) {
            Square bottom = board[coordinate.getRow() + 1][coordinate.getCol()]; 
            if (bottom.getActive() && bottom.getID() == ID) {
                return true; 
            }
        }
        if (!(coordinate.getCol() - 1 < 0)) {
            Square left = board[coordinate.getRow()][coordinate.getCol() - 1]; 
            if (left.getActive() && left.getID() == ID) {
                return true; 
            } 
        }
        
        return false; 
    }
    
    private void findSameIDAdjacents(
        BoardCoordinate coordinate, 
        int ID, 
        ArrayList<BoardCoordinate> sameIDAdjacents
    ) {
        Square current = board[coordinate.getRow()][coordinate.getCol()];
        
        if (!current.getActive() || current.getID() != ID) return; 
        
        // Update treasures
        for (Treasure t : treasures) {
            boolean before = t.getActive();
            t.removeOccupiedSquare(coordinate);
            if (!t.getActive() && t.getActive() != before) {
                info.increaseScore(t.getValue());
            }
        }
        
        current.setInactive();
        info.increaseScore(current.getValue());
        
        sameIDAdjacents.add(coordinate);
        
        if (!(coordinate.getRow() - 1 < 0)) {
            findSameIDAdjacents(
                new BoardCoordinate(coordinate.getRow() - 1, coordinate.getCol()), 
                ID, 
                sameIDAdjacents
            );
        }
        if (!(coordinate.getCol() + 1 >= COLS)) {
            findSameIDAdjacents(
                new BoardCoordinate(coordinate.getRow(), coordinate.getCol() + 1), 
                ID, 
                sameIDAdjacents
            );
        }
        if (!(coordinate.getRow() + 1 >= ROWS)) {
            findSameIDAdjacents(
                new BoardCoordinate(coordinate.getRow() + 1, coordinate.getCol()), 
                ID, 
                sameIDAdjacents
            );
        }
        if (!(coordinate.getCol() - 1 < 0)) {
            findSameIDAdjacents(
                new BoardCoordinate(coordinate.getRow(), coordinate.getCol() - 1), 
                ID, 
                sameIDAdjacents
            );
        }
    }
    
    private void findDifferentIDAdjacents(
        Set<BoardCoordinate> different,
        ArrayList<BoardCoordinate> same
    ) {
        for (BoardCoordinate coordinate : same) {
            if (!(coordinate.getRow() - 1 < 0)) {
                BoardCoordinate top = new BoardCoordinate(
                    coordinate.getRow() - 1,
                    coordinate.getCol()
                );
                if (board[top.getRow()][top.getCol()].getActive()) {
                    different.add(top);
                }
            }
            if (!(coordinate.getCol() + 1 >= COLS)) {
                BoardCoordinate right = new BoardCoordinate(
                    coordinate.getRow(),
                    coordinate.getCol() + 1
                );
                if (board[right.getRow()][right.getCol()].getActive()) {
                    different.add(right);
                }
            }
            if (!(coordinate.getRow() + 1 >= ROWS)) {
                BoardCoordinate bottom = new BoardCoordinate(
                    coordinate.getRow() + 1,
                    coordinate.getCol()
                );
                if (board[bottom.getRow()][bottom.getCol()].getActive()) {
                    different.add(bottom);
                }
            }
            if (!(coordinate.getCol() - 1 < 0)) {
                BoardCoordinate left = new BoardCoordinate(
                    coordinate.getRow(),
                    coordinate.getCol() - 1
                );
                if (board[left.getRow()][left.getCol()].getActive()) {
                    different.add(left);
                }
            }    
        }
    }
    
    private boolean gameOver() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (canBeRemoved(new BoardCoordinate(i, j), board[i][j].getID())) {
                    return false;
                }
            }
        }
        return true; 
    }
    
    @Override 
    public void mouseClicked(MouseEvent e) {
        BoardCoordinate currentCoordinate = new BoardCoordinate(
            e.getY() / Square.getSideLength(),
            e.getX() / Square.getSideLength()
        );
        int currentID = board[currentCoordinate.getRow()][currentCoordinate.getCol()].getID();

        // Check if current square can be removed
        if (!canBeRemoved(currentCoordinate, currentID)) return;
        
        // Find and remove all adjacent squares with the same ID (recursive),
        // and update treasures
        ArrayList<BoardCoordinate> sameIDAdjacents = new ArrayList<BoardCoordinate>();
        findSameIDAdjacents(currentCoordinate, currentID, sameIDAdjacents);
        
        // Find and increment type of all adjacent squares with different IDs 
        Set<BoardCoordinate> differentIDAdjacents = new HashSet<BoardCoordinate>();
        findDifferentIDAdjacents(differentIDAdjacents, sameIDAdjacents);
        for (BoardCoordinate coordinate : differentIDAdjacents) {
            board[coordinate.getRow()][coordinate.getCol()].incrementType();
        }
        
        // Check if there are still possible moves 
        if (gameOver()) {
            info.setGameOverTrue();
        }
        
        repaint();
    }

    @Override 
    public void mouseEntered(MouseEvent e) {
        
    }
    
    @Override 
    public void mouseExited(MouseEvent e) {
        
    }
    
    @Override 
    public void mousePressed(MouseEvent e) {
        
    }
    
    @Override 
    public void mouseReleased(MouseEvent e) {
        
    } 
}
