package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.domain.exceptions.NutzerExistiertBereitsException;
import bib.local.entities.Mitarbeiter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Panel zum Registrieren eines neuen Mitarbeiters.
 */
public class RegisterMitarbeiterPanel extends JPanel {

    // Interface to notify when a new employee is registered
    public interface RegisterMitarbeiterListener {
        void onEmployeeRegistered(Mitarbeiter mitarbeiter);
    }

    private EShop shop;
    private RegisterMitarbeiterListener registerListener;

    private JTextField nummerField;
    private JTextField nameField;
    private JTextField benutzerkennungField;
    private JTextField passwortField;
    private JButton registerButton;

    public RegisterMitarbeiterPanel(EShop shop, RegisterMitarbeiterListener listener) {
        this.shop = shop;
        this.registerListener = listener;

        setupUI();
        setupEvents();
    }

    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Dimension borderMinSize = new Dimension(5, 10);
        Dimension borderPrefSize = new Dimension(5, 10);
        Dimension borderMaxSize = new Dimension(5, 10);
        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        add(new JLabel("Mitarbeiternummer: "));
        nummerField = new JTextField();
        add(nummerField);

        add(new JLabel("Name: "));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Benutzerkennung: "));
        benutzerkennungField = new JTextField();
        add(benutzerkennungField);

        add(new JLabel("Passwort: "));
        passwortField = new JTextField();
        add(passwortField);

        Dimension minSize = new Dimension(1, 20);
        Dimension prefSize = new Dimension(1, Short.MAX_VALUE);
        Dimension maxSize = new Dimension(1, Short.MAX_VALUE);
        Box.Filler filler = new Box.Filler(minSize, prefSize, maxSize);
        add(filler);

        registerButton = new JButton("Registrieren");
        add(registerButton);

        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        setBorder(BorderFactory.createTitledBorder("Mitarbeiter registrieren"));
    }

    private void setupEvents() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Event: " + ae.getActionCommand());
                onRegisterButtonClick();
            }
        });
    }

    private void onRegisterButtonClick() {
        String nummer = nummerField.getText();
        String name = nameField.getText();
        String benutzerkennung = benutzerkennungField.getText();
        String passwort = passwortField.getText();

        if (!nummer.isEmpty() && !name.isEmpty() && !benutzerkennung.isEmpty() && !passwort.isEmpty()) {
            try {
                int nummerAlsInt = Integer.parseInt(nummer);
                Mitarbeiter mitarbeiter = shop.registriereMitarbeiter(nummerAlsInt, name, benutzerkennung, passwort);
                shop.schreibeMitarbeiter();

                nummerField.setText("");
                nameField.setText("");
                benutzerkennungField.setText("");
                passwortField.setText("");

                // Notify the listener (e.g., main frame) about the new employee
                registerListener.onEmployeeRegistered(mitarbeiter);
                JOptionPane.showMessageDialog(this, "Mitarbeiter wurde erfolgreich registriert.");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Bitte eine gültige Zahl eingeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (NutzerExistiertBereitsException | IOException e) {
                JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bitte alle Felder ausfüllen.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
