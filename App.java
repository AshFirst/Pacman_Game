import javax.swing.JFrame; // For the window 32x32px x 21x19


public class App {
    public static void main(String[] args) throws Exception {
        int rowCount = 21; // number of row
        int columnCount = 19; // nmber of columna
        int tileSize = 32; // size of each pixel
        int boardWidth = columnCount*tileSize;
        int boardHeight = rowCount*tileSize;

        JFrame frame = new JFrame("Pack Man"); // title
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); // centre of window
        frame.setResizable(false); // resizing not allowed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit when pressed closed

        pacman game  = new pacman(); // instance of pacman
        frame.add(game); // add pacman class to winndow
        frame.pack(); // adjust game in the window
        game.requestFocus();
        frame.setVisible(true);
        


    }
}
