import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LedgerController {

    private final FileStorage fileStorage;
    private final LedgerView ledgerView;
    static Scanner input = new Scanner(System.in);

    public LedgerController(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        this.ledgerView = new LedgerView();
    }

    public void init() {
        //  Get ledgers from user's ledger file
        fileStorage.readUserLedgers();
    }

    public void listAllRecords() {
        ledgerView.printLedgerDetails(fileStorage.getLedgersList());
    }

    public void saveAllRecords() {
        fileStorage.saveUserLedgers();
    }

    public void addCredit() {
        try {
            System.out.println("Enter Credit amount");
            double credit = input.nextDouble();
            input.nextLine();   // Discard line
            System.out.println("Enter the description: ");
            String description = input.nextLine();
            System.out.println("Enter date in format yyyy-MM-dd");
            String dateString = input.next();
            LocalDate date = LocalDate.parse(dateString);
            CreditLedger creditLedger = new CreditLedger(date, description, credit);
            Ledger.balance += credit;
            fileStorage.getLedgersList().add(creditLedger);
        } catch (InputMismatchException ex) {
            System.out.println("Please enter valid input " + ex.getMessage());
        } catch (DateTimeException ex) {
            System.out.println("Please enter a valid date in format yyyy-MM-dd");
        } catch (IllegalArgumentException | ArithmeticException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void MakeDebit() {
        try {
            System.out.println("Enter Debit amount");
            double debit = input.nextDouble();
            input.nextLine();   // Discard line
            if (debit > Ledger.balance) {
                System.out.println("\nYour amount can not exceed your current balance (" + Ledger.balance + ")\n");
                return;
            }
            System.out.println("Enter the description: ");
            String description = input.nextLine();
            System.out.println("Enter date in format yyyy-MM-dd");
            String dateString = input.next();
            LocalDate date = LocalDate.parse(dateString);
            DebitLedger debitLedger = new DebitLedger(date, description, debit);
            Ledger.balance -= debit;
            fileStorage.getLedgersList().add(debitLedger);
        } catch (InputMismatchException ex) {
            System.out.println("Please enter valid input " + ex.getMessage());
        } catch (DateTimeException ex) {
            System.out.println("Please enter a valid date in format yyyy-MM-dd");
        } catch (IllegalArgumentException | ArithmeticException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void ModifyRecord() {
        ArrayList<Ledger> ledgersList = fileStorage.getLedgersList();
        if (ledgersList.isEmpty()) {
            System.out.println("\nThere are no records in the system. please add a new one\n");
            return;
        }
        try {
            System.out.println("Please enter a record # to modify");
            int recordID = input.nextInt();
            input.nextLine();   //discard input
            if (!ledgersList.contains(ledgersList.get(recordID - 1))) {
                System.out.println("the record does not exist in the ledger");
            } else {
                System.out.println("Enter the Description: ");
                String description = input.nextLine();
                System.out.println("Enter the amount: ");
                double amount = input.nextDouble();
                System.out.println("Enter date in format yyyy-MM-dd");
                String dateString = input.next();
                LocalDate date = LocalDate.parse(dateString);
                Ledger ledger = ledgersList.get(recordID - 1);
                ledger.setDescription(description);
                ledger.setDate(date);
                if (ledger instanceof DebitLedger) {
                    ((DebitLedger) ledger).setDebit(amount);
                }
                if (ledger instanceof CreditLedger) {
                    ((CreditLedger) ledger).setCredit(amount);
                }
                System.out.println("Record #" + recordID + " successfully updated");
            }
        } catch (InputMismatchException | IndexOutOfBoundsException ex) {
            System.out.println("\nPlease enter a valid no. from the list\n");
        } catch (DateTimeException ex) {
            System.out.println("Please enter a valid date in format yyyy-MM-dd");
        } catch (IllegalArgumentException | ArithmeticException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteRecord() {
        ArrayList<Ledger> ledgersList = fileStorage.getLedgersList();
        if (ledgersList.isEmpty()) {
            System.out.println("\nThere are no records in the system. please add a new one\n");
            return;
        }
        System.out.println("Please enter a record # to delete");
        try {
            int recordID = input.nextInt();
            if (!ledgersList.contains(ledgersList.get(recordID - 1))) {
                System.out.println("the record does not exist in the ledger");
            } else {
                ledgersList.remove(recordID - 1);
                System.out.println("Record #" + recordID + " deleted");
            }
        } catch (InputMismatchException | IndexOutOfBoundsException ex) {
            System.out.println("\nPlease enter a valid no. from the list\n");
        }
    }

    public void sortByDate() {
        Collections.sort(fileStorage.getLedgersList(), new DateComparator());
        this.listAllRecords();
    }
}
