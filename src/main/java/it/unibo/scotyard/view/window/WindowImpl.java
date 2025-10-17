package it.unibo.scotyard.view.window;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowImpl extends JFrame implements Window {

    public static final String WINDOW_TITLE = "Scotland Yard";
    public static final int PROPORTION = 2;

    private final JFrame frame;
    private final int screenWidth;
    private final int screenHeight;

    public WindowImpl() {
        this.frame = new JFrame(WindowImpl.WINDOW_TITLE);

        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = (int) screen.getWidth() / WindowImpl.PROPORTION;
        this.screenHeight = (int) screen.getHeight() / WindowImpl.PROPORTION;
    }

    @Override
    public int getWidth() {
        return this.screenWidth;
    }

    @Override
    public int getHeight() {
        return this.screenHeight;
    }

    @Override
    public void setBody(JPanel panel) {
        this.frame.setContentPane(panel);
    }

    @Override
    public void display() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(this.getWidth(), this.getHeight());

        this.setLocationByPlatform(true);

        this.setVisible(true);
    }

}
