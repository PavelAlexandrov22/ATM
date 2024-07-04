package ATM_machine.dto;

public class Account {
    private  String cardNumber;
    private  int PIN;
    private  double balance;

    private static final int MAX_BALANCE = 1_000_000;
    private static final int MAX_WITHDRAW = 10_000;

    public Account(String cardNumber, int PIN, double balance) {
        this.cardNumber = cardNumber;
        this.PIN = PIN;
        this.balance = balance;
    }

    public Account() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getPIN() {
        return PIN;
    }

    public double getBalance() {
        return balance;
    }
    public boolean validatePIN(int pin) {
        return this.PIN == pin;
    }
    public boolean withdraw(double amount) {
        if (amount <= balance && amount <= MAX_WITHDRAW) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean deposit(double amount) {
        if (balance + amount <= MAX_BALANCE) {
            balance += amount;
            return true;
        }
        return false;
    }

}
