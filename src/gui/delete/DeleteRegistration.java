package gui.delete;

import Objects.DataAccess;
import gui.MainMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DeleteRegistration extends JFrame{
    private JPanel mainPanel;
    private JButton cancelBtn;
    private JButton deleteBtn;
    private JPanel btnPanel;
    private JLabel titleLbl;
    private JTable table1;
    private JLabel idPicLabel;
    private JScrollPane table1Pane;
    private JPanel infoTablePanel;
    private JTable table2;
    private JScrollPane table2Pane;
    private JTable table3;
    private JScrollPane table3Pane;

    public DeleteRegistration(DataAccess functions, int regid) {
        functions.setCon();
        new JFrame();
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 850, 369);
        setLocationRelativeTo(null);
        setTitle("Confirm Deletion");
        titleLbl.setForeground(Color.white);

        //query to find registration
        String registrationQuery = "SELECT * FROM registration WHERE regid = " + regid;
        String subscriberQuery = "SELECT * FROM subscriber CROSS JOIN registration, sim where registration.simno = " +
                "sim.simno AND sim.subsid = subscriber.subsid AND registration.regid = " + regid;
        Connection con = functions.getCon();
        String subsid;
        //print table of the registration
        try {
            //subscriber query/statement/resultset
            Statement st1 = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_SENSITIVE);
            ResultSet rs1 = st1.executeQuery(subscriberQuery);

            //tables to populate, overrides isCellEditable in order to keep the table results uneditable
            DefaultTableModel model1 = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel model2 = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel model3 = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            //set the headers
            String[] colNames1 = new String[]{"Sim Number", "Full name", "Registration Date", "Registration Type"};
            String[] colNames2 = new String[]{"Birthdate", "Sex", "Nationality", "ID Type", "ID Number"};
            String[] colNames3 = new String[]{"Unit No.", "Street", "Subdivision", "Barangay", "Province", "City", "Zipcode"};

            //attributes of subscriber table
            rs1.next();
            String simno, firstname, middlename, lastname, unitno, street, subdivision, province,
                    city, barangay, zipcode, sex, birthdate, nationality, idtype, idnumber;
            //populate subscriber query
            subsid=rs1.getString("subsid");
            simno=rs1.getString("simno");
            firstname=rs1.getString("firstname");
            middlename=rs1.getString("middlename");
            lastname=rs1.getString("lastname");
            unitno=rs1.getString("unitno");
            street=rs1.getString("street");
            subdivision=rs1.getString("subdivision");
            province=rs1.getString("province");
            city=rs1.getString("city");
            barangay=rs1.getString("barangay");
            zipcode=rs1.getString("zipcode");
            sex=rs1.getString("sex");
            birthdate=rs1.getString("birthdate");
            nationality=rs1.getString("nationality");
            idtype=rs1.getString("typeid");
            idnumber=rs1.getString("idnumber");

            //get idname from idtype
            String idName = functions.getIdNameFromIdType(idtype);

            //registration query/statement/resultset
            Statement st2 = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_SENSITIVE);
            ResultSet rs2 = st2.executeQuery(registrationQuery);

            rs2.next();

            //attributes of registration table
            String regdate, regtype;
            //create a SQL date object and convert to string
            Date sqlDate = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            regdate = dateFormat.format(sqlDate);
            //populate registration query
            regtype=rs2.getString("regtype");

            //set idpic constraints
            Blob blob = rs1.getBlob("idpic");
            byte[] imageData = blob.getBytes(1, (int) blob.length());
            ImageIcon icon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageData)));

            //some stuff about pictures
            int width = 200, height = 200;
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // scale the image to 100x100 pixels
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            idPicLabel.setIcon(scaledIcon);
            idPicLabel.setPreferredSize(new Dimension(width, height));
            idPicLabel.setLayout(null);

            //add the data to the rows
            String[] row1 = {simno, firstname + " " + middlename + " " + lastname, regdate, regtype};
            String[] row2 = {birthdate, sex, nationality, idName, idnumber};
            String[] row3 = {unitno, street, subdivision, barangay, province, city, zipcode};

            //add the rows to the tableModels
            model1.setColumnIdentifiers(colNames1);
            model2.setColumnIdentifiers(colNames2);
            model3.setColumnIdentifiers(colNames3);

            Dimension tableDimensions = new Dimension(600, 20);
            Dimension paneDimensions = new Dimension(600, 50);
            model1.addRow(row1);
            table1.setPreferredSize(tableDimensions);
            table1.setModel(model1);
            table1Pane.setPreferredSize(paneDimensions);

            model2.addRow(row2);
            table2.setPreferredSize(tableDimensions);
            table2.setModel(model2);
            table2Pane.setPreferredSize(paneDimensions);

            model3.addRow(row3);
            table3.setPreferredSize(tableDimensions);
            table3.setModel(model3);
            table3Pane.setPreferredSize(paneDimensions);
            pack();
        } catch (Exception ee) {
            throw new RuntimeException(ee);
        }
        //cancel deletion
        cancelBtn.addActionListener(e -> {
            new DeleteSearchRegistration(functions);
            dispose();
        });
        //confirm delete
        deleteBtn.addActionListener(e -> {
            try {
                PreparedStatement pst = con.prepareStatement("delete from subscriber where subsid = " + subsid);
                pst.executeUpdate();
                pst = con.prepareStatement("delete from registration where regid = " + regid);
                pst.executeUpdate();
                pst = con.prepareStatement("DELETE sim from sim inner join registration on sim.simno=registration.simno where registration.regid= " +regid);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registration Deleted Successfully!");

                dispose();
                new MainMenu(functions);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }); //end of delete actionListener

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
