public class Account {
    private String name;
    private Long id;
    private Integer age;
    private Integer balance;

    public Account(String name, Long id, Integer age, Integer balance) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
}
