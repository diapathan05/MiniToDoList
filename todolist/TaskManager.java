import java.io.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.DefaultListModel;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    public void updateTasksFromModel(DefaultListModel<Task> model) {
        tasks.clear();
        for (int i = 0; i < model.size(); i++) {
            tasks.add(model.get(i));
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void saveTasks() {
        try (PrintWriter out = new PrintWriter("tasks.txt")) {
            for (Task task : tasks) {
                out.println(task.getDescription() + "|" + task.isCompleted() + "|" + task.getDueDate() + "|" + task.getPriority());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTasks() {
        File file = new File("tasks.txt");
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split("\\|");
                if (parts.length == 4) {
                    Task task = new Task(
                        parts[0],
                        LocalDate.parse(parts[2]),
                        parts[3]
                    );
                    if (Boolean.parseBoolean(parts[1])) {
                        task.markAsCompleted();
                    }
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
