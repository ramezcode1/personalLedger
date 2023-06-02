import java.util.InputMismatchException;
import java.util.Scanner;

public class LoginMenu {
    private final LoginController loginController;
    static Scanner input = new Scanner(System.in);

    public LoginMenu(LoginController loginController) {
        this.loginController = loginController;
    }

    public void display() {

        System.out.println("##########################################");
        System.out.println("#                Login in                #");
        System.out.println("##########################################");
        User user = null;
        do {
            System.out.println("\nPlease select one of these options");
            System.out.println("\t\t\t1 login");
            System.out.println("\t\t\t2 register");
            System.out.println("\t\t\t3 Exit");
            System.out.print("Select(1-3): ");
            try {
                int option = input.nextInt();
                switch (option) {
                    case 1 -> loginController.login();
                    case 2 -> loginController.register();
                    case 3 -> System.exit(1);
                }
            } catch (InputMismatchException ex) {
                input.nextLine();
            }
        } while (!Auth.isLoggedIn());
    }
}
