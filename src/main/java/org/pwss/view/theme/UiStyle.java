package org.pwss.view.theme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Insets;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import org.pwss.view.component.Card;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * Shared styling helpers for the screens.
 * <p>
 * The screens are laid out with the IntelliJ GUI designer; these helpers add
 * the visual polish on top without touching the generated layout code.
 */
public final class UiStyle {

    /**
     * Style class for the main action of a screen (filled accent button).
     */
    public static final String PRIMARY = "primary";

    /**
     * Style class for destructive or interrupting actions (filled red button).
     */
    public static final String DANGER = "danger";

    /**
     * Style class for secondary text.
     */
    public static final String MUTED = "muted";

    /**
     * Outer padding of a page, in pixels.
     */
    private static final int PAGE_PADDING = 24;

    private UiStyle() {
    }

    /**
     * Assigns a FlatLaf style class to a component.
     *
     * @param component  the component to style
     * @param styleClass the style class, or null to clear it
     */
    public static void styleClass(JComponent component, String styleClass) {
        component.putClientProperty(FlatClientProperties.STYLE_CLASS, styleClass);
    }

    /**
     * Looks up a named font from the look and feel (for example "h2.font").
     *
     * @param key the UI defaults key
     * @return the font, or the default label font if the key is unknown
     */
    public static Font font(String key) {
        Font font = UIManager.getFont(key);
        return font != null ? font : UIManager.getFont("Label.font");
    }

    /**
     * Creates a label rendered as secondary text.
     *
     * @param text the label text
     * @return the label
     */
    public static JLabel mutedLabel(String text) {
        JLabel label = new JLabel(text);
        styleClass(label, MUTED);
        return label;
    }

    /**
     * Wraps content in a page: a header with a title, a short description and
     * optional actions, followed by the content itself.
     *
     * @param title    the page title
     * @param subtitle a one-line description shown under the title
     * @param content  the page content
     * @param leading  a component shown before the title (such as a back
     *                 button), or null
     * @param actions  components shown at the trailing edge of the header
     * @return the page panel
     */
    public static JPanel page(String title, String subtitle, JComponent content, JComponent leading,
            JComponent... actions) {
        return page(title, subtitle, content, leading, null, Palette::accent, actions);
    }

    /**
     * Same as {@link #page(String, String, JComponent, JComponent, JComponent...)},
     * with a coloured icon tile in the header.
     *
     * @param title    the page title
     * @param subtitle a one-line description shown under the title
     * @param content  the page content
     * @param leading  a component shown before the title, or null
     * @param glyph    the page icon, or null for none
     * @param color    the page colour, tinting the icon and the header
     * @param actions  components shown at the trailing edge of the header
     * @return the page panel
     */
    public static JPanel page(String title, String subtitle, JComponent content, JComponent leading,
            AppIcons.Glyph glyph, Supplier<Color> color, JComponent... actions) {
        JPanel page = new JPanel(new BorderLayout());
        fillPage(page, title, subtitle, content, leading, glyph, color, actions);
        return page;
    }

    /**
     * Same as {@link #page}, but fills an existing (empty) panel.
     *
     * @param page     the panel to fill
     * @param title    the page title
     * @param subtitle a one-line description shown under the title
     * @param content  the page content
     * @param leading  a component shown before the title, or null
     * @param actions  components shown at the trailing edge of the header
     */
    public static void fillPage(JPanel page, String title, String subtitle, JComponent content, JComponent leading,
            JComponent... actions) {
        fillPage(page, title, subtitle, content, leading, null, Palette::accent, actions);
    }

    /**
     * Same as {@link #fillPage(JPanel, String, String, JComponent, JComponent, JComponent...)},
     * with a coloured icon tile in the header.
     *
     * @param page     the panel to fill
     * @param title    the page title
     * @param subtitle a one-line description shown under the title
     * @param content  the page content
     * @param leading  a component shown before the title, or null
     * @param glyph    the page icon, or null for none
     * @param color    the page colour, tinting the icon and the header
     * @param actions  components shown at the trailing edge of the header
     */
    public static void fillPage(JPanel page, String title, String subtitle, JComponent content, JComponent leading,
            AppIcons.Glyph glyph, Supplier<Color> color, JComponent... actions) {
        page.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(font("h2.font"));
        JLabel subtitleLabel = mutedLabel(subtitle);

        JPanel titles = transparent(null);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(titleLabel);
        titles.add(Box.createVerticalStrut(2));
        titles.add(subtitleLabel);

        // The header is a card with a soft gradient in the page colour
        Card hero = Card.gradient(new BorderLayout(14, 0), color, Palette::accent);
        hero.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JPanel lead = transparent(new FlowLayout(FlowLayout.LEADING, 0, 0));
        if (leading != null) {
            lead.add(leading);
        }
        if (glyph != null) {
            if (leading != null) {
                lead.add(Box.createHorizontalStrut(12));
            }
            lead.add(new JLabel(AppIcons.tile(glyph, color, 44)));
        }
        if (lead.getComponentCount() > 0) {
            hero.add(centered(lead, false), BorderLayout.LINE_START);
        }
        hero.add(centered(titles, true), BorderLayout.CENTER);
        if (actions.length > 0) {
            JPanel actionBar = transparent(new FlowLayout(FlowLayout.TRAILING, 8, 0));
            for (JComponent action : actions) {
                actionBar.add(action);
            }
            hero.add(centered(actionBar, false), BorderLayout.LINE_END);
        }

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(PAGE_PADDING - 8, PAGE_PADDING, 8, PAGE_PADDING));
        header.add(hero, BorderLayout.CENTER);

