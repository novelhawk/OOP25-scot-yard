package it.unibo.scotyard.view.window;

import javax.swing.JPanel;

public interface Window {
    void setBody(JPanel panel);

    int getWidth();

    int getHeight();

    void display();
}
