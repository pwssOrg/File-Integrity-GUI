package org.pwss.view.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.pwss.view.theme.Palette;
import org.pwss.view.theme.UiStyle;

/**
 * A small rounded badge with a tinted background and coloured text.
 */
public class Pill extends JLabel {

    private Color color;

    /**
     * Creates a pill.
     *
     * @param text  the text
     * @param color the colour of the text, which also tints the background
     * @param icon  an icon shown before the text, or null
     */
    public Pill(String text, Color color, Icon icon) {
        super(text, icon, SwingConstants.CENTER);
        setFont(UiStyle.font("small.font").deriveFont(Font.BOLD));
        setIconTextGap(5);
        setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        setColor(color);
    }

    /**
     * Changes the colour of the pill.
     *
     * @param color the new colour
     */
    public void setColor(Color color) {
        this.color = color;
        setForeground(color);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Palette.tint(color, 0.18));
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
        } finally {
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
