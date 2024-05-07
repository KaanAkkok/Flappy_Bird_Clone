import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int BoardWidth = 360;
        int BoardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird"); 
        frame.setSize(BoardWidth, BoardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Flappy_bird flappyBird = new Flappy_bird();

        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}