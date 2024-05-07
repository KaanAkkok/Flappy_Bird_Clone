import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Flappy_bird extends JPanel implements ActionListener, KeyListener {
    int BoardWidth = 360;
    int BoardHeight = 640;

    //images
    Image BackgroundImg;
    Image BirdImg;
    Image TopPipeImg;
    Image BottomPipeImg;

    //bird
    int birdX = BoardWidth/8;
    int birdY = BoardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    //pipe
    int pipeX = BoardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    //game logic
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1; 

    Timer gameLoop;
    Timer pipesTimer;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    boolean gameOver = false;

    double score = 0;

    public class Bird {
    
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;
    
        public Bird(Image img) {
            this.img = img;
        }
    
    }

    public class Pipe {
        int x = pipeX;
        int y = pipeY;
        int Width = pipeWidth;
        int Height = pipeHeight;
        Image img;
        boolean passed = false;

        public Pipe(Image img) {
            this.img = img;
        }
    }

    public Flappy_bird() {
        setPreferredSize(new Dimension(BoardWidth, BoardHeight));
        setBackground(Color.green);
        setFocusable(true);
        addKeyListener(this);

        //load images
        BackgroundImg = new ImageIcon(getClass().getResource("./images/flappybirdbg.png")).getImage();
        BirdImg = new ImageIcon(getClass().getResource("./images/flappybird.png")).getImage();
        TopPipeImg = new ImageIcon(getClass().getResource("./images/toppipe.png")).getImage();
        BottomPipeImg = new ImageIcon(getClass().getResource("./images/bottompipe.png")).getImage();

        //create bird
        bird = new Bird(BirdImg);
        pipes = new ArrayList<Pipe>();

        //pipes timer
        pipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        pipesTimer.start();

        //game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openSpace = BoardHeight/4;

        Pipe topPipe = new Pipe(TopPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(BottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Draw(g);
    }

    public void Draw(Graphics g) {
        //bg
        g.drawImage(BackgroundImg, 0, 0, BoardWidth, BoardHeight, null);
        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        //pipes
        for(int i=0; i<pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.Width, pipe.Height, null);
        }
        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver) {
            g.drawString("Game over: " + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);
        //pipes
        for(int i=0; i<pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.Width) {
                pipe.passed = true;
                score += 0.5;
            }

            if(collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > BoardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            pipesTimer.stop();
            gameLoop.stop();
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.Width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.Height &&
               a.y + a.height > b.y;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if(gameOver) {
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                pipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}