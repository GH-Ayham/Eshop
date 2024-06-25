package bib.local.ui.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import bib.local.domain.EShop;
import bib.local.domain.exceptions.NutzerExistiertBereitsException;
import bib.local.entities.Kunde;

public class RegisterPanel extends JPanel {
    private EShop shop;

    public RegisterPanel(CardLayout cardLayout, JPanel mainPanel, EShop shop) {
        this.shop = shop;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        JTextField nameText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(nameText, gbc);

        JLabel userLabel = new JLabel("Benutzerkennung:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(userLabel, gbc);

        JTextField userText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(userText, gbc);

        JLabel passLabel = new JLabel("Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passLabel, gbc);

        JPasswordField passText = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passText, gbc);

        JLabel strasseLabel = new JLabel("Straße:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(strasseLabel, gbc);

        JTextField strasseText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(strasseText, gbc);

        JLabel plzLabel = new JLabel("PLZ:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(plzLabel, gbc);

        JTextField plzText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(plzText, gbc);

        JLabel wohnortLabel = new JLabel("Wohnort:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(wohnortLabel, gbc);

        JTextField wohnortText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(wohnortText, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(registerButton, gbc);

        JButton backButton = new JButton("Back to Login");
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(backButton, gbc);

        // Action listener to switch back to login panel
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "login");
            }
        });

        // Action listener for register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText();
                String benutzerkennung = userText.getText();
                String passwort = new String(passText.getPassword());
                String strasse = strasseText.getText();
                String plz = plzText.getText();
                String wohnort = wohnortText.getText();

                try {
                    Kunde kunde = shop.registriereKunde(name, benutzerkennung, passwort, strasse, plz, wohnort);
                    shop.schreibeKunde();

                    nameText.setText("");
                    userText.setText("");
                    passText.setText("");
                    strasseText.setText("");
                    plzText.setText("");
                    wohnortText.setText("");

                    JOptionPane.showMessageDialog(null, "Registrierung erfolgreich: " + kunde.getName());
                } catch (NutzerExistiertBereitsException ex) {
                    JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Fehler beim Speichern des Kunden: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
