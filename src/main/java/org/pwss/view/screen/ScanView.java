package org.pwss.view.screen;

import javax.swing.*;
import java.awt.*;

public class ScanView extends JFrame {

    private final JButton startButton;
    private final JButton stopButton;

    public ScanView() {
        setTitle("Scan View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        // Initialize buttons
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");

        // Layout
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        panel.add(startButton);
        panel.add(stopButton);

        add(panel);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Scan success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Scan error", JOptionPane.ERROR_MESSAGE);
    }
}
