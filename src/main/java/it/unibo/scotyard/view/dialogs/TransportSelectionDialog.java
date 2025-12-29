package it.unibo.scotyard.view.dialogs;

import it.unibo.scotyard.model.map.TransportType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
 * Modal dialog for selecting transport type when multiple options are available.
 *
 * <p>This dialog presents colored buttons for each available transport option, styled according to the game's color
 * scheme.
 */
public final class TransportSelectionDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    // colori tema
    private static final Color BACKGROUND_COLOR = new Color(62, 39, 35);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color ACCENT_COLOR = new Color(200, 160, 120);

    // colore traspoorti
    private static final Color TAXI_COLOR = new Color(255, 255, 85);
    private static final Color BUS_COLOR = new Color(58, 132, 36);
    private static final Color UNDERGROUND_COLOR = new Color(200, 43, 29);
    private static final Color FERRY_COLOR = new Color(128, 128, 128);

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    private TransportType selectedTransport;

    /**
     * Creates a transport selection dialog.
     *
     * @param parent the parent frame (can be null)
     * @param nodeId the destination node ID
     * @param transportTypes the available transport types
     */
    public TransportSelectionDialog(final Frame parent, final int nodeId, final List<TransportType> transportTypes) {
        super(parent, "Select Transport", true);

        selectedTransport = null;

        setupDialog();
        buildContent(nodeId, transportTypes);
        pack();
        setLocationRelativeTo(parent);
    }

    /** Setup dialog properties. */
    private void setupDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    /**
     * Build dialog content.
     *
     * @param nodeId the destination node ID
     * @param transportTypes the available transport types
     */
    private void buildContent(final int nodeId, final List<TransportType> transportTypes) {
        final JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // title
        final JLabel titleLabel = new JLabel("Choose transport to node " + nodeId);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // buttons panel
        final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.setBackground(BACKGROUND_COLOR);

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
        button.setPreferredSize(new Dimension(120, 50));
        button.setFont(BUTTON_FONT);

        button.setBackground(Color.WHITE);
        button.setForeground(BACKGROUND_COLOR);
        button.setBorder(BorderFactory.createLineBorder(getTransportColor(transport), 3));
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                button.setBackground(new Color(245, 245, 245));
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
        switch (transport) {
            case TAXI:
                return TAXI_COLOR;
            case BUS:
                return BUS_COLOR;
            case UNDERGROUND:
                return UNDERGROUND_COLOR;
            case FERRY:
                return FERRY_COLOR;
            default:
                return ACCENT_COLOR;
        }
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
     * Shows the dialog and waits for user selection. This is a convenience method that calls setVisible(true).
     *
     * @return the selected transport, or null if cancelled
     */
    public TransportType showAndWait() {
        setVisible(true);
        return selectedTransport;
    }
}
