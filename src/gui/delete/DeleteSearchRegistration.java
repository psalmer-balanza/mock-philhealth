package gui.delete;

import Objects.DataAccess;
import gui.MainMenu;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

class  JTextFieldLimit extends PlainDocument {
    private int limit;
    JTextFieldLimit(int limit) {
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

public class DeleteSearchRegistration extends JFrame{
    private JButton continueBtn;
    private JButton backToMainMenuBtn;
    private JTextField ctrlNumberTxtField;
    private JPanel mainPanel;

    public DeleteSearchRegistration(DataAccess functions) {
        functions.setCon();
        ctrlNumberTxtField.setDocument(new JTextFieldLimit(7));
        new JFrame();
        setTitle("Delete Registration");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 540, 270);
        setLocationRelativeTo(null);
        ctrlNumberTxtField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });

        backToMainMenuBtn.addActionListener(e -> {
            new MainMenu(functions);
            dispose();
        });

        ctrlNumberTxtField.addActionListener(e -> {
            controlNumber(functions);
        });

        continueBtn.addActionListener(e -> {
           controlNumber(functions);
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

    public void controlNumber(DataAccess functions){
        try {
            if(ctrlNumberTxtField.getText().equals("") | ctrlNumberTxtField.getText().matches("[a-zA-Z]+")){
                JOptionPane.showMessageDialog(null,"Invalid Input! Please enter numbers only.");
                ctrlNumberTxtField.setText(null);
            } else if(functions.registrationExists(ctrlNumberTxtField.getText())) {
                dispose();
                new DeleteRegistration(functions, Integer.parseInt(ctrlNumberTxtField.getText()));
            } else {
                JOptionPane.showMessageDialog(null, "Registration " +
                        ctrlNumberTxtField.getText() + " Not Found!");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
