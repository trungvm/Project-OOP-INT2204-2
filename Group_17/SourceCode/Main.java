import java.util.Scanner;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Your name: ");
        String yourName = sc.nextLine();

        System.out.print("Enter your income: ");
        int income = sc.nextInt();

        App app = new App(income);

        while(true) {
            System.out.println("Hi " + yourName + " !");
            System.out.println("Options:");
            System.out.println("1.Print balance.");
            System.out.println("2.Print total spent in last weak.");
            System.out.println("3.Print all transactions.");
            System.out.println("4.Add a new transaction.");
            System.out.println("5.Add a new category.");
            System.out.println("6.Find and print transaction by category.");
            System.out.println("7.Find and print transaction by date.");
            System.out.println("0.Exit.");

            System.out.println("************************************************************");

            System.out.print("Your option: ");
            int option = sc.nextInt();

            switch (option) {
                case 0:
                    System.out.println("Bye " + yourName + " !");
                    System.exit(0);
                case 1:
                    System.out.println("Your balance: " +
                    app.getBalance());
                    break;
                case 2:
                    System.out.println("Total spent last week: " + app.getTotalSpentTime());
                    break;
                case 3:
                    app.printTransactions();
                    break;
                case 4:
                    app.addTransaction();
                    break;
                case 5:
                    app.addCategory();
                    break;
                case 6:
                    app.findAndPrintCategory();
                    break;
                case 7:
                    app.findAndPrintDate();
                    break;
                default:
                    break;
            }

            System.out.println("************************************************************");

        }
    }
}
