package org.example;

public class Task {

    private int id;
    private String text;
    private boolean done;

    public Task(int id, String text) {
        this.id = id;
        this.text = text;
        this.done = false;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isDone() {
        return done;
    }

    public void markDone() {
        this.done = true;
    }

    @Override
    public String toString() {
        return id + ". [" + (done ? "X" : " ") + "] " + text;
    }
}