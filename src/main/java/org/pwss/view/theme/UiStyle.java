package org.pwss.view.theme;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

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
        JPanel page = new JPanel(new BorderLayout());
        fillPage(page, title, subtitle, content, leading, actions);
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
        page.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(font("h2.font"));
        JLabel subtitleLabel = mutedLabel(subtitle);

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(titleLabel);
        titles.add(Box.createVerticalStrut(2));
        titles.add(subtitleLabel);

        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setBorder(BorderFactory.createEmptyBorder(PAGE_PADDING - 4, PAGE_PADDING, 6, PAGE_PADDING));
        if (leading != null) {
            JPanel leadingHolder = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 4));
            leadingHolder.add(leading);
            header.add(leadingHolder, BorderLayout.LINE_START);
        }
        header.add(titles, BorderLayout.CENTER);
        if (actions.length > 0) {
            JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.TRAILING, 8, 4));
            for (JComponent action : actions) {
                actionBar.add(action);
            }
            header.add(actionBar, BorderLayout.LINE_END);
        }

        // The designer panels already carry a 10px margin of their own
        JPanel body = new JPanel(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(0, PAGE_PADDING - 10, PAGE_PADDING - 10, PAGE_PADDING - 10));
        content.setVisible(true);
        body.add(content, BorderLayout.CENTER);

        page.add(header, BorderLayout.PAGE_START);
        page.add(body, BorderLayout.CENTER);
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
                }
            } else if (child instanceof JSplitPane splitPane) {
                splitPane.setBorder(null);
                splitPane.setContinuousLayout(true);
                if (splitPane.getResizeWeight() == 0) {
                    splitPane.setResizeWeight(0.5);
                }
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
