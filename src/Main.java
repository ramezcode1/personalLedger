public class Main {
    public static String AppName = "Personal Ledger App";

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        System.out.println("Welcome to " + AppName);

        FileStorage fileStorage = new FileStorage();

        LoginController loginController = new LoginController(fileStorage);
        LedgerController ledgerController = new LedgerController(fileStorage);

        LoginMenu loginMenu = new LoginMenu(loginController);
        LedgerMenu ledgerMenu = new LedgerMenu(ledgerController);

        loginMenu.display();
        ledgerMenu.display();
    }
}