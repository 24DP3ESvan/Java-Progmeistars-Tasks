package org.example.dto;

public class UserDto {

    private final long id;
    private final String name;
    private final int age;
    private final Status status;

    public UserDto(long id, String name, int age, Status status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", status=" + status +
                '}';
    }
}