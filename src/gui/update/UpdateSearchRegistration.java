package gui.update;

import Objects.DataAccess;
import gui.MainMenu;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class JTextFieldLimit extends PlainDocument {
    private int limit;
    public JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    JTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null)
            return;

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}

public class UpdateSearchRegistration extends JFrame {
    private JPanel mainPanel;
    private JButton continueButton;
    private JButton backToMainMenuButton;
    private JTextField enterIDHereTextField;

    public UpdateSearchRegistration(DataAccess functions) {
        functions.setCon();
        enterIDHereTextField.setDocument(new JTextFieldLimit(15));
        new JFrame();
        setTitle("Update Subscriber Information");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 600, 300);
        setLocationRelativeTo(null);

        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu(functions);
                dispose();
            }
        });

        enterIDHereTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idNumberValidate(functions);
            }
        });

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idNumberValidate(functions);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Program", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }

            }
        });
    }

    public void idNumberValidate(DataAccess functions){
        if(functions.idNumberExists(enterIDHereTextField.getText())){
            new EditUserInformation(enterIDHereTextField.getText(), functions);
            dispose();
        }else{
            JOptionPane.showMessageDialog(null, "Subscriber not found! Please enter a previously used ID Number.");
        }
    }
}
