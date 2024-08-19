import Objects.DataAccess;
import gui.MainMenu;

public class Main {
    public static void main(String[] args) {

        DataAccess functions = new DataAccess();
        functions.setCon();
        new MainMenu(functions);
    }
 }

