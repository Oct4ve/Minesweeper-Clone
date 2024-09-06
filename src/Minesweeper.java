import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Minesweeper {
    private SweeperBoard gameBoard;
    private int columns = 14;
    private int rows = 10;
    private int bombs = 20;
    private final JMenu bombsRemaining;
    public Minesweeper() {
        JFrame gameFrame = new JFrame("MS Clone");
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        bombsRemaining = new JMenu("" + bombs);
        Minesweeper sweeper = this;
        JMenuItem settings = new JMenuItem("Difficulty");
        JMenuItem restart = new JMenuItem("Restart");
        JMenuItem quit = new JMenuItem("Quit");
        menu.add(settings);
        menu.add(restart);
        menu.add(quit);
        final JButton[] difficultyOptions = new JButton[5];
        difficultyOptions[0] = new JButton("Easy");
        difficultyOptions[1] = new JButton("Medium");
        difficultyOptions[2] = new JButton("Hard");
        difficultyOptions[3] = new JButton("Expert");
        difficultyOptions[4] = new JButton("Master");
        difficultyOptions[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                columns = 10;
                rows = 7;
                bombs = 8;
                bombsRemaining.setText("" + bombs);
                Window w = SwingUtilities.getWindowAncestor(difficultyOptions[0]);
                w.setVisible(false);
            }
        });
        difficultyOptions[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                columns = 14;
                rows = 10;
                bombs = 20;
                bombsRemaining.setText("" + bombs);
                Window w = SwingUtilities.getWindowAncestor(difficultyOptions[1]);
                w.setVisible(false);
            }
        });
        difficultyOptions[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                columns = 20;
                rows = 14;
                bombs = 50;
                bombsRemaining.setText("" + bombs);
                Window w = SwingUtilities.getWindowAncestor(difficultyOptions[2]);
                w.setVisible(false);
            }
        });
        difficultyOptions[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                columns = 27;
                rows = 18;
                bombs = 99;
                bombsRemaining.setText("" + bombs);
                Window w = SwingUtilities.getWindowAncestor(difficultyOptions[3]);
                w.setVisible(false);
            }
        });
        difficultyOptions[4].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                columns = 36;
                rows = 27;
                bombs = 216;
                bombsRemaining.setText("" + bombs);
                Window w = SwingUtilities.getWindowAncestor(difficultyOptions[4]);
                w.setVisible(false);
            }
        });
        JOptionPane.showOptionDialog(gameFrame, "Choose Your Difficulty!", "Minesweeper Clone! Author: Sam Wilson", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, difficultyOptions, 0);
        gameBoard = new SweeperBoard(columns, rows, bombs, sweeper);
        final JPanel[] gamePanel = {gameBoard.getBoardPanel()};
        int sizey = (34 * rows) + (5 * (rows - 1)); // IDK what these do but removing them breaks everything
        int sizex = (34 * columns) + (5 * (rows - 1));
        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                // Show settings menu to choose new difficulty
                JOptionPane.showOptionDialog(gameFrame, "Choose Your Difficulty!", "Minesweeper Clone - Difficulty", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, difficultyOptions, 0);
                // remove panel from frame
                gameFrame.remove(gamePanel[0]);
                // create new board and attach it to the frame
                gameBoard = new SweeperBoard(columns, rows, bombs, sweeper);
                gamePanel[0] = gameBoard.getBoardPanel();
                // add the panel back to the frame and pack it
                gameFrame.add(gamePanel[0]);
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
            }
        });
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                gameBoard.tearDown();
            }
        });
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        menuBar.add(menu);
        menuBar.add(bombsRemaining);
        gameFrame.setJMenuBar(menuBar);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(sizex, sizey);
        gameFrame.setResizable(false);
        gameFrame.add(gamePanel[0]);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
    public void updateCounter(){
        bombsRemaining.setText("" + gameBoard.getBombsLeft());
    }
}