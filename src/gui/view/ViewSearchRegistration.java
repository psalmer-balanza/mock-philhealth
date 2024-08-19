package gui.view;

import Objects.DataAccess;
import gui.MainMenu;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

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

public class ViewSearchRegistration extends JFrame{
    private JPanel mainPanel;
    private JButton backBtn;
    private JButton continueBtn;
    private JTextField regidTextField;
    private JLabel titleLbl;

    public ViewSearchRegistration(DataAccess functions) {
        functions.setCon();
        regidTextField.setDocument(new JTextFieldLimit(7));
        new JFrame();
        setTitle("View Registration");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 700, 300);
        setLocationRelativeTo(null);

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu(functions);
                dispose();
            }
        });

        regidTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterCtrlNumber(functions);
            }
        });

        continueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                   enterCtrlNumber(functions);
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

    public void enterCtrlNumber(DataAccess functions){
        if(regidTextField.getText().equals("") | regidTextField.getText().matches("[a-zA-Z]+")){
            JOptionPane.showMessageDialog(null,"Invalid Input! Please enter numbers only.");
            regidTextField.setText(null);
        } else{
            try{
                if(functions.registrationExists(regidTextField.getText())){
                    new ViewRegistrationInformation(functions,Integer.parseInt(regidTextField.getText()));
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null,"Registration does not exist! Please enter a valid CONTROL NUMBER.");
                }

            } catch(SQLException ex){
                JOptionPane.showMessageDialog(null,"ERROR: " +ex);
            }
        }
    }
}
