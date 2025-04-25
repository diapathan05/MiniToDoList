import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;

public class ToDoListGUI extends JFrame {
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskField, dueDateField, searchField;
    private JComboBox<String> priorityBox;
    private TaskManager manager;

    public ToDoListGUI() {
        setTitle("To-Do List Manager");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        manager = new TaskManager();
        manager.loadTasks();

        Color mint = new Color(152, 251, 152);
        Color dark = new Color(40, 40, 40);
        Color white = Color.WHITE;

        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        taskField = new JTextField();
        dueDateField = new JTextField("yyyy-mm-dd");
        String[] priorities = {"Low", "Medium", "High"};
        priorityBox = new JComboBox<>(priorities);
        JButton addButton = new JButton("Add");

        JPanel inputRow = new JPanel(new BorderLayout());
        inputRow.add(taskField, BorderLayout.CENTER);
        inputRow.add(addButton, BorderLayout.EAST);

        JPanel infoRow = new JPanel(new GridLayout(1, 3));
        infoRow.add(dueDateField);
        infoRow.add(priorityBox);

        searchField = new JTextField("Search...");
        infoRow.add(searchField);

        topPanel.add(inputRow);
        topPanel.add(infoRow);

        // Task list
        taskListModel = new DefaultListModel<>();
        for (Task t : manager.getTasks()) taskListModel.addElement(t);

        taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(taskList);

        // Bottom Buttons
        JPanel bottomPanel = new JPanel();
        JButton completeBtn = new JButton("Complete");
        JButton deleteBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");

        completeBtn.setBackground(mint);
        deleteBtn.setBackground(Color.PINK);
        saveBtn.setBackground(dark);
        saveBtn.setForeground(white);

        bottomPanel.add(completeBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(saveBtn);

        // Add to Frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        addButton.addActionListener(e -> {
            try {
                String desc = taskField.getText().trim();
                LocalDate date = LocalDate.parse(dueDateField.getText().trim());
                String priority = (String) priorityBox.getSelectedItem();
                if (!desc.isEmpty()) {
                    Task task = new Task(desc, date, priority);
                    taskListModel.addElement(task);
                    manager.addTask(task);
                    taskField.setText("");
                    dueDateField.setText("yyyy-mm-dd");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date!");
            }
        });

        completeBtn.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                taskListModel.get(index).markAsCompleted();
                taskList.repaint();
            }
        });

        deleteBtn.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                taskListModel.remove(index);
                manager.removeTask(index);
            }
        });

        saveBtn.addActionListener(e -> {
            manager.updateTasksFromModel(taskListModel);
            manager.saveTasks();
            JOptionPane.showMessageDialog(this, "Tasks Saved!");
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String keyword = searchField.getText().toLowerCase();
                DefaultListModel<Task> filtered = new DefaultListModel<>();
                for (Task t : manager.getTasks()) {
                    if (t.getDescription().toLowerCase().contains(keyword)) {
                        filtered.addElement(t);
                    }
                }
                taskList.setModel(filtered);
            }
        });

        setVisible(true);
    }
}
