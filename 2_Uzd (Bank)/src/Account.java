public class Account {
    private final String name;
    private final Long id;
    private final Integer age;
    public static Integer balance;

    public Account(String name, Long id, Integer age, Integer balance) {
        this.name = name;
        this.id = id;
        this.age = age;
        Account.balance = balance;
    }

    public String getName() {
        return name;
    }

    public Long getId(){
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getBalance() {
        return balance;
    }

    public int addBalance(){
        balance += 1000;
        return balance;
    }
}



