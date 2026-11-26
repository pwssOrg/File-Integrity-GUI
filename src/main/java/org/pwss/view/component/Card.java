package org.pwss.view.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Supplier;
import javax.swing.JPanel;
import org.pwss.view.theme.Palette;

/**
 * A rounded panel with a solid or diagonal gradient fill and an optional
 * coloured strip along its leading edge: the basic surface of the UI.
 * <p>
 * Colours are looked up each time the card is painted, so cards follow theme
 * changes without being rebuilt.
 */
public class Card extends JPanel {

    /**
     * The default corner radius.
     */
    public static final int ARC = 16;

    private final Supplier<Color> from;
    private final Supplier<Color> to;
    private Supplier<Color> strip;
    private int arc = ARC;
    private boolean fixedHeight;

    /**
     * Creates a card filled with the card surface colour.
     *
     * @param layout the layout manager, or null for none
     */
    public Card(LayoutManager layout) {
        this(layout, Palette::surface, null);
    }

    /**
     * Creates a card with a custom fill.
     *
     * @param layout the layout manager, or null for none
     * @param from   the fill colour, or the gradient start (top left)
     * @param to     the gradient end (bottom right), or null for a solid fill
     */
    public Card(LayoutManager layout, Supplier<Color> from, Supplier<Color> to) {
        super(layout);
        this.from = from;
        this.to = to;
        setOpaque(false);
    }

    /**
     * Creates a card with a gradient tinted by the given colours, as used for
     * headers.
     *
     * @param layout the layout manager, or null for none
     * @param start  the colour tinting the top left
     * @param end    the colour tinting the bottom right
     * @return the card
     */
    public static Card gradient(LayoutManager layout, Supplier<Color> start, Supplier<Color> end) {
        return new Card(layout,
                () -> Palette.mix(start.get(), Palette.surface(), 0.20),
                () -> Palette.mix(end.get(), Palette.surface(), 0.08));
    }

    /**
     * Sets the corner radius.
     *
     * @param arc the corner diameter in pixels
     * @return this card
     */
    public Card arc(int arc) {
        this.arc = arc;
        return this;
    }

    /**
     * Paints a coloured strip along the leading edge.
     *
     * @param strip the strip colour, or null for none
     * @return this card
     */
    public Card strip(Supplier<Color> strip) {
        this.strip = strip;
        return this;
    }

    /**
     * Keeps the card at its preferred height inside a vertical box layout,
     * instead of letting it stretch.
     *
     * @return this card
     */
    public Card fixedHeight() {
        this.fixedHeight = true;
        return this;
    }

    @Override
    public Dimension getMaximumSize() {
        return fixedHeight ? new Dimension(Integer.MAX_VALUE, getPreferredSize().height) : super.getMaximumSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            RoundRectangle2D shape = new RoundRectangle2D.Double(0, 0, w, h, arc, arc);
            g2.setPaint(to == null ? from.get() : new GradientPaint(0, 0, from.get(), w, h, to.get()));
            g2.fill(shape);
            if (strip != null) {
                g2.clip(shape);
                g2.setColor(strip.get());
                g2.fillRect(0, 0, 4, h);
            }
        } finally {
            g2.dispose();
        }
    }
}
