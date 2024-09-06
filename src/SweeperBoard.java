import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
public class SweeperBoard extends JPanel {
    int CELL_SIZE = 20;
    private int ROWS;
    private int COLUMNS;
    private int DIFFICULTY;
    private JPanel boardPanel;
    private final ArrayList<Cell> cellList;
    private boolean firstClick;
    private boolean win;
    private Cell firstCell;
    private int bombsLeft;
    private Minesweeper sweeper;
    public SweeperBoard(int columns, int rows, int bombs, Minesweeper minesweeper) {
        super();
        this.sweeper = minesweeper;
        this.COLUMNS = columns;
        this.ROWS = rows;
        this.DIFFICULTY = bombs;
        this.boardPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
        this.cellList = new ArrayList<Cell>();
        this.win = false;
        this.firstClick = false;
        this.bombsLeft = bombs;
        if (ROWS < 5){
            ROWS = 5;
        }
        if (COLUMNS < 5){
            COLUMNS = 5;
        }
        if (DIFFICULTY > ROWS*COLUMNS-9){
            DIFFICULTY = ROWS*COLUMNS-9;
        }
        cellSetup();
        addCellsToPanel();
    }
    public JPanel getBoardPanel(){ return this.boardPanel; }
    public int getROWS(){ return ROWS; }
    public int getCOLUMNS(){ return COLUMNS; }
    public void incrementBombsLeft(){
        bombsLeft ++;
        sweeper.updateCounter();
    }
    public void decrementBombsLeft(){
        bombsLeft--;
        sweeper.updateCounter();
    }
    public int getBombsLeft(){ return bombsLeft; }
    public ArrayList<Integer> generateBombs(int bombNumber, int boardSize) {
        ArrayList<Integer> tileList = new ArrayList<Integer>();
        ArrayList<Integer> bannedList = new ArrayList<Integer>();
        Random rand = new Random();
        // Ban first square and all adjacent squares
        bannedList.add(firstCell.getCellNumber());
        for (Cell cell: firstCell.getAdjacentCells()){
            bannedList.add(cell.getCellNumber());
        }
        for (int i = 0; i < bombNumber; i++) {
            int tileNumber = rand.nextInt(boardSize);
            // Only add number to list if not already included AND isn't banned
            if ((! tileList.contains(tileNumber)) && (! bannedList.contains(tileNumber))){
                tileList.add(tileNumber);
            } else {
                i--;
            }
        }
        return tileList;
    }
    private void cellSetup(){
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLUMNS; j++){
                Cell newCell = new Cell(j, i, this);
                newCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                newCell.setBackground(Color.WHITE);
                newCell.setMargin(new Insets(0,0,0,0));
                cellList.add(newCell);
            }
        }
    }
    public Cell findCell(int x, int y){
        // Returns the cell with the specified coordinates
        for (Cell cell: this.cellList){
            if ((cell.getXCoord() == x) && (cell.getYCoord() == y)){
                return cell;
            }
        }
        return null;
    }
    private void addCellsToPanel(){
        for (Cell cell: cellList){
            cell.setVisible(true);
            boardPanel.add(cell);
        }
    }
    private void assignBombs(ArrayList bombList){
        for (int i = 0; i < ROWS*COLUMNS; i++){
            if (bombList.contains(i)) {
                findCell(i % COLUMNS, i / COLUMNS).setBomb(true);
                // findCell(i % COLUMNS, i / COLUMNS).setBackground(Color.RED);
            }
        }
    }
    private void assignNumbers(){
        for (Cell cell: cellList){
            if (! cell.isBomb()){
                int counter = 0;
                for (Cell cell2: cellList){
                    if ((cell2.isAdjacent(cell)) && (cell2.isBomb())){
                        counter ++;
                    }
                }
                cell.setBombNumber(counter);
            }
        }
    }
    public void cellClicked(Cell cell){
        if (! firstClick){
            this.firstCell = cell;
            assignBombs(generateBombs(DIFFICULTY, ROWS*COLUMNS));
            assignNumbers();
            firstCell.revealCell();
            firstClick = true;
        }
        // This only gets passed if all non-bomb squares are revealed. Passing this means win.
        for (Cell cellCheck: cellList){
            if (!cellCheck.isBomb()){
                if (!cellCheck.isRevealed()){
                    return;
                }
            }
        }
        gameWin();
    }
    public void tearDown(){
        for (Cell cell: cellList){
            cell.hideCell();
            cell.setLocked(false);
        }
        this.firstClick = false;
        this.win = false;
        this.bombsLeft = DIFFICULTY;
        sweeper.updateCounter();
    }
    public void gameWin(){
        for (Cell aCell: cellList){
            if (aCell.isBomb()){
                aCell.setFlagged(true);
                aCell.setBackground(Color.GREEN);
            }
            aCell.setLocked(true);
        }
        if (!win){
            win = true;
            JOptionPane.showMessageDialog(boardPanel, "You Win!");
        }
    }
    public void gameOver(){
        for (Cell aCell: cellList){
            aCell.setLocked(true);
            if (aCell.isBomb()){
                if (aCell.isFlagged()){
                    aCell.setBackground(Color.GREEN);
                } else {
                    aCell.setBackground(Color.RED);
                }
            } else {
                if (aCell.isFlagged()){
                    aCell.setBackground(Color.PINK);
                    aCell.setText("x");
                }
            }
        }
        JOptionPane.showMessageDialog(boardPanel, "Game Over!");
    }
}