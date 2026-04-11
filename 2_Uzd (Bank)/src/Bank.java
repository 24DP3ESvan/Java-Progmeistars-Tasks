import java.util.Arrays;

public class Bank {
    private int capacity = 100;

    private int accountCount = 0;
    private Account[] accounts;

    public Bank() {
        this.accounts = new Account[capacity];
    }

    public void addAccount(Account account){
        if (accountCount == capacity){
            extendCapacity();
        }
        accounts[accountCount] = account;
        accountCount++;
    }

    public void addBalance(Account balance){
        Account.balance += 1000;
    }

    public Account findAccount(Long AccountId) {
        for (int i = 0; i < accountCount; i++) {
            Account b = accounts[i];
            if (b.getId().equals(AccountId)) {
                return b;
            }
        }
        return null;
    }

    public int getAccountCount() {
        return accountCount;
    }

    public int getCapacity() {
        return capacity;
    }

    private void extendCapacity() {
        capacity += 100;
        accounts = Arrays.copyOf(accounts, capacity);
    }
}
