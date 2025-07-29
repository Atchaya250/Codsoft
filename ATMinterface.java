import java.util.Scanner;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("✅ Deposit successful. New Balance: $" + balance);
        } else {
            System.out.println("❌ Invalid deposit amount.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Invalid withdrawal amount.");
            return false;
        } else if (amount > balance) {
            System.out.println("❌ Insufficient balance. Current Balance: $" + balance);
            return false;
        } else {
            balance -= amount;
            System.out.println("✅ Withdrawal successful. Remaining Balance: $" + balance);
            return true;
        }
    }
}

class ATM {
    private BankAccount account;

    public ATM(BankAccount account) {
        this.account = account;
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n===== ATM Menu =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    System.out.print("Enter deposit amount: $");
                    double depositAmount = scanner.nextDouble();
                    deposit(depositAmount);
                    break;
                case 3:
                    System.out.print("Enter withdrawal amount: $");
                    double withdrawAmount = scanner.nextDouble();
                    withdraw(withdrawAmount);
                    break;
                case 4:
                    System.out.println("👋 Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
            }

        } while (option != 4);

        scanner.close();
    }

    public void checkBalance() {
        System.out.println("💰 Your current balance is: $" + account.getBalance());
    }

    public void deposit(double amount) {
        account.deposit(amount);
    }

    public void withdraw(double amount) {
        account.withdraw(amount);
    }
}

public class ATMinterface {
    public static void main(String[] args) {
    
        BankAccount userAccount = new BankAccount(1000.0);

        ATM atm = new ATM(userAccount);

        atm.showMenu();
    }
}