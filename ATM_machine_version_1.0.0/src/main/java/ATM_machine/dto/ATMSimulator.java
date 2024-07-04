package ATM_machine.dto;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATMSimulator {
    private static final String DATA_FILE = "atm_data.txt";

    private static final Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        loadAccountData();
        runATM();
    }

    private static void loadAccountData() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                if (data.length == 3) {
                    String cardNumber = data[0];
                    int pin = Integer.parseInt(data[1]);
                    double balance = Double.parseDouble(data[2]);
                    accounts.put(cardNumber, new Account(cardNumber, pin, balance));
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading account data: " + e.getMessage());
        }
    }

    private static void runATM() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the ATM Simulator!");

        while (true) {
            System.out.print("Enter your card number (XXXX-XXXX-XXXX-XXXX) or 'exit' to quit: ");
            String cardNumber = scanner.nextLine();

            if (cardNumber.equalsIgnoreCase("exit")) {
                saveAccountData();
                break;
            }

            if (isValidCardNumber(cardNumber)) {
                Account account = accounts.get(cardNumber);
                if (account != null) {
                    System.out.print("Enter your PIN: ");
                    int pin = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    if (account.validatePIN(pin)) {
                        handleAccountActions(account, scanner);
                    } else {
                        System.out.println("Invalid PIN. Please try again.");
                    }
                } else {
                    System.out.println("Card number not found. Please try again.");
                }
            } else {
                System.out.println("Invalid card number format. Please try again.");
            }
        }

        System.out.println("Thank you for using the ATM Simulator. Goodbye!");
    }

    private static void handleAccountActions(Account account, Scanner scanner) {
        while (true) {
            System.out.println("\nAccount actions:");
            System.out.println("1. Check balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Your current balance is: $" + account.getBalance());
                    break;
                case 2:
                    System.out.print("Enter the amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();
                    if (account.withdraw(withdrawAmount)) {
                        System.out.println("Withdrawal successful. New balance: $" + account.getBalance());
                    } else {
                        System.out.println("Withdrawal failed. Insufficient funds or exceeded maximum withdrawal limit.");
                    }
                    break;
                case 3:
                    System.out.print("Enter the amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline character
                    if (account.deposit(depositAmount)) {
                        System.out.println("Deposit successful. New balance: $" + account.getBalance());
                    } else {
                        System.out.println("Deposit failed. Deposit amount exceeded the maximum balance limit.");
                    }
                    break;
                case 4:
                    saveAccountData();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void saveAccountData() {
        try {
            File file = new File(DATA_FILE);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (Account account : accounts.values()) {
                writer.write(account.getCardNumber() + " " + account.getPIN() + " " + account.getBalance());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving account data: " + e.getMessage());
        }
    }

    private static boolean isValidCardNumber(String cardNumber) {
        Pattern pattern = Pattern.compile("\\d{4}-\\d{4}-\\d{4}-\\d{4}");
        Matcher matcher = pattern.matcher(cardNumber);
        return matcher.matches();
    }
}
