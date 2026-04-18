package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Выберите пункт: ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
                continue;
            }

            switch (choice) {
                case 1:
                    taskManager.add();
                    break;
                case 2:
                    taskManager.markDone();
                    break;
                case 3:
                    taskManager.showAll();
                    break;
                case 4:
                    taskManager.showActive();
                    break;
                case 5:
                    taskManager.saveToCsv();
                    break;
                case 6:
                    taskManager.loadFromCsv();
                    break;
                case 0:
                    System.out.println("Выход");
                    running = false;
                    break;
                default:
                    System.out.println("Нет такого пункта!");
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("1. Добавить задачу");
        System.out.println("2. Отметить выполненной");
        System.out.println("3. Показать все");
        System.out.println("4. Показать активные");
        System.out.println("5. Сохранить");
        System.out.println("6. Загрузить");
        System.out.println("0. Выход");
    }
}