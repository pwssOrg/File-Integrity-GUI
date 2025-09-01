package org.pwss.view.screen;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class HomeScreen extends BaseScreen {
    private final JButton startButton;
    private final JButton stopButton;

    public HomeScreen() {
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

    @Override
    protected String getScreenName() {
        return "Home";
    }

    /**
     * Get the Start button.
     *
     * @return The Start JButton.
     */
    public JButton getStartButton() {
        return startButton;
    }

    /**
     * Get the Stop button.
     * @return The Stop JButton.
     */
    public JButton getStopButton() {
        return stopButton;
    }
}
