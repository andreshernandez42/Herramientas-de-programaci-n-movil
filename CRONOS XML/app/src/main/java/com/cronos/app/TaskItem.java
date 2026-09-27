package com.cronos.app;

public class TaskItem {
    private final String title;
    private final String description;
    private final String category;
    private final String priority;
    private final String dueDate;
    private boolean completed;

    public TaskItem(String title, String description, String category,
                    String priority, String dueDate, boolean completed) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
