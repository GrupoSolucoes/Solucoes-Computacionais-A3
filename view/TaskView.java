package view;

import controller.TaskController;
import model.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * Tela para gerenciar (CRUD) as Tarefas.
 * Versão com layout proporcional e design similar à TeamView e ProjectView.
 */
public class TaskView extends JFrame {

    // --- Paleta de Cores e Fontes ---
    private static final Color COR_FUNDO = new Color(245, 245, 245);
    private static final Color COR_PAINEL_FORMULARIO = Color.WHITE;
    private static final Color COR_BOTAO_SALVAR = new Color(34, 139, 34);
    private static final Color COR_BOTAO_DELETAR = new Color(178, 34, 34);
    private static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONTE_TITULO_PAINEL = new Font("Segoe UI", Font.BOLD, 16);

    private final TaskController taskController = new TaskController();
    private final DefaultTableModel tableModel;
    private final JTable taskTable;

    // Campos do Formulário
    private final JTextField txtId = new JTextField();
    private final JTextField txtTitle = new JTextField();
    private final JTextArea txtDescription = new JTextArea(3, 20);
    private final JComboBox<String> comboStatus = new JComboBox<>(new String[]{"A Fazer", "Em Andamento", "Concluída", "Bloqueada"});
    private final JComboBox<String> comboPriority = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
    private final JSpinner spinnerStartDate = new JSpinner(new SpinnerDateModel());
    private final JSpinner spinnerEndDate = new JSpinner(new SpinnerDateModel());
    private final JTextField txtProjectId = new JTextField();
    private final JTextField txtResponsibleId = new JTextField();

    public TaskView() {
        setTitle("Gestão de Tarefas");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COR_FUNDO);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(COR_FUNDO);

        // --- PAINEL DO FORMULÁRIO ---
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBackground(COR_PAINEL_FORMULARIO);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Dados da Tarefa"));
        ((javax.swing.border.TitledBorder) formPanel.getBorder()).setTitleFont(FONTE_TITULO_PAINEL);

        // Estilização dos componentes
        txtId.setEditable(false);
        txtId.setFont(FONTE_PADRAO);
        txtTitle.setFont(FONTE_PADRAO);
        txtDescription.setFont(FONTE_PADRAO);
        comboStatus.setFont(FONTE_PADRAO);
        comboPriority.setFont(FONTE_PADRAO);
        spinnerStartDate.setEditor(new JSpinner.DateEditor(spinnerStartDate, "dd/MM/yyyy"));
        spinnerEndDate.setEditor(new JSpinner.DateEditor(spinnerEndDate, "dd/MM/yyyy"));
        spinnerStartDate.setFont(FONTE_PADRAO);
        spinnerEndDate.setFont(FONTE_PADRAO);
        txtProjectId.setFont(FONTE_PADRAO);
        txtResponsibleId.setFont(FONTE_PADRAO);

        formPanel.add(createStyledLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(createStyledLabel("Título da Tarefa:"));
        formPanel.add(txtTitle);
        formPanel.add(createStyledLabel("Descrição:"));
        formPanel.add(new JScrollPane(txtDescription));
        formPanel.add(createStyledLabel("Status:"));
        formPanel.add(comboStatus);
        formPanel.add(createStyledLabel("Prioridade:"));
        formPanel.add(comboPriority);
        formPanel.add(createStyledLabel("Data de Início Prevista:"));
        formPanel.add(spinnerStartDate);
        formPanel.add(createStyledLabel("Data de Fim Prevista:"));
        formPanel.add(spinnerEndDate);
        formPanel.add(createStyledLabel("ID do Projeto:"));
        formPanel.add(txtProjectId);
        formPanel.add(createStyledLabel("ID do Responsável:"));
        formPanel.add(txtResponsibleId);

        topPanel.add(formPanel, BorderLayout.CENTER);

        // --- PAINEL DE BOTÕES ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(COR_FUNDO);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton btnSave = createStyledButton("Salvar", COR_BOTAO_SALVAR);
        JButton btnUpdate = createStyledButton("Atualizar", Color.DARK_GRAY);
        JButton btnDelete = createStyledButton("Deletar", COR_BOTAO_DELETAR);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- TABELA DE TAREFAS ---
        String[] columnNames = {"ID", "Título", "Status", "Prioridade", "Projeto ID", "Responsável ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        taskTable.setFont(FONTE_PADRAO);
        taskTable.setRowHeight(25);
        taskTable.getTableHeader().setFont(FONTE_BOTAO);

        mainPanel.add(new JScrollPane(taskTable), BorderLayout.CENTER);

        // AÇÕES
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

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONTE_PADRAO);
        return label;
    }

    private JButton createStyledButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(FONTE_BOTAO);
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        return button;
    }

    private void saveTask() {
        String taskTitle = txtTitle.getText().trim();
        if (taskTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O título da tarefa não pode estar em branco.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Task task = new Task(
                    0,
                    taskTitle,
                    txtDescription.getText(),
                    comboStatus.getSelectedItem().toString(),
                    (Date) spinnerStartDate.getValue(),
                    (Date) spinnerEndDate.getValue(),
                    null,
                    comboPriority.getSelectedItem().toString(),
                    Integer.parseInt(txtProjectId.getText()),
                    Integer.parseInt(txtResponsibleId.getText())
            );
            taskController.addTask(task);
            JOptionPane.showMessageDialog(this, "Tarefa salva com sucesso!");
            refreshView();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Projeto e do Responsável devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTask() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String taskTitle = txtTitle.getText().trim();
        if (taskTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O título da tarefa não pode estar em branco.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Task task = new Task(
                    Integer.parseInt(txtId.getText()),
                    taskTitle,
                    txtDescription.getText(),
                    comboStatus.getSelectedItem().toString(),
                    (Date) spinnerStartDate.getValue(),
                    (Date) spinnerEndDate.getValue(),
                    null,
                    comboPriority.getSelectedItem().toString(),
                    Integer.parseInt(txtProjectId.getText()),
                    Integer.parseInt(txtResponsibleId.getText())
            );
            taskController.updateTask(task);
            JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!");
            refreshView();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Projeto e do Responsável devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa na tabela para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(txtId.getText());
            int confirmation = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar esta tarefa?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                taskController.deleteTask(id);
                JOptionPane.showMessageDialog(this, "Tarefa deletada com sucesso!");
                refreshView();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID da tarefa inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
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

    private void clearForm() {
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
    }

    private void refreshView() {
        clearForm();
        loadTasks();
    }
}