        // The designer panels already carry a 10px margin of their own
        JPanel body = new JPanel(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(0, PAGE_PADDING - 10, PAGE_PADDING - 10, PAGE_PADDING - 10));
        content.setVisible(true);
        body.add(content, BorderLayout.CENTER);

        page.add(header, BorderLayout.PAGE_START);
        page.add(body, BorderLayout.CENTER);
    }

    private static JPanel transparent(LayoutManager layout) {
        JPanel panel = layout != null ? new JPanel(layout) : new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Centres a component vertically, optionally stretching it horizontally.
     */
    private static JPanel centered(JComponent component, boolean fillWidth) {
        JPanel holder = transparent(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        if (fillWidth) {
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
        }
        holder.add(component, constraints);
        return holder;
    }

    /**
     * Applies the common finishing touches to every component below the given
     * root: section headings, split panes, tables and read-only text areas.
     *
     * @param root the root of the component tree to polish
     */
    public static void polish(Container root) {
        for (Component child : root.getComponents()) {
            if (child instanceof JLabel label) {
                Font font = label.getFont();
                if (font != null && font.isBold() && font.getSize() >= 14) {
                    label.setFont(font("h3.font"));
                    label.setBorder(BorderFactory.createEmptyBorder(4, 2, 6, 0));
                    if (label.getIcon() == null) {
                        label.setIcon(AppIcons.bar(Palette::accent));
                        label.setIconTextGap(10);
                    }
                }
            } else if (child instanceof JSplitPane splitPane) {
                splitPane.setBorder(null);
                splitPane.setContinuousLayout(true);
                if (splitPane.getResizeWeight() == 0) {
                    splitPane.setResizeWeight(0.5);
                }
            } else if (child instanceof JScrollPane scrollPane) {
                styleScrollPane(scrollPane);
            } else if (child instanceof JTable table) {
                TableStyle.install(table);
            } else if (child instanceof JTextComponent text && !(child instanceof javax.swing.JTextField)) {
                text.setMargin(new Insets(12, 14, 12, 14));
            }
            if (child instanceof Container container) {
                polish(container);
            }
        }
    }

    /**
     * Gives a scroll pane the look of a card: larger rounded corners and a
     * border that blends into the card surface.
     *
     * @param scrollPane the scroll pane to style
     */
    public static void styleScrollPane(JScrollPane scrollPane) {
        Color border = Palette.mix(UIManager.getColor("Component.borderColor"), Palette.surface(), 0.55);
        scrollPane.putClientProperty(FlatClientProperties.STYLE, Map.of("arc", 16, "borderColor", border));
    }

    /**
     * Finds the first label below the given root showing exactly the given text.
     *
     * @param root the root of the component tree to search
     * @param text the label text to look for
     * @return the label, if present
     */
    public static Optional<JLabel> findLabel(Container root, String text) {
        for (Component child : root.getComponents()) {
            if (child instanceof JLabel label && text.equals(label.getText())) {
                return Optional.of(label);
            }
            if (child instanceof Container container) {
                Optional<JLabel> found = findLabel(container, text);
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds the first component of the given type below the given root.
     *
     * @param root the root of the component tree to search
     * @param type the component type to look for
     * @param <T>  the component type
     * @return the component, if present
     */
    public static <T extends Component> Optional<T> find(Container root, Class<T> type) {
        for (Component child : root.getComponents()) {
            if (type.isInstance(child)) {
                return Optional.of(type.cast(child));
            }
            if (child instanceof Container container) {
                Optional<T> found = find(container, type);
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds the first component of the given type below the given root that
     * matches a condition.
     *
     * @param root  the root of the component tree to search
     * @param type  the component type to look for
     * @param match the condition the component must meet
     * @param <T>   the component type
     * @return the component, if present
     */
    public static <T extends Component> Optional<T> find(Container root, Class<T> type, Predicate<T> match) {
        for (Component child : root.getComponents()) {
            if (type.isInstance(child) && match.test(type.cast(child))) {
                return Optional.of(type.cast(child));
            }
            if (child instanceof Container container) {
                Optional<T> found = find(container, type, match);
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Gives a table column a fixed width, for columns that only hold an action
     * button.
     *
     * @param table      the table
     * @param columnName the column identifier
     * @param width      the width in pixels
     */
    public static void fixColumnWidth(JTable table, String columnName, int width) {
        TableColumn column = table.getColumn(columnName);
        column.setMinWidth(width);
        column.setMaxWidth(width);
        column.setPreferredWidth(width);
    }
}
