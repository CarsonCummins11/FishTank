package ff;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class App {
    public App() throws InterruptedException {
        World w = new World();
        JFrame f = new JFrame();
        f.setLayout(new GridLayout());
        f.setSize(600, 600);
        f.add(w);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.addMouseMotionListener(w);
        while (true) {
            w.step();
            f.repaint();
            synchronized (this) {
                this.wait(10);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new App();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

}
