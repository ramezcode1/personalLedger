import java.util.Scanner;

public class LoginController {

    private final FileStorage fileStorage;
    static Scanner input = new Scanner(System.in);

    public LoginController(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        //  Get usersList from users file
        fileStorage.readUsersFromFile();
    }

    public void login() {
        System.out.print("USERNAME: ");
        String username = input.next();
        User user = findUser(username);
        if (user == null) {
            System.out.println("\n### username is not exists. Try again or create a new username!! ###\n");
            return;
        }
        System.out.print("Password: ");
        String password = input.next();
        if (!user.getPassword().equals(password)) {
            System.out.println("\n### wrong password!! ###\n");
            return;
        }
        Auth.user = user;
    }

    public void register() {
        User user = null;
        try {
            String username = "";
            do {
                System.out.print("USERNAME: ");
                username = input.next();
                user = findUser(username);
                if (user != null) {
                    System.out.println("\n### username already exists. Please select another username ###");
                }
            } while (user != null);

            System.out.print("Password: ");
            String password = input.next();
            user = new User(username, password);
            fileStorage.saveUsersIntoFile(user);
        } catch (IllegalArgumentException ex) {
            System.out.println("\n### " + ex.getMessage() + " ####");
        }
        Auth.user = user;
    }

    private User findUser(String useName) {
        for (User user : fileStorage.getUsersList()) {
            if (user.getUserName().equals(useName)) {
                return user;
            }
        }
        return null;
    }
}
