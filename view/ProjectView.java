package view;

import controller.ProjectController;
import model.Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * Tela de gerenciamento de projetos.
 * Versão com layout corrigido para campos proporcionais.
 */
public class ProjectView extends JFrame {

    // --- Paleta de Cores e Fontes ---
    private static final Color COR_FUNDO = new Color(245, 245, 245);
    private static final Color COR_PAINEL_FORMULARIO = Color.WHITE;
    private static final Color COR_BOTAO_SALVAR = new Color(34, 139, 34);
    private static final Color COR_BOTAO_DELETAR = new Color(178, 34, 34);
    private static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONTE_TITULO_PAINEL = new Font("Segoe UI", Font.BOLD, 16);

    private final ProjectController projectController = new ProjectController();
    private final DefaultTableModel tableModel;
    private final JTable projectTable;

    // Campos de formulário com tamanho preferencial
    private final JTextField txtId = new JTextField(20);
    private final JTextField txtName = new JTextField(20);
    private final JTextArea txtDescription = new JTextArea(3, 20);
    private final JSpinner spinnerStartDate = new JSpinner(new SpinnerDateModel());
    private final JSpinner spinnerEndDatePrev = new JSpinner(new SpinnerDateModel());
    private final JSpinner spinnerEndDateReal = new JSpinner(new SpinnerDateModel());
    private final JComboBox<String> comboStatus = new JComboBox<>(new String[]{"Planejado", "Em andamento", "Concluído", "Cancelado"});
    private final JTextField txtManagerId = new JTextField(20);

    public ProjectView() {
        setTitle("Gestão de Projetos");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COR_FUNDO);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(COR_FUNDO);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        formPanel.setBackground(COR_PAINEL_FORMULARIO);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Dados do Projeto"));
        ((javax.swing.border.TitledBorder) formPanel.getBorder()).setTitleFont(FONTE_TITULO_PAINEL);
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        txtId.setEditable(false);
        spinnerStartDate.setEditor(new JSpinner.DateEditor(spinnerStartDate, "dd/MM/yyyy"));
        spinnerEndDatePrev.setEditor(new JSpinner.DateEditor(spinnerEndDatePrev, "dd/MM/yyyy"));
        spinnerEndDateReal.setEditor(new JSpinner.DateEditor(spinnerEndDateReal, "dd/MM/yyyy"));

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(createStyledLabel("ID:"), gbc);
        gbc.gridx = 1; formPanel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(createStyledLabel("Nome do Projeto:"), gbc);
        gbc.gridx = 1; formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(createStyledLabel("Descrição:"), gbc);
        gbc.gridx = 1; formPanel.add(new JScrollPane(txtDescription), gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(createStyledLabel("Data de Início:"), gbc);
        gbc.gridx = 1; formPanel.add(spinnerStartDate, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(createStyledLabel("Data Fim Prevista:"), gbc);
        gbc.gridx = 1; formPanel.add(spinnerEndDatePrev, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(createStyledLabel("Data Fim Real:"), gbc);
        gbc.gridx = 1; formPanel.add(spinnerEndDateReal, gbc);

        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(createStyledLabel("Status:"), gbc);
        gbc.gridx = 1; formPanel.add(comboStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 7; formPanel.add(createStyledLabel("ID do Gerente:"), gbc);
        gbc.gridx = 1; formPanel.add(txtManagerId, gbc);

        topPanel.add(formPanel, BorderLayout.CENTER);

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

        String[] columnNames = {"ID", "Nome", "Status", "Gerente ID", "Data Fim Prevista"};
        tableModel = new DefaultTableModel(columnNames, 0);
        projectTable = new JTable(tableModel);
        projectTable.setFont(FONTE_PADRAO);
        projectTable.setRowHeight(25);
        projectTable.getTableHeader().setFont(FONTE_BOTAO);
        mainPanel.add(new JScrollPane(projectTable), BorderLayout.CENTER);

        btnSave.addActionListener(e -> saveProject());
        btnUpdate.addActionListener(e -> updateProject());
        btnDelete.addActionListener(e -> deleteProject());
        projectTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && projectTable.getSelectedRow() != -1) {
                populateFormFromSelectedRow();
            }
        });
        loadProjects();
    }

    // --- MÉTODOS AUXILIARES E DE LÓGICA (AGORA COMPLETOS) ---
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

    private void saveProject() {
        String projectName = txtName.getText().trim();
        if (projectName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do projeto não pode estar em branco.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Project project = new Project(
                    projectName,
                    txtDescription.getText(),
                    (Date) spinnerStartDate.getValue(),
                    (Date) spinnerEndDatePrev.getValue(),
                    (Date) spinnerEndDateReal.getValue(),
                    (String) comboStatus.getSelectedItem(),
                    Integer.parseInt(txtManagerId.getText())
            );
            if (projectController.addProject(project)) {
                JOptionPane.showMessageDialog(this, "Projeto salvo com sucesso!");
                refreshView();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao salvar o projeto.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Gerente deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProject() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um projeto na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String projectName = txtName.getText().trim();
        if (projectName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do projeto não pode estar em branco.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Project project = new Project(
                    projectName,
                    txtDescription.getText(),
                    (Date) spinnerStartDate.getValue(),
                    (Date) spinnerEndDatePrev.getValue(),
                    (Date) spinnerEndDateReal.getValue(),
                    (String) comboStatus.getSelectedItem(),
                    Integer.parseInt(txtManagerId.getText())
            );
            project.setId(Integer.parseInt(txtId.getText()));

            if (projectController.updateProject(project)) {
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!");
                refreshView();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar o projeto.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Gerente deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProject() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um projeto na tabela para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int projectId = Integer.parseInt(txtId.getText());
            int confirmation = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este projeto?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                if (projectController.deleteProject(projectId)) {
                    JOptionPane.showMessageDialog(this, "Projeto deletado com sucesso!");
                    refreshView();
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao deletar o projeto.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do projeto inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = projectTable.getSelectedRow();
        if (selectedRow != -1) {
            int projectId = (int) tableModel.getValueAt(selectedRow, 0);
            Project project = projectController.getAllProjects().stream()
                    .filter(p -> p.getId() == projectId)
                    .findFirst()
                    .orElse(null);

            if (project != null) {
                txtId.setText(String.valueOf(project.getId()));
                txtName.setText(project.getName());
                txtDescription.setText(project.getDescription());
                spinnerStartDate.setValue(project.getStartDate());
                spinnerEndDatePrev.setValue(project.getEndDatePrev());
                spinnerEndDateReal.setValue(project.getDataFimReal() != null ? project.getDataFimReal() : new Date());
                comboStatus.setSelectedItem(project.getStatus());
                txtManagerId.setText(String.valueOf(project.getGerenteId()));
            }
        }
    }

    private void loadProjects() {
        tableModel.setRowCount(0);
        List<Project> projects = projectController.getAllProjects();
        for (Project p : projects) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getStatus(),
                    p.getGerenteId(),
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getEndDatePrev())
            });
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtDescription.setText("");
        spinnerStartDate.setValue(new Date());
        spinnerEndDatePrev.setValue(new Date());
        spinnerEndDateReal.setValue(new Date());
        comboStatus.setSelectedIndex(0);
        txtManagerId.setText("");
        projectTable.clearSelection();
    }

    private void refreshView() {
        clearForm();
        loadProjects();
    }
}
