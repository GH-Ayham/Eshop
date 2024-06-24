package bib.local.ui.gui;

import bib.local.domain.EShop;
import bib.local.entities.Kunde;
import bib.local.entities.Mitarbeiter;
import bib.local.ui.gui.panels.KundeMenuPanel;
import bib.local.ui.gui.panels.LoginPanel;
import bib.local.ui.gui.panels.MitarbeiterMenuPanel;
import bib.local.ui.gui.panels.RegisterPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BibClientGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private EShop shop;

    public BibClientGUI() {
        // Set the title of the window
        setTitle("Bibliothek GUI");

        // Set the size of the window
        setSize(800, 600);

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the EShop
        try {
            shop = new EShop("BIB");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler beim Laden des EShops: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Initialize the CardLayout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create the Login and Register panels
        JPanel loginPanel = new LoginPanel(cardLayout, mainPanel, shop, this);
        JPanel registerPanel = new RegisterPanel(cardLayout, mainPanel, shop);
        //JPanel mitarbeiterMenuPanel = new MitarbeiterMenuPanel(cardLayout, mainPanel, shop);
        //JPanel kundeMenuPanel = new KundeMenuPanel(cardLayout, mainPanel, shop, null);

        // Add panels to the main panel with identifiers
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        //mainPanel.add(mitarbeiterMenuPanel, "mitarbeiterMenu");
        //mainPanel.add(kundeMenuPanel, "kundeMenu");

        // Show the login panel initially
        cardLayout.show(mainPanel, "login");

        // Add the main panel to the frame
        add(mainPanel);

        // Make the window visible
        setVisible(true);
    }

    public void zeigeKundeMenuPanel(Kunde eingeloggterKunde) {
        KundeMenuPanel kundeMenuPanel = new KundeMenuPanel(cardLayout, mainPanel, shop, eingeloggterKunde);
        mainPanel.add(kundeMenuPanel, "kundeMenu");
        cardLayout.show(mainPanel, "kundeMenu");
    }

    public void zeigeMitarbeiterMenuPanel(Mitarbeiter eingeloggterMitarbeiter) {
        MitarbeiterMenuPanel mitarbeiterMenuPanel = new MitarbeiterMenuPanel(cardLayout, mainPanel, shop, eingeloggterMitarbeiter);
        mainPanel.add(mitarbeiterMenuPanel, "mitarbeiterMenu");
        cardLayout.show(mainPanel, "mitarbeiterMenu");
    }


    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(BibClientGUI::new);
    }
}
