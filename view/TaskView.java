package view;

import controller.TaskController;
import model.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Tela para gerenciar (CRUD) as Tarefas.
 * Versão completa e funcional.
 */
public class TaskView extends JFrame {

    private final TaskController taskController = new TaskController();
    private final DefaultTableModel tableModel;
    private final JTable taskTable;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // --- Campos do Formulário ---
    private final JTextField txtId = new JTextField();
    private final JTextField txtTitle = new JTextField();
    private final JTextArea txtDescription = new JTextArea(3, 20);
    private final JComboBox<String> comboStatus = new JComboBox<>(new String[]{"A Fazer", "Em Andamento", "Concluída"});
    private final JComboBox<String> comboPriority = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
    private final JSpinner spinnerStartDate = new JSpinner(new SpinnerDateModel());
    private final JSpinner spinnerEndDate = new JSpinner(new SpinnerDateModel());
    private final JTextField txtProjectId = new JTextField();
    private final JTextField txtResponsibleId = new JTextField();

    // --- Botões de Ação ---
    private final JButton btnSave = new JButton("Salvar");
    private final JButton btnUpdate = new JButton("Atualizar");
    private final JButton btnDelete = new JButton("Deletar");

    public TaskView() {
        setTitle("Gestão de Tarefas");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DO FORMULÁRIO (NORTE) ---
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Tarefa"));

        // Formato dos campos de data
        spinnerStartDate.setEditor(new JSpinner.DateEditor(spinnerStartDate, "dd/MM/yyyy"));
        spinnerEndDate.setEditor(new JSpinner.DateEditor(spinnerEndDate, "dd/MM/yyyy"));

        txtId.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Título:"));
        formPanel.add(txtTitle);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(new JScrollPane(txtDescription));
        formPanel.add(new JLabel("Status:"));
        formPanel.add(comboStatus);
        formPanel.add(new JLabel("Prioridade:"));
        formPanel.add(comboPriority);
        formPanel.add(new JLabel("Data de Início Prevista:"));
        formPanel.add(spinnerStartDate);
        formPanel.add(new JLabel("Data de Fim Prevista:"));
        formPanel.add(spinnerEndDate);
        formPanel.add(new JLabel("ID do Projeto:"));
        formPanel.add(txtProjectId);
        formPanel.add(new JLabel("ID do Responsável:"));
        formPanel.add(txtResponsibleId);

        add(formPanel, BorderLayout.NORTH);

        // --- PAINEL DE BOTÕES (CENTRO) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.CENTER);

        // --- TABELA DE TAREFAS (SUL) ---
        String[] columnNames = {"ID", "Título", "Status", "Prioridade", "Projeto ID", "Responsável ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        add(new JScrollPane(taskTable), BorderLayout.SOUTH);

        // --- AÇÕES (LISTENERS) ---
        btnSave.addActionListener(e -> saveTask());
        btnUpdate.addActionListener(e -> updateTask());
        btnDelete.addActionListener(e -> deleteTask());

        taskTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && taskTable.getSelectedRow() != -1) {
                populateFormFromSelectedRow();
            }
        });

        loadTasks();
    }

    private void saveTask() {
        try {
            Task task = new Task(
                    0, // ID é gerado pelo banco, então 0 por padrão
                    txtTitle.getText(),
                    txtDescription.getText(),
                    comboStatus.getSelectedItem().toString(),
                    (Date) spinnerStartDate.getValue(),
                    (Date) spinnerEndDate.getValue(),
                    null, // dataFimReal é nula na criação
                    comboPriority.getSelectedItem().toString(),
                    Integer.parseInt(txtProjectId.getText()),
                    Integer.parseInt(txtResponsibleId.getText())
            );
            taskController.addTask(task);
            JOptionPane.showMessageDialog(this, "Tarefa salva com sucesso!");
            clearFormAndReload();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Projeto e do Responsável devem ser números.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTask() {
        try {
            int id = Integer.parseInt(txtId.getText());
            Task task = new Task(
                    id,
                    txtTitle.getText(),
                    txtDescription.getText(),
                    comboStatus.getSelectedItem().toString(),
                    (Date) spinnerStartDate.getValue(),
                    (Date) spinnerEndDate.getValue(),
                    null, // Lógica para dataFimReal pode ser adicionada aqui se necessário
                    comboPriority.getSelectedItem().toString(),
                    Integer.parseInt(txtProjectId.getText()),
                    Integer.parseInt(txtResponsibleId.getText())
            );
            taskController.updateTask(task);
            JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!");
            clearFormAndReload();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTask() {
        try {
            int id = Integer.parseInt(txtId.getText());
            int confirmation = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar esta tarefa?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                taskController.deleteTask(id);
                JOptionPane.showMessageDialog(this, "Tarefa deletada com sucesso!");
                clearFormAndReload();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            // Buscando o objeto Task completo para popular as datas corretamente
            int taskId = (int) tableModel.getValueAt(selectedRow, 0);
            Task task = taskController.getAllTasks().stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);

            if (task != null) {
                txtId.setText(String.valueOf(task.getId()));
                txtTitle.setText(task.getTitle());
                txtDescription.setText(task.getDescription());
                comboStatus.setSelectedItem(task.getStatus());
                comboPriority.setSelectedItem(task.getPrioridade());
                spinnerStartDate.setValue(task.getDataInicioPrevista());
                spinnerEndDate.setValue(task.getDataFimPrevista());
                txtProjectId.setText(String.valueOf(task.getProjectId()));
                txtResponsibleId.setText(String.valueOf(task.getResponsavelId()));
            }
        }
    }

    private void loadTasks() {
        tableModel.setRowCount(0);
        List<Task> tasks = taskController.getAllTasks();
        for (Task t : tasks) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getTitle(),
                    t.getStatus(),
                    t.getPrioridade(),
                    t.getProjectId(),
                    t.getResponsavelId()
            });
        }
    }

    private void clearFormAndReload() {
        txtId.setText("");
        txtTitle.setText("");
        txtDescription.setText("");
        comboStatus.setSelectedIndex(0);
        comboPriority.setSelectedIndex(0);
        spinnerStartDate.setValue(new Date());
        spinnerEndDate.setValue(new Date());
        txtProjectId.setText("");
        txtResponsibleId.setText("");
        taskTable.clearSelection();
        loadTasks();
    }
}
