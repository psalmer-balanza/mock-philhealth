package gui.register;

import Objects.DataAccess;
import Objects.Database.Registration;
import gui.MainMenu;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class RegistrationComplete extends JFrame{
    private JPanel mainPanel;
    private JButton registerAnotherSIMButton;
    private JLabel congratsLbl;
    private JLabel thankyouLbl;
    private JButton backToMainMenuButton;
    private JTextField controlNumberField;
    private JTextArea numberTextArea;

    public RegistrationComplete(DataAccess functions, Registration reg) throws SQLException {
        functions.setCon();
        new JFrame();
        setTitle("Registration Complete");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 570, 300);
        setLocationRelativeTo(null);

        numberTextArea = new JTextArea();
        //numberTextArea.setSize(50,50);

        controlNumberField.setText(String.valueOf(DataAccess.getRegId(reg.getSimNo())));

        backToMainMenuButton.addActionListener(e -> {
            new MainMenu(functions);
            dispose();
        });

        registerAnotherSIMButton.addActionListener(e -> {
            new EnterMobileNumber(functions);
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Program", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }

            }
        });
    }
}
