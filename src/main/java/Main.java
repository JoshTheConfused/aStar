import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.MouseListener;

public class Main extends JFrame {
    static BoardPiece[][] board = new BoardPiece[158][83];
    //Board used to run the algorithm in, changing its size will not translate to visuals

    public Main() { //Used to set up the window and settings with swing and is called in main
        super("A*");

        DrawPane drawPane = new DrawPane();
        setContentPane(drawPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1912, 1024);
        setVisible(true);
    }

    class DrawPane extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0,0, 1897,997);
            getContentPane().setLayout(null);

            int x;
            int y;
            for (int i = 0; i < 158; i++) {
                for (int j = 0; j < 83; j++) {
                    x = (12 * i) + 1;
                    y = (12 * j) + 1;
                    g.setColor(board[i][j].getColor());
                    g.fillRect(x, y, 10, 10);
                }
            } //Creates a 158X83 grid of squares to use as the space in which to run the algorithm
        }
    }

    public static void setup() {
        for (int i = 0; i < 158; i++) {
            for (int j = 0; j < 83; j++) {
                board[i][j] = new BoardPiece(i, j);
            }
        } //Resets the board fully with new pieces
    }

    public static void main(String[] args){
        setup(); //Fills the board with empty pieces
        JFrame frame = new Main(); //Starts the window and allows for various listeners below

        final boolean[] hasStart = {false};
        final boolean[] hasEnd = {false};
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            int x;
            int y;
            @Override
            public void mousePressed(MouseEvent e) {
                x = (e.getX() - 8) / 12;
                y = (e.getY() - 32) / 12;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (board[x][y].getColor() == Color.WHITE) {
                        board[x][y].setColor(Color.BLACK);
                        board[x][y].setWall(true);
                    }
                    else if (board[x][y].getColor() == Color.BLACK) {
                        board[x][y].setColor(Color.WHITE);
                        board[x][y].setWall(false);
                    }
                } //Places or removes wall pieces with a left click
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    if (board[x][y].getColor() == Color.WHITE) {
                        if (!hasStart[0]) {
                            board[x][y].setColor(Color.BLUE);
                            board[x][y].setStart(true);
                            hasStart[0] = true;
                        }
                        else if (!hasEnd[0]) {
                            board[x][y].setColor(Color.BLUE);
                            board[x][y].setEnd(true);
                            hasEnd[0] = true;
                        }
                    }
                    else if (board[x][y].getColor() == Color.BLUE) {
                        if (board[x][y].isStart()) {
                            board[x][y].setStart(false);
                            hasStart[0] = false;
                        }
                        if (board[x][y].isEnd()) {
                            board[x][y].setEnd(false);
                            hasEnd[0] = false;
                        }
                        board[x][y].setColor(Color.WHITE);
                    }
                } //Places or removes start/end pieces with a right click
                //Start and end pieces are visually the same, but the path should be the same in either direction
                frame.repaint(); //Necessary to see changes
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        final boolean[] pathFound = {false};
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!pathFound[0]) {
                    try {
                        AStar.findPath(board);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("There must be a start point and an end point!!!");
                    }
                    pathFound[0] = true;
                }
                else {
                    setup();
                    hasStart[0] = false;
                    hasEnd[0] = false;
                    pathFound[0] = false;
                }
                frame.repaint();
            } //Any key press will run the algorithm and another will fully reset the board
            //This isn't the best for tinkering with the walls/obstacles/room, but fine for just the algorithm

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }
}