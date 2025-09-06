package view;

import controller.TaskController;
import model.Task;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TaskView extends JFrame {

    private final TaskController taskController = new TaskController();
    private final DefaultTableModel tableModel;
    private final JTable taskTable;
    // Adicionar JTextFields para os novos campos aqui...

    public TaskView() {
        setTitle("Gestão de Tarefas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Construir a interface com os novos campos...

        String[] columnNames = {"ID", "Título", "Status", "Responsável ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        add(new JScrollPane(taskTable));

        loadTasks();
    }

    // Lógica de save/update precisa ser refeita com os novos campos e construtores

    private void loadTasks() {
        tableModel.setRowCount(0);
        List<Task> tasks = taskController.getAllTasks();
        for (Task t : tasks) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getTitle(),
                    t.getStatus(),
                    t.getResponsavelId()
            });
        }
    }
}