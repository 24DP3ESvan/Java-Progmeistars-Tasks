package org.example;

import java.io.*;
import java.util.*;

public class TaskManager {

    private List<Task> tasks = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private int nextId = 1;

    public void add() {
        System.out.print("Введите текст задачи: ");
        String text = scanner.nextLine();

        Task task = new Task(nextId++, text);
        tasks.add(task);

        System.out.println("Задача добавлена");
    }

    public void markDone() {
        System.out.print("Введите ID задачи: ");
        int id = Integer.parseInt(scanner.nextLine());

        for (Task task : tasks) {
            if (task.getId() == id) {
                task.markDone();
                System.out.println("Готово!");
                return;
            }
        }

        System.out.println("Задача не найдена");
    }

    public void showAll() {
        if (tasks.isEmpty()) {
            System.out.println("Список пуст");
            return;
        }

        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public void showActive() {
        for (Task task : tasks) {
            if (!task.isDone()) {
                System.out.println(task);
            }
        }
    }

    public void saveToCsv() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("tasks.csv"))) {

            for (Task task : tasks) {
                writer.println(task.getId() + "," + task.getText() + "," + task.isDone());
            }

            System.out.println("Сохранено");

        } catch (IOException e) {
            System.out.println("Ошибка сохранения");
        }
    }

    public void loadFromCsv() {
        tasks.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.csv"))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0]);
                String text = parts[1];
                boolean done = Boolean.parseBoolean(parts[2]);

                Task task = new Task(id, text);
                if (done) {
                    task.markDone();
                }

                tasks.add(task);

                if (id >= nextId) {
                    nextId = id + 1;
                }
            }

            System.out.println("Загружено");

        } catch (IOException e) {
            System.out.println("Ошибка загрузки");
        }
    }
}