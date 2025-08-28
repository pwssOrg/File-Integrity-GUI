package org.pwss.view.screen;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ScanView extends JPanel {

    private final JButton startButton;
    private final JButton stopButton;

    public ScanView() {
        setLayout(new BorderLayout());

        // Buttons
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");

        // Inner panel for horizontal alignment
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        // Wrapper with GridBagLayout to center vertically + horizontally
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(buttonPanel, new GridBagConstraints());

        add(wrapper, BorderLayout.CENTER);
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
