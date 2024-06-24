package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.entities.Benutzer;
import bib.local.entities.Kunde;
import bib.local.entities.Mitarbeiter;
import bib.local.ui.gui.BibClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private EShop shop;
    private Benutzer eingeloggterBenutzer = null;
    private BibClientGUI mainFrame;

    public LoginPanel(CardLayout cardLayout, JPanel mainPanel, EShop shop, BibClientGUI mainFrame) {
        this.shop = shop;
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        JTextField userText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userText, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passLabel, gbc);

        JPasswordField passText = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passText, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(registerButton, gbc);

        // Action listener to switch to register panel
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "register");
            }
        });

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passText.getPassword());
                try {
                    Kunde kunde = shop.getKundenVW().findeKunde(username, password);
                    if (kunde != null) {
                        shop.setBenutzer(kunde);
                        eingeloggterBenutzer = kunde;
                        JOptionPane.showMessageDialog(null, "Login erfolgreich!");
                        mainFrame.zeigeKundeMenuPanel(kunde);
                        JOptionPane.showMessageDialog(null, shop.getBenutzer().getName());
                    } else {
                        Mitarbeiter mitarbeiter = shop.getMitarbeiterVW().findeMitarbeiter(username, password);
                        if (mitarbeiter != null) {
                            shop.setBenutzer(mitarbeiter);
                            eingeloggterBenutzer = mitarbeiter;
                            JOptionPane.showMessageDialog(null, "Login erfolgreich!");
                            mainFrame.zeigeMitarbeiterMenuPanel(mitarbeiter);
                            //cardLayout.show(mainPanel, "mitarbeiterMenu");
                            JOptionPane.showMessageDialog(null, shop.getBenutzer().getName());
                        } else {
                            JOptionPane.showMessageDialog(null, "Benutzername oder Passwort falsch.", "Fehler", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
