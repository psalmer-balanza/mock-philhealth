package Objects;

import Objects.Database.NetworkDetails;
import Objects.Database.Registration;
import Objects.Database.Sim;
import Objects.Database.Subscriber;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DataAccess {
    private static Connection con;

    public void setCon() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3308/jjackp", "root", "");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection failed");
            System.exit(0);
        }
    }
    public Connection getCon() {
        return con;
    }

    //this method should be used upon registration and in entering personal info
    //add name?
    public int checkExistingSubscriber(int idType, String idNumber, String birthdate) throws Exception {
        ArrayList<Subscriber> allSubscribers = getSubscribers();
        int subsid = 0;

        for (int i = 0; i < allSubscribers.size(); i++) {
            if (allSubscribers.get(i).getBirthDate().equals(birthdate)) {
                if (allSubscribers.get(i).getTypeId() == idType) {
                    if (allSubscribers.get(i).getIdNumber().equals(idNumber)) {
                        subsid = allSubscribers.get(i).getSubsID();
                    }
                }
            }
        }

        return subsid;
    }
    public void addSubscriber(Subscriber sub) {
        String query = "INSERT INTO subscriber (firstname, middlename, lastname, suffix, unitno,street, subdivision, province, city, barangay ,zipcode, sex, birthdate, nationality, typeid, idPic, idnumber) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            setCon();
            PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setString(1, sub.getFirstName());
            ps.setString(2, sub.getMiddleName());
            ps.setString(3, sub.getLastName());
            ps.setString(4, sub.getSuffix());
            ps.setString(5, sub.getUnitNo());
            ps.setString(6, sub.getStreet());
            ps.setString(7, sub.getSubdivision());
            ps.setString(8, sub.getProvince());
            ps.setString(9, sub.getCity());
            ps.setString(10, sub.getBarangay());
            ps.setInt(11, sub.getZipCode());
            ps.setString(12, sub.getSex());
            ps.setString(13, sub.getBirthDate());
            ps.setString(14, sub.getNationality());
            ps.setInt(15, sub.getTypeId());
            ps.setBlob(16, sub.getIdPic());
            ps.setString(17, sub.getIdNumber());

            ps.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to add subscriber");
            e.printStackTrace();
        }
    }

    public void addSim(Sim sim, Subscriber sub, NetworkDetails netDet){
        String query = "INSERT INTO sim(simno, subsid, networkID)VALUES(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1,sim.getSimNo());
            ps.setInt(2, getSubscriberId(sub));
            ps.setInt(3, getNetworkId(netDet));
            ps.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to add sim");
            e.printStackTrace();
        }
    }

    public void addRegistration(Registration reg) {
        String query = "INSERT INTO registration(simno, regdate, regtype) VALUES (?,?,?)";
        try {
            setCon();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1,reg.getSimNo());
            //change string to an sql date object to be passed
            String date = reg.getRegistrationDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // your template here
            java.util.Date dateStr = formatter.parse(date);
            java.sql.Date dateDB = new Date(dateStr.getTime());
            ps.setDate(2,dateDB);
            ps.setString(3,reg.getRegistrationType());
            ps.execute();
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to add registration!");
            e.printStackTrace();
        }
    }
    public Registration retrieveRegistration(int regId) throws SQLException {
        ArrayList<Registration> registrations = new ArrayList<>();
        String query = "select registration.regid, sim.simno,CONCAT(subscriber.firstname,' ',subscriber.middlename,' '," +
                "subscriber.lastname,' ',subscriber.suffix),registration.regdate, registration.regtype FROM registration" +
                " JOIN sim USING(simno) JOIN subscriber USING(subsid)WHERE registration.regid=?";
        PreparedStatement ps = con.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ps.setInt(1,regId);
        ResultSet rs = ps.executeQuery();
        rs.beforeFirst();
        rs.next();

        Registration registration = new Registration(rs.getInt(1),rs.getLong(2),
                rs.getString(3),rs.getString(4),rs.getString(5));

        rs.close();
        return registration;
    }

    public boolean updateSubscriber(String idNumber, String middleName, String lastName, String unitNo, String street
    , String subdivision, String province, String city, String barangay, int zipCode){
        String query = "UPDATE subscriber SET middlename=?, lastname=?, unitno=?, street=?, subdivision=?, province=?, city=?" +
                ", barangay=?, zipcode=? WHERE subscriber.idnumber=?";
        try{
            PreparedStatement ps = con.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ps.setString(1,middleName);
            ps.setString(2,lastName);
            ps.setString(3,unitNo);
            ps.setString(4,street);
            ps.setString(5,subdivision);
            ps.setString(6,province);
            ps.setString(7,city);
            ps.setString(8,barangay);
            ps.setInt(9,zipCode);
            ps.setString(10,idNumber);
            ps.execute();
            return true;
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"ERROR! Could Not Update!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrationExists(String key) throws SQLException {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM registration WHERE regid = " + key);
            return rs.next();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } return false;
    }

    public ArrayList<String> getNetworkProviders() throws SQLException {
        ArrayList<String> providers = new ArrayList<>();
        String query = "select networkname FROM network_details";

        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            providers.add(rs.getString(1));
        }
        return providers;
    }

    public static int getSubscriberId(Subscriber sub) throws SQLException {
        String query = "SELECT subsid FROM subscriber WHERE idnumber =" + "'"
                + sub.getIdNumber() + "'";
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = stmt.executeQuery(query);
        resultSet.beforeFirst();
        resultSet.next();
        return resultSet.getInt("subsid");
    }
    public ImageIcon getSubscriberIdPic(int regid) throws SQLException{
        String query = "select idpic from subscriber inner join sim using (subsid) " +
                "inner join registration using(simno) where regid = " + "'" + regid + "'";
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery(query);
        if(rs.next()) {
            try {
                Blob blob = rs.getBlob("idpic");
                byte[] imageData = blob.getBytes(1, (int) blob.length());
                ImageIcon icon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageData)));

                //some stuff about pictures
                int width = 200, height = 200;
                Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // scale the image to 100x100 pixels
                return new ImageIcon(scaledImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ImageIcon getSubscriberIdPicUsingSubsID(int subsID) throws SQLException{
        String query = "select idpic from subscriber where subsid = " + subsID;
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery(query);
        if(rs.next()) {
            try {
                Blob blob = rs.getBlob("idpic");
                byte[] imageData = blob.getBytes(1, (int) blob.length());
                ImageIcon icon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageData)));

                //some stuff about pictures
                int width = 200, height = 200;
                Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // scale the image to 100x100 pixels
                return new ImageIcon(scaledImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ImageIcon getSubscriberIdPicNotYetSubscribed(Blob blob) throws SQLException {
        try {
            byte[] imageData = blob.getBytes(1, (int) blob.length());
            ImageIcon icon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageData)));

            //some stuff about pictures
            int width = 200, height = 200;
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // scale the image to 100x100 pixels
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean checkIfSimnoIsRegistered(String simno) {
        String query = "SELECT simno FROM registration WHERE simno = '" + simno.substring(1) + "'";
        try {
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery(query);
            //see if empty set returned
            if (rs.next()) {
                if(simno.substring(1).equals(rs.getString("simno"))) {
                    return true;
                } else {
                return false;
            } }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static int getNetworkId (NetworkDetails netDet) throws SQLException {
        String query = "SELECT networkid FROM network_details WHERE networkname= " + "'"+netDet.getNetworkName()+"'";

        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        ResultSet rs = stmt.executeQuery(query);
        rs.beforeFirst();
        rs.next();
        return rs.getInt(1);
    }

    public String getIdNameFromIdType(String idName) throws SQLException{
        String query = "SELECT name from id_type WHERE typeid =" + "'" + idName + "'";
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery(query);
        rs.next();
        return rs.getString(1);
    }
    public ArrayList<String> getIdNameArray() throws SQLException {
        String query = "SELECT name from id_type";
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery(query);
        rs.beforeFirst();
        ArrayList<String> idNameList = new ArrayList<>();
        while(rs.next()) {
            idNameList.add(rs.getString("name"));
        } return idNameList;
    }
    public int getIdTypeFromIdName(String idname) throws SQLException {
        String query = "SELECT typeid FROM id_type WHERE name =" + "'" + idname + "'";
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }
    public String getIdNameFromIdType(int idType) throws SQLException {
        String query = "SELECT name FROM id_type WHERE typeid =" + "'" + idType + "'";
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery(query);
        rs.next();
        return rs.getString(1);
    }

    public boolean checkIfIdIsAlreadyRegistered(int idType, String idNumber) {
        String query = "SELECT typeid, idnumber FROM subscriber WHERE typeid = " +
        "'" + idType + "'" + "AND idnumber = " + "'" + idNumber + "'";
        try {
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                if (rs.getInt(1) == idType && rs.getString(2).equals(idNumber.trim())) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getRegId(long simNumber) throws SQLException {
        String query = "SELECT regid FROM registration WHERE simno= " + "'"+simNumber+"'";

        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = stmt.executeQuery(query);
        resultSet.beforeFirst();
        resultSet.next();
        return resultSet.getInt(1);
    }
    public ArrayList<Subscriber> getSubscribers () throws Exception {
        ArrayList<Subscriber> subscribers = new ArrayList<>();
        String query = "select * FROM subscriber ORDER by subsid";
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Subscriber subscriber = new Subscriber(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                    rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11),
                    rs.getInt(12), rs.getString(13), rs.getDate(14).toString(), rs.getString(15),
                    rs.getInt(16),rs.getBlob(17),rs.getString(18));
            subscribers.add(subscriber);
        }

        rs.close();
        return subscribers;
    }

    public Subscriber retrieveSubscriber(int subsID) throws SQLException {
        Subscriber subscriber = null;
        String query = "SELECT * FROM subscriber WHERE + subsid ="+subsID;

        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            subscriber = new Subscriber(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                    rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11),
                    rs.getInt(12), rs.getString(13), rs.getDate(14).toString(), rs.getString(15),
                    rs.getInt(16), rs.getBlob(17), rs.getString(18));
        }
        rs.close();

        return subscriber;
    }

    public boolean idNumberExists(String idNum){
        String query = "SELECT idnumber FROM subscriber WHERE idnumber = '" + idNum +"'";
        try {
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery(query);
            //see if empty set returned
            if (rs.next()) {
                if(idNum.equals(rs.getString("idnumber"))) {
                    return true;
                } else {
                    return false;
                } }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getIdType(String idType){
        int idTypeNo = 0;

        try{
            String query = "SELECT typeid FROM id_type WHERE name = '" +idType+ "'";
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
               idTypeNo = rs.getInt(1);
            }
            rs.close();

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return idTypeNo;
    }

    /*
    UNUSUSED METHODS
    --removed SIM data store btw


    public ArrayList<Sim> getSims () throws Exception {
        ArrayList<Sim> sims = new ArrayList<>();
        String query = "select * FROM sim ORDER by simno";
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Sim sim = new Sim(rs.getInt(1), rs.getInt(2), rs.getInt(3));
            sims.add(sim);
        }

        rs.close();
        return sims;
    }

    public ArrayList<Registration> getRegistrations () throws Exception {
        ArrayList<Registration> registrations = new ArrayList<>();
        String query = "select registration.regid, sim.simno, " +
                "CONCAT(subscriber.firstname,' ',subscriber.middlename' ',subscriber.lastname,' ',subsciber.prefix)," +
                "registration.regdate, registration.regtype" +
                " FROM registration JOIN sim USING(simno) JOIN subscriber USING(subsid)";
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Registration registration = new Registration(rs.getInt(1), rs.getInt(2), rs.getString(3),
                    rs.getString(4));
            registrations.add(registration);
        }

        rs.close();
        return registrations;
    }
    public Subscriber retrieveSubscriber(int controlNumber) throws SQLException {
        Subscriber subscriber = null;
        String query = "SELECT * FROM subscriber INNER JOIN registration ON subscriber.subsid = registration.simno WHERE" +
                "registration.regid =?";

        PreparedStatement ps =  con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ps.setInt(1,controlNumber);
        ResultSet rs = ps.executeQuery();
        rs.beforeFirst();


        return subscriber;
    }
     */
}
