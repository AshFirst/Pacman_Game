import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random; 
import javax.swing.*;
public class pacman extends JPanel implements ActionListener, KeyListener{ // to allow pacman class serve as canva

    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'U';
        int velocityX = 0;
        int velocityY=0; 


        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.height = height;
            this.width = width;
            this.x = x;
            this.y = y;
            this.startX = x;
            this.startY = y;


        }

        void updateDirection(char direction){
            char prevDirection  = this.direction;
            this.direction = direction; 
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for(Block wall:walls){ // if pacman hits the wall
                if(collision(this, wall)){
                    this.x -= this.velocityX;
                    this.y -=this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity(){
            if(this.direction=='U'){
                this.velocityX=0;
                this.velocityY= (-1)*(tileSize/4); // 32/4=8 every frame 8px up
            }
            else if(this.direction=='D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if(this.direction=='L'){
                this.velocityX = (-1)*tileSize/4;
                this.velocityY = 0 ;
            }
            else if(this.direction=='R'){
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }

        }
        void reset(){
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    private int rowCount = 21; // number of row
    private int columnCount = 19; // nmber of columna
    private int tileSize = 32; // size of each pixel
    private int boardWidth = columnCount*tileSize;
    private int boardHeight = rowCount*tileSize;

    //loading images
    private Image wallImage;
    private Image blueGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image scaredGhostmage;
    private Image orangeGhostImage;


    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanRightImage;
    private Image pacmanLeftImage;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block Pacman;
    
    Timer gameloop;
    char[] directions = {'U','D','R','L'};
    Random random = new Random();
    int score =0; 
    int lives = 3;
    boolean gameover = false;



    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };


    pacman(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        //ensuring Jpanel listens
        setFocusable(true);
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap();
        for(Block ghost:ghosts){
            char newDirection = directions[random.nextInt(4)];  
            ghost.updateDirection(newDirection);
        }
        gameloop = new Timer(50, this);// 20fps 1000/50
        gameloop.start();

        


    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();


        for(int r=0; r<rowCount; r++){
            for(int c=0; c<columnCount; c++){
                String row  = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = tileSize*c;
                int y = tileSize*r;

                if(tileMapChar=='X'){//wall
                    Block wall= new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);

                }
                else if(tileMapChar=='b'){
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar=='o'){
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar=='p'){
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar=='r'){
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar=='P'){
                    Pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if(tileMapChar==' '){
                    Block food = new Block(null, x+14, y+14, 4, 4); //32 == 14+4+14  move 14px hori move 14px ver then dram a 4px 
                    foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(Pacman.image, Pacman.x, Pacman.y, Pacman.width, Pacman.height, null);
        
        for(Block ghost:ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for(Block wall: walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for(Block food:foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));

        if(gameover){
            g.drawString("Game Over: "+String.valueOf(score), tileSize/2, tileSize/2);
        }
        else{
            g.drawString("x"+String.valueOf(lives)+ " Score: "+ String.valueOf(score), tileSize/2, tileSize/2);
        }
    }

    public void move(){
        Pacman.x += Pacman.velocityX;
        Pacman.y += Pacman.velocityY;

        for(Block wall:walls){
            if(collision(Pacman, wall)){
                Pacman.x -= Pacman.velocityX;
                Pacman.y -= Pacman.velocityY;
                break;
            }

        }

        for(Block ghost:ghosts){
            if(collision(ghost, Pacman)){
                lives-=1;
                if(lives==0){
                    gameover = true;
                    return;
                }
                resetPositions();
            }
            if(ghost.y==tileSize*9 && ghost.direction != 'U' && ghost.direction!= 'D'){
                ghost.updateDirection('U');
            }
            ghost.x+=ghost.velocityX;
            ghost.y+=ghost.velocityY;
            for(Block wall:walls){
                if(collision(ghost, wall) || ghost.x<=0 || ghost.x+ ghost.width>=boardWidth ){
                    ghost.x-=ghost.velocityX;
                    ghost.y -=ghost.velocityY;
                    char newDirection= directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }
        //check food eaten
        Block foodEaten = null ;
        for(Block food:foods){
            if(collision(Pacman, food)){
                foodEaten = food;
                score +=10;

            }  
        }
        foods.remove(foodEaten);

        if(foods.isEmpty()){
            loadMap();
            resetPositions();
        }
        

    }

    public boolean collision(Block a, Block b){
        return a.x<b.x+b.width &&
        a.x + a.width>b.x &&
        a.y<b.y+b.height &&
        a.y + a.height >b.y;
    }

    public void resetPositions(){
        Pacman.reset();
        Pacman.velocityX = 0;
        Pacman.velocityY=0;
        for(Block ghost: ghosts){
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // update and redraw
        move();
        repaint();
        if(gameover){
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {    
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(gameover){
            loadMap();
            resetPositions();
            lives= 3;
            score = 0;
            gameover = false;
            gameloop.start();
        }

        if(e.getKeyCode()==KeyEvent.VK_UP){
            Pacman.updateDirection('U');
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN){
            Pacman.updateDirection('D');
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT){
            Pacman.updateDirection('L');
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            Pacman.updateDirection('R');
        }
        else if(e.getKeyCode()==KeyEvent.VK_W){
            Pacman.updateDirection('U');
        }
        else if(e.getKeyCode()==KeyEvent.VK_S){
            Pacman.updateDirection('D');
        }
        else if(e.getKeyCode()==KeyEvent.VK_A){
            Pacman.updateDirection('L');
        }
        else if(e.getKeyCode()==KeyEvent.VK_D){
            Pacman.updateDirection('R');
        }

        if(Pacman.direction=='U'){
            Pacman.image = pacmanUpImage;
        }
        else if(Pacman.direction=='D'){
            Pacman.image = pacmanDownImage;
        }
        if(Pacman.direction=='L'){
            Pacman.image = pacmanLeftImage;
        }
        if(Pacman.direction=='R'){
            Pacman.image = pacmanRightImage;
        }
    }


}
