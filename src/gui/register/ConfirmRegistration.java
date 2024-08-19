package gui.register;

import Objects.DataAccess;
import Objects.Database.NetworkDetails;
import Objects.Database.Registration;
import Objects.Database.Sim;
import Objects.Database.Subscriber;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static Objects.DataAccess.getNetworkId;

public class ConfirmRegistration extends JFrame{
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel titleLbl;
    private JLabel regtypeLbl;
    private JLabel regdateLbl;
    private JLabel numberLbl;
    private JLabel nameLbl;
    private JComboBox<String>regTypeDropDown;
    private JButton registerButton;
    private JButton cancelButton;
    private JTextField nameField;
    private JTextField mobileNumField;
    private JTextField regDateField;

    public ConfirmRegistration(DataAccess functions, Sim sim, NetworkDetails netDet, Subscriber sub, boolean exist) {
        functions.setCon();
        frame = new JFrame();
        frame.setTitle("Review Subscriber Information");
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
        frame.setSize( 500, 300);
        frame.setLocationRelativeTo(null);

        String [] regTypes = {"OWNER", "PARENT/LEGAL GUARDIAN",
                "AUTHORIZED SIGNATORY/REPR"};

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String today = formatter.format(now);

        nameField.setText(sub.getFirstName()
                + " " + sub.getMiddleName()
                + " " + sub.getLastName()
                + " " + sub.getSuffix());
        mobileNumField.setText(Long.toString(sim.getSimNo()));
        regDateField.setText(today);

        for (String regType : regTypes) {
            regTypeDropDown.addItem(regType);
        }

        registerButton.addActionListener(e -> {
            Registration reg = new Registration();
            reg.setSimNo(sim.getSimNo());
            reg.setRegistrationDate(today);
            reg.setRegistrationType((String)regTypeDropDown.getSelectedItem());
            if (!exist) {
                try {
                    //add to the Subscriber table using the Subscriber object
                    functions.addSubscriber(sub);

                    //before adding Sim table, set the information of the Sim object
                    sim.setSubsid(sub.getSubsID());
                    netDet.setNetworkId(getNetworkId(netDet));
                    sim.setNetworkId(netDet.getNetworkId());

                    //add to the Sim table using the Sim object
                    functions.addSim(sim, sub, netDet);
                    //add to the Registration table using the Registration object
                    functions.addRegistration(reg);

                    //new RegistrationComplete GUI using the Registration object
                    new RegistrationComplete(functions, reg);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR!: " + ex);
                }
            } else {
                try {
                //add to the Sim table using the Sim object
                functions.addSim(sim, sub, netDet);
                //add to the Registration table using the Registration object
                functions.addRegistration(reg);

                //new RegistrationComplete GUI using the Registration object
                new RegistrationComplete(functions, reg);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR!: " + ex);
                }
            }
            frame.dispose();
        });

        cancelButton.addActionListener(e -> {
            try {
                if (exist) {
                    frame.dispose();
                    new EnterPersonalInformation(functions, sim, netDet, sub.getSubsID(), true);
                } else {
                    frame.dispose();
                    new EnterPersonalInformation(functions, sim, netDet, sub.getSubsID(), false);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Program", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    System.exit(0);
                } else {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }

            }
        });
    }
}
