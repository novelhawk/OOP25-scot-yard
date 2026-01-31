package it.unibo.scotyard.view.dialogs;

import it.unibo.scotyard.commons.patterns.CommonCostants;
import it.unibo.scotyard.commons.patterns.MagicNumbers;
import it.unibo.scotyard.commons.patterns.ScotColors;
import it.unibo.scotyard.commons.patterns.ScotFont;
import it.unibo.scotyard.model.map.NodeId;
import it.unibo.scotyard.model.map.TransportType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Modal dialog for selecting transport type when multiple options are
 * available.
 * This dialog presents colored buttons for each available transport option,
 * styled according to the game's color
 * scheme.
 */
public final class TransportSelectionDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private TransportType selectedTransport;

    /**
     * Creates a transport selection dialog.
     *
     * @param parent         the parent frame (can be null)
     * @param nodeId         the destination node ID
     * @param transportTypes the available transport types
     */
    public TransportSelectionDialog(final Frame parent, final NodeId nodeId, final List<TransportType> transportTypes) {
        super(parent, CommonCostants.SELECTION_TRANSPORT_JDIALOG, true);

        selectedTransport = null;

        setupDialog();
        buildContent(nodeId, transportTypes);
        pack();
        setLocationRelativeTo(parent);
    }

    /** Setup dialog properties. */
    private void setupDialog() {
        setResizable(false);
        getContentPane().setBackground(ScotColors.BACKGROUND_COLOR);
    }

    /**
     * Build dialog content.
     *
     * @param nodeId         the destination node ID
     * @param transportTypes the available transport types
     */
    private void buildContent(final NodeId nodeId, final List<TransportType> transportTypes) {
        final JPanel mainPanel = new JPanel(new BorderLayout(MagicNumbers.GAP_10, MagicNumbers.GAP_10));
        mainPanel.setBackground(ScotColors.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                MagicNumbers.GAP_20, MagicNumbers.GAP_20, MagicNumbers.GAP_20, MagicNumbers.GAP_20));

        // title
        final JLabel titleLabel = new JLabel(CommonCostants.SELECTION_TRANSPORT_TITLE + nodeId.id());
        titleLabel.setForeground(ScotColors.ACCENT_COLOR);
        titleLabel.setFont(ScotFont.TEXT_FONT_20);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // buttons panel
        final JPanel buttonsPanel =
                new JPanel(new FlowLayout(FlowLayout.CENTER, MagicNumbers.GAP_10, MagicNumbers.GAP_10));
        buttonsPanel.setBackground(ScotColors.BACKGROUND_COLOR);

        for (final TransportType transport : transportTypes) {
            final JButton button = createTransportButton(transport);
            buttonsPanel.add(button);
        }

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * Creates a styled button for a transport type.
     *
     * @param transport the transport type
     * @return the created button
     */
    private JButton createTransportButton(final TransportType transport) {
        final JButton button = new JButton(transport.toString());
        button.setPreferredSize(new Dimension(MagicNumbers.WIDTH_120, MagicNumbers.HEIGHT_50));
        button.setFont(ScotFont.TEXT_FONT_16);

        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(getTransportColor(transport), 3));
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                button.setBackground(ScotColors.MOUSE_HOVER);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        // Click handler
        button.addActionListener(e -> {
            selectedTransport = transport;
            dispose();
        });

        return button;
    }

    /**
     * Gets the color for a transport type.
     *
     * @param transport the transport type
     * @return the color
     */
    private Color getTransportColor(final TransportType transport) {
        return switch (transport) {
            case TAXI -> ScotColors.TAXI_COLOR;
            case BUS -> ScotColors.BUS_COLOR;
            case UNDERGROUND -> ScotColors.UNDERGROUND_COLOR;
            case FERRY -> ScotColors.FERRY_COLOR;
        };
    }

    /**
     * Gets the selected transport type. Returns null if the dialog was cancelled.
     *
     * @return the selected transport, or null
     */
    public TransportType getSelectedTransport() {
        return selectedTransport;
    }

    /**
     * Shows the dialog and waits for user selection. This is a convenience method
     * that calls setVisible(true).
     *
     * @return the selected transport, or null if cancelled
     */
    public TransportType showAndWait() {
        setVisible(true);
        return selectedTransport;
    }
}
