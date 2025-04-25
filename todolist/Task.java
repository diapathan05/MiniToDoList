import java.time.LocalDate;

public class Task {
    private String description;
    private boolean isCompleted;
    private LocalDate dueDate;
    private String priority;

    public Task(String description, LocalDate dueDate, String priority) {
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = false;
    }

    public String getDescription() { return description; }
    public boolean isCompleted() { return isCompleted; }
    public LocalDate getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public void markAsCompleted() { isCompleted = true; }

    @Override
    public String toString() {
        return (isCompleted ? "[✔] " : "[ ] ") + description +
               " (Due: " + dueDate + ", Priority: " + priority + ")";
    }
}
