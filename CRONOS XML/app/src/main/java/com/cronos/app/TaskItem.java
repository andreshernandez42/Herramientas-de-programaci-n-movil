package com.cronos.app;

import org.json.JSONException;
import org.json.JSONObject;

public class TaskItem {
    private final long id;
    private final String title;
    private final String description;
    private final String category;
    private final String priority;
    private final String dueDate;
    private String tag;
    private boolean completed;

    public TaskItem(String title, String description, String category,
                    String priority, String dueDate, boolean completed) {
        this(System.nanoTime(), title, description, category, priority, dueDate, "", completed);
    }

    public TaskItem(long id, String title, String description, String category,
                    String priority, String dueDate, String tag, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.tag = tag == null ? "" : tag;
        this.completed = completed;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getDueDate() { return dueDate; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag == null ? "" : tag; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("title", title);
        json.put("description", description);
        json.put("category", category);
        json.put("priority", priority);
        json.put("dueDate", dueDate);
        json.put("tag", tag);
        json.put("completed", completed);
        return json;
    }

    public static TaskItem fromJson(JSONObject json) {
        return new TaskItem(
                json.optLong("id", System.nanoTime()),
                json.optString("title"),
                json.optString("description"),
                json.optString("category"),
                json.optString("priority"),
                json.optString("dueDate"),
                json.optString("tag"),
                json.optBoolean("completed")
        );
    }
}
