public class Main {
    public static void main(String[] args) {
        Account account1 = new Account("Eric", 1L, 34, 2000);
        Account account2 = new Account("Star", 2L, 34, 500);
        Account account3 = new Account("George", 3L, 34, 4000);

        Bank bank = new Bank();

        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.addAccount(account3);

        bank.addBalance(account2);

        System.out.println("Accounts in bank: " + bank.getAccountCount());
        System.out.println("Current capacity: " + bank.getCapacity());

        Long search = 1L;
        Account found = bank.findAccount(search);

        if (found != null) {
            System.out.println("\nFound account:");
            System.out.println("ID: " + found.getId());
            System.out.println("Name: " + found.getName());
            System.out.println("Age: " + found.getAge());
            System.out.println("Balance: " + found.getBalance());
        } else {
            System.out.println("\nUser was not found.");
        }
    }
}
