package gui.view;

import Objects.DataAccess;
import Objects.Database.Registration;
import gui.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class ViewRegistrationInformation extends JFrame {
    private JLabel titleLbl;
    private JButton backToMainMenuButton;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTextArea textArea1;
    private JLabel idPicLabel;

    public ViewRegistrationInformation(DataAccess functions, int controlNumber) throws SQLException {
        functions.setCon();
        new JFrame();
        setTitle("Registration Information");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 650, 500);
        setLocationRelativeTo(null);
        Registration registration = functions.retrieveRegistration(controlNumber);
        //get idpic and set it
        ImageIcon idPic = functions.getSubscriberIdPic(controlNumber);
        idPicLabel.setIcon(idPic);
        idPicLabel.setPreferredSize(new Dimension(200, 200));
        idPicLabel.setLayout(null);

        textArea1.setFont(new Font("Helvetica", Font.PLAIN,20));
        textArea1.append("------------------------------------------------------ \n");
        textArea1.append("REGISTRATION ID: " + registration.getRegistrationID()+"\n");
        textArea1.append("------------------------------------------------------ \n");
        textArea1.append("SIM NUMBER: +(63)" + registration.getSimNo()+"\n");
        textArea1.append("------------------------------------------------------ \n");
        textArea1.append("OWNER: " + registration.getOwner()+"\n");
        textArea1.append("------------------------------------------------------ \n");
        textArea1.append("REGISTRATION DATE: " + registration.getRegistrationDate()+"\n");
        textArea1.append("------------------------------------------------------ \n");
        textArea1.append("REGISTRATION TYPE: " + registration.getRegistrationType()+"\n");
        textArea1.append("------------------------------------------------------ \n");
        scrollPane.setViewportView(textArea1);

        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu(functions);
                dispose();
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
}
