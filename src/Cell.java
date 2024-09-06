import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
public class Cell extends JButton {
    private boolean bomb;
    private int bombNumber;
    private boolean empty;
    private boolean flagged;
    private boolean revealed;
    private boolean locked;
    private final int x;
    private final int y;
    private final SweeperBoard gameBoard;
    public Cell(int xCoord, int yCoord, SweeperBoard board){
        this.bomb = false;
        this.empty = false;
        this.flagged = false;
        this.revealed = false;
        this.locked = false;
        this.bombNumber = 0;
        this.x = xCoord;
        this.y = yCoord;
        this.gameBoard = board;
        Cell thisCell = this;
        this.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e){
                if (e.getButton() == MouseEvent.BUTTON3){
                    // Right click flags a cell
                    toggleFlagged();
                } else if (e.getButton() == MouseEvent.BUTTON1){
                    if (thisCell.isRevealed()){
                        // If left click on revealed cell, reveal all adjacent cells
                        for (Cell adjacentCell: thisCell.getAdjacentCells()){
                            adjacentCell.revealCell();
                        }
                    } else {
                        // Left click reveals a cell
                        revealCell();
                        gameBoard.cellClicked(thisCell);
                    }
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }
    public void setBomb(boolean value){ this.bomb = value; }
    public void setRevealed(boolean value){ this.revealed = value; }
    public void setLocked(boolean value){ this.locked = value; }
    public void setBombNumber(int number){
        this.bombNumber = number;
        if (bombNumber == 0){
            this.empty = true;
        }
    }
    public void toggleFlagged(){
        if (!locked){
            if (! revealed){
                if (! flagged){
                    flagged = true;
                    this.setBackground(Color.RED);
                    gameBoard.decrementBombsLeft();
                } else {
                    flagged = false;
                    this.setBackground(Color.WHITE);
                    gameBoard.incrementBombsLeft();
                }
            }
        }
    }
    public void setFlagged(boolean newValue){
        if (newValue != flagged){
            toggleFlagged();
        }
    }
    public boolean isFlagged(){ return flagged; }
    public boolean isBomb(){ return bomb; }
    public boolean isEmpty(){ return empty; }
    public int getBombNumber(){ return bombNumber; }
    public int getXCoord(){ return x; }
    public int getYCoord(){ return y; }
    public boolean isRevealed(){ return revealed; }
    public void revealCell(){
        if (!this.locked){
            if (this.bomb) {
                if (! this.flagged){
                    // This means you lose
                    this.setText("x");
                    this.gameBoard.gameOver();
                }
            } else {
                if (! this.flagged){
                    this.setBackground(Color.GRAY);
                    if (this.bombNumber > 0){
                        this.setText(String.valueOf(bombNumber));
                    }
                    this.revealed = true;
                }
            }
            // Clear area with no bombs
            if (this.empty){
                for (Cell adjacentCell: this.getAdjacentCells()){
                    if (! adjacentCell.isRevealed()){
                        adjacentCell.revealCell();
                    }
                }
            }
        }
    }
    public boolean isAdjacent(Cell cell){
        if ((this.x - cell.getXCoord() >= -1) && (this.x - cell.getXCoord() <= 1)) {
            if ((this.y - cell.getYCoord() >= -1) && (this.y - cell.getYCoord() <= 1)) {
                return true;
            }
        }
        return false;
    }
    public ArrayList<Cell> getAdjacentCells(){
        ArrayList<Cell> adjacentCellList = new ArrayList<Cell>();
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                int xCoord = this.x + i;
                int yCoord = this.y + j;
                Cell adjacentCell = gameBoard.findCell(xCoord, yCoord);
                if (adjacentCell != null){
                    adjacentCellList.add(adjacentCell);
                }
                adjacentCellList.remove(this);
            }
        }
        return adjacentCellList;
    }
    public int getCellNumber(){
        return (y * gameBoard.getCOLUMNS()) + x;
    }
    public void hideCell(){
        setBomb(false);
        setRevealed(false);
        this.flagged = false;
        setBombNumber(0);
        this.empty = false;
        this.setText("");
        this.setBackground(Color.WHITE);
    }
}