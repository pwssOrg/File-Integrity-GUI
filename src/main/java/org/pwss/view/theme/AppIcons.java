package org.pwss.view.theme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * Small vector icon set used across the application.
 * <p>
 * Icons are drawn with Java2D on a 24x24 grid, so they stay sharp at any
 * display scale and automatically follow the colour of the component they are
 * painted on (and therefore the active theme).
 */
public final class AppIcons {

    /**
     * The available icon shapes.
     */
    public enum Glyph {
        HOME, SCAN, FILES, WARNING, SETTINGS, LIST, PLUS, PLAY, STOP, BACK, FOLDER, FILE, RESTART, TRASH, CHECK,
        SKIP, INFO, COPY, CLOCK
    }

    /**
     * Default icon size in pixels (before display scaling).
     */
    private static final int DEFAULT_SIZE = 16;

    /**
     * Cached, lazily loaded application logo.
     */
    private static BufferedImage logo;

    private AppIcons() {
    }

    /**
     * Creates an icon at the default size.
     *
     * @param glyph the shape to draw
     * @return the icon
     */
    public static Icon of(Glyph glyph) {
        return new GlyphIcon(glyph, DEFAULT_SIZE, null);
    }

    /**
     * Creates an icon with a fixed colour.
     *
     * @param glyph the shape to draw
     * @param size  the icon size in pixels
     * @param color the colour to draw with, or null to follow the component
     * @return the icon
     */
    public static Icon of(Glyph glyph, int size, Color color) {
        return new GlyphIcon(glyph, size, color);
    }

    /**
     * Creates a small filled status dot.
     *
     * @param color the dot colour
     * @return the icon
     */
    public static Icon dot(Color color) {
        return new DotIcon(color);
    }

    /**
     * Creates a glyph centred on a rounded square tinted with its colour, as
     * used for page, navigation and card icons.
     *
     * @param glyph the shape to draw
     * @param color the colour of the glyph and the tint, looked up on every
     *              paint so it follows theme changes
     * @param size  the size of the square in pixels
     * @return the icon
     */
    public static Icon tile(Glyph glyph, Supplier<Color> color, int size) {
        return new TileIcon(glyph, color, size);
    }

    /**
     * Creates a glyph on a tinted rounded square in a fixed colour.
     *
     * @param glyph the shape to draw
     * @param color the colour of the glyph and the tint
     * @param size  the size of the square in pixels
     * @return the icon
     */
    public static Icon tile(Glyph glyph, Color color, int size) {
        return new TileIcon(glyph, () -> color, size);
    }

    /**
     * Creates a short, rounded vertical bar, used as a coloured marker in front
     * of section headings.
     *
     * @param color the bar colour, looked up on every paint
     * @return the icon
     */
    public static Icon bar(Supplier<Color> color) {
        return new BarIcon(color);
    }

    /**
     * Creates an icon showing the application logo.
     *
     * @param size the icon size in pixels
     * @return the icon
     */
    public static Icon logo(int size) {
        return new LogoIcon(size);
    }

    private static synchronized BufferedImage loadLogo() {
        if (logo == null) {
            try {
                URL url = AppIcons.class.getResource("/app-icon.png");
                logo = url != null ? ImageIO.read(url) : null;
            } catch (IOException e) {
                logo = null;
            }
            if (logo == null) {
                logo = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            }
        }
        return logo;
    }

    /**
     * Scales an image down in halving steps, which keeps small results smooth.
     */
    private static BufferedImage downscale(BufferedImage source, int target) {
        BufferedImage current = source;
        int side = Math.max(source.getWidth(), source.getHeight());
        while (side > target) {
            side = Math.max(target, side / 2);
            BufferedImage next = new BufferedImage(side, side, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = next.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(current, 0, 0, side, side, null);
            g2.dispose();
            current = next;
        }
        return current;
    }

    private static Graphics2D prepare(Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }

    /**
     * A line icon drawn on a 24x24 grid.
     */
    private static final class GlyphIcon implements Icon {
        private final Glyph glyph;
        private final int size;
        private final Color color;

        private GlyphIcon(Glyph glyph, int size, Color color) {
            this.glyph = glyph;
            this.size = size;
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = prepare(g, x, y);
            try {
                g2.scale(size / 24.0, size / 24.0);
                g2.setColor(resolveColor(c));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                paintGlyph(g2);
            } finally {
                g2.dispose();
            }
        }

        private Color resolveColor(Component c) {
            if (color != null) {
                return color;
            }
            if (c == null) {
                return UIManager.getColor("Label.foreground");
            }
            if (!c.isEnabled()) {
                return UIManager.getColor("Label.disabledForeground");
            }
            return c.getForeground();
        }

        private void paintGlyph(Graphics2D g) {
            switch (glyph) {
                case HOME -> {
                    g.draw(new RoundRectangle2D.Double(3, 3, 7, 9, 2, 2));
                    g.draw(new RoundRectangle2D.Double(14, 3, 7, 5, 2, 2));
                    g.draw(new RoundRectangle2D.Double(14, 12, 7, 9, 2, 2));
                    g.draw(new RoundRectangle2D.Double(3, 16, 7, 5, 2, 2));
                }
                case SCAN -> {
                    g.draw(new Ellipse2D.Double(3.5, 3.5, 14, 14));
                    g.draw(new Line2D.Double(15.6, 15.6, 20.5, 20.5));
                }
                case FILES, FOLDER -> {
                    Path2D p = new Path2D.Double();
                    p.moveTo(3, 5);
                    p.lineTo(9, 5);
                    p.lineTo(11.5, 8);
                    p.lineTo(21, 8);
                    p.lineTo(21, 19);
                    p.lineTo(3, 19);
                    p.closePath();
                    g.draw(p);
                }
                case FILE -> {
                    Path2D p = new Path2D.Double();
                    p.moveTo(6, 3);
                    p.lineTo(14, 3);
                    p.lineTo(19, 8);
                    p.lineTo(19, 21);
                    p.lineTo(6, 21);
                    p.closePath();
                    g.draw(p);
                    Path2D fold = new Path2D.Double();
                    fold.moveTo(14, 3);
                    fold.lineTo(14, 8);
                    fold.lineTo(19, 8);
                    g.draw(fold);
                }
                case WARNING -> {
                    Path2D p = new Path2D.Double();
                    p.moveTo(12, 3.5);
                    p.lineTo(21.5, 20);
                    p.lineTo(2.5, 20);
                    p.closePath();
                    g.draw(p);
                    g.draw(new Line2D.Double(12, 10, 12, 14));
                    g.fill(new Ellipse2D.Double(10.9, 16, 2.2, 2.2));
                }
                case SETTINGS -> {
                    double[][] rows = { { 6, 15 }, { 12, 8 }, { 18, 14 } };
                    for (double[] row : rows) {
                        double y = row[0];
                        double knob = row[1];
                        g.draw(new Line2D.Double(3, y, knob - 2.5, y));
                        g.draw(new Ellipse2D.Double(knob - 2.5, y - 2.5, 5, 5));
                        g.draw(new Line2D.Double(knob + 2.5, y, 21, y));
                    }
                }
                case LIST -> {
                    for (double y : new double[] { 6, 12, 18 }) {
                        g.fill(new Ellipse2D.Double(3, y - 1.2, 2.4, 2.4));
                        g.draw(new Line2D.Double(9, y, 21, y));
                    }
                }
                case PLUS -> {
                    g.draw(new Line2D.Double(12, 5, 12, 19));
                    g.draw(new Line2D.Double(5, 12, 19, 12));
                }
                case PLAY -> {
                    Path2D p = new Path2D.Double();
                    p.moveTo(7, 4.5);
                    p.lineTo(19, 12);
                    p.lineTo(7, 19.5);
                    p.closePath();
                    g.fill(p);
                    g.draw(p);
                }
                case STOP -> {
                    RoundRectangle2D r = new RoundRectangle2D.Double(6, 6, 12, 12, 3, 3);
                    g.fill(r);
                    g.draw(r);
                }
                case BACK -> {
                    g.draw(new Line2D.Double(19, 12, 5, 12));
                    Path2D p = new Path2D.Double();
                    p.moveTo(12, 19);
                    p.lineTo(5, 12);
                    p.lineTo(12, 5);
                    g.draw(p);
                }
                case RESTART -> {
                    g.draw(new Arc2D.Double(4, 4, 16, 16, 20, 290, Arc2D.OPEN));
                    Path2D p = new Path2D.Double();
                    p.moveTo(20.5, 3.5);
                    p.lineTo(20.5, 9.5);
                    p.lineTo(14.5, 9.5);
                    g.draw(p);
                }
                case TRASH -> {
                    g.draw(new Line2D.Double(4, 7, 20, 7));
                    Path2D lid = new Path2D.Double();
                    lid.moveTo(9, 7);
                    lid.lineTo(9, 4);
                    lid.lineTo(15, 4);
                    lid.lineTo(15, 7);
                    g.draw(lid);
                    Path2D can = new Path2D.Double();
                    can.moveTo(6, 7);
                    can.lineTo(7, 20);
                    can.lineTo(17, 20);
                    can.lineTo(18, 7);
                    g.draw(can);
                }
                case CHECK -> {
                    g.draw(new Ellipse2D.Double(3, 3, 18, 18));
                    Path2D p = new Path2D.Double();
                    p.moveTo(8, 12.5);
                    p.lineTo(11, 15.5);
                    p.lineTo(16.5, 9);
                    g.draw(p);
                }
                case SKIP -> {
                    g.draw(new Ellipse2D.Double(3, 3, 18, 18));
                    g.draw(new Line2D.Double(8, 12, 16, 12));
                }
                case INFO -> {
                    g.draw(new Ellipse2D.Double(3, 3, 18, 18));
                    g.draw(new Line2D.Double(12, 11.5, 12, 16.5));
                    g.fill(new Ellipse2D.Double(10.9, 6.9, 2.2, 2.2));
                }
                case COPY -> {
                    g.draw(new RoundRectangle2D.Double(8, 8, 13, 13, 3, 3));
                    Path2D back = new Path2D.Double();
                    back.moveTo(5, 16);
                    back.lineTo(4.5, 16);
                    back.quadTo(3, 16, 3, 14.5);
                    back.lineTo(3, 4.5);
                    back.quadTo(3, 3, 4.5, 3);
                    back.lineTo(14.5, 3);
                    back.quadTo(16, 3, 16, 4.5);
                    back.lineTo(16, 5);
                    g.draw(back);
                }
                case CLOCK -> {
                    g.draw(new Ellipse2D.Double(3, 3, 18, 18));
                    Path2D hands = new Path2D.Double();
                    hands.moveTo(12, 7);
                    hands.lineTo(12, 12);
                    hands.lineTo(15.5, 14);
                    g.draw(hands);
                }
            }
        }
    }

    /**
     * A small filled circle used as a status indicator.
     */
    private static final class DotIcon implements Icon {
        private static final int SIZE = 10;
        private final Color color;

        private DotIcon(Color color) {
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = prepare(g, x, y);
            try {
                g2.setColor(color);
                g2.fill(new Ellipse2D.Double(1, 1, SIZE - 2, SIZE - 2));
            } finally {
                g2.dispose();
            }
        }
    }

    /**
     * A glyph centred on a rounded, tinted square.
     */
    private static final class TileIcon implements Icon {
        private final Glyph glyph;
        private final Supplier<Color> color;
        private final int size;

        private TileIcon(Glyph glyph, Supplier<Color> color, int size) {
            this.glyph = glyph;
            this.color = color;
            this.size = size;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color tint = color.get();
            Graphics2D g2 = prepare(g, x, y);
            try {
                g2.setColor(Palette.tint(tint, 0.22));
                g2.fill(new RoundRectangle2D.Double(0, 0, size, size, size * 0.4, size * 0.4));
            } finally {
                g2.dispose();
            }
            int glyphSize = Math.round(size * 0.55f);
            int offset = (size - glyphSize) / 2;
            new GlyphIcon(glyph, glyphSize, tint).paintIcon(c, g, x + offset, y + offset);
        }
    }

    /**
     * A short, rounded vertical bar.
     */
    private static final class BarIcon implements Icon {
        private final Supplier<Color> color;

        private BarIcon(Supplier<Color> color) {
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return 4;
        }

        @Override
        public int getIconHeight() {
            return 18;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = prepare(g, x, y);
            try {
                g2.setColor(color.get());
                g2.fill(new RoundRectangle2D.Double(0, 0, 4, 18, 4, 4));
            } finally {
                g2.dispose();
            }
        }
    }

    /**
     * The application logo, cropped to its rounded tile.
     */
    private static final class LogoIcon implements Icon {
        /**
         * The tile inside the 1024px source image (the rest is a soft vignette).
         */
        private static final double CROP_INSET = 64 / 1024.0;
        private final int size;
        private final Map<Integer, Image> scaled = new HashMap<>();

        private LogoIcon(int size) {
            this.size = size;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = prepare(g, x, y);
            try {
                BufferedImage source = loadLogo();
                int inset = (int) Math.round(source.getWidth() * CROP_INSET);
                int side = Math.max(1, source.getWidth() - 2 * inset);
                BufferedImage cropped = source.getWidth() > 2 * inset && source.getHeight() > 2 * inset
                        ? source.getSubimage(inset, inset, side, Math.max(1, source.getHeight() - 2 * inset))
                        : source;
                int target = Math.max(1, (int) Math.ceil(size * g2.getTransform().getScaleX()));
                Image image = scaled.computeIfAbsent(target, key -> downscale(cropped, key));
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                double arc = size * 0.42;
                g2.clip(new RoundRectangle2D.Double(0, 0, size, size, arc, arc));
                g2.drawImage(image, 0, 0, size, size, null);
            } finally {
                g2.dispose();
            }
        }
    }
}
