import java.util.Arrays;

public class Bank {
    private int capacity = 100;
    private int accountCount = 0;
    private Account[] accounts;

    public Bank() {
        accounts = new Account[capacity];
    }

    public void addAccount(Account account) {
        if (accountCount == capacity) {
            extendCapacity();
        }
        accounts[accountCount] = account;
        accountCount++;
    }

    public boolean addBalance(Long accountId, int amount) {
        Account account = findAccount(accountId);
        if (account != null) {
            account.addBalance(amount);
            return true;
        }
        return false;
    }

    public boolean removeAccount(Long accountId) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getId().equals(accountId)) {

                for (int j = i; j < accountCount - 1; j++) {
                    accounts[j] = accounts[j + 1];
                }

                accounts[accountCount - 1] = null;
                accountCount--;
                return true;
            }
        }
        return false;
    }

    public Account findAccount(Long accountId) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getId().equals(accountId)) {
                return accounts[i];
            }
        }
        return null;
    }

    public int getTotalBalance() {
        int total = 0;
        for (int i = 0; i < accountCount; i++) {
            total += accounts[i].getBalance();
        }
        return total;
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
