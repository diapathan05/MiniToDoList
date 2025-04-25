import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

class Task {
    String name;
    String dueDate;
    String dueTime;
    String priority;

    Task(String name, String dueDate, String dueTime, String priority) {
        this.name = name;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "<html><b>" + name + "</b><br>📅 " + dueDate + " ⏰ " + dueTime + " | ⭐ " + priority + "</html>";
    }
}

public class Main {
    private static DefaultListModel<Task> taskListModel = new DefaultListModel<>();
    private static JList<Task> taskList;

    public static void main(String[] args) {
        JFrame frame = new JFrame("🌿 To-Do List Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        // 🌿 Background panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(224, 255, 238)); // Mint green
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 📋 Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBackground(new Color(224, 255, 238));

        JTextField taskField = new JTextField();
        JTextField dateField = new JTextField("yyyy-MM-dd");
        JTextField timeField = new JTextField("HH:mm");

        String[] priorities = { "Low", "Medium", "High" };
        JComboBox<String> priorityBox = new JComboBox<>(priorities);

        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Due Date (yyyy-MM-dd):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Due Time (HH:mm):"));
        inputPanel.add(timeField);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityBox);

        // 🔍 Search field
        JTextField searchField = new JTextField();
        searchField.setToolTipText("Search tasks...");
        searchField.setPreferredSize(new Dimension(100, 25));

        // ➕ Add button
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(144, 238, 144));

        // 🗃️ Task list
        taskList = new JList<>(taskListModel);
        taskList.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                label.setVerticalTextPosition(JLabel.TOP);
                label.setBorder(new EmptyBorder(5, 5, 5, 5));
                label.setBackground(isSelected ? Color.lightGray : Color.white);
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList);

        // 🧹 Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(224, 255, 238));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.PINK);
        JButton completeButton = new JButton("Complete");
        completeButton.setBackground(Color.LIGHT_GRAY);

        bottomPanel.add(completeButton);
        bottomPanel.add(deleteButton);

        // ➕ Add task logic
        addButton.addActionListener(e -> {
            String name = taskField.getText().trim();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String priority = (String) priorityBox.getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a task name.");
                return;
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                sdf.setLenient(false);
                sdf.parse(date + " " + time);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date/time format.");
                return;
            }

            taskListModel.addElement(new Task(name, date, time, priority));
            taskField.setText("");
            dateField.setText("yyyy-MM-dd");
            timeField.setText("HH:mm");
        });

        // ❌ Delete task
        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1)
                taskListModel.remove(index);
        });

        // ✅ Complete task
        completeButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                Task completed = taskListModel.get(index);
                JOptionPane.showMessageDialog(frame, "Completed: " + completed.name);
                taskListModel.remove(index);
            }
        });

        // 🔍 Search logic
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String search = searchField.getText().toLowerCase();
                DefaultListModel<Task> filteredModel = new DefaultListModel<>();
                for (int i = 0; i < taskListModel.size(); i++) {
                    Task t = taskListModel.get(i);
                    if (t.name.toLowerCase().contains(search)) {
                        filteredModel.addElement(t);
                    }
                }
                taskList.setModel(filteredModel);
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(224, 255, 238));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);
        topPanel.add(searchField, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}



