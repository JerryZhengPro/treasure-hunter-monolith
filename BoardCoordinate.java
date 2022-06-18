package treasurehuntermonolith;

/**
 *
 * @author tianq.zheng
 */
// Program to create a board coordinate, which stores a row and column (to be used 
// with the board) 
public class BoardCoordinate {
    private int row; 
    private int col; 

    public BoardCoordinate(int row, int col) {
        this.row = row; 
        this.col = col; 
    }

    public int getRow() {
        return row; 
    }

    public int getCol() {
        return col; 
    }

    @Override 
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    @Override 
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }

    @Override 
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BoardCoordinate other = (BoardCoordinate)obj;

        return row == other.row && col == other.col; 
    }     
}
