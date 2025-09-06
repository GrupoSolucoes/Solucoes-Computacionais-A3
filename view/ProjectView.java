package view;

import controller.ProjectController;
import model.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * Tela de gerenciamento de projetos.
 * Permite criar, atualizar, deletar e listar projetos usando JTable e formulários Swing.
 * Agora com JSpinner para seleção de datas e JComboBox para Status.
 */
public class ProjectView extends JFrame {

    // Controlador que faz a comunicação com a camada DAO
    private final ProjectController projectController = new ProjectController();

    // Tabela para exibir projetos
    private final DefaultTableModel tableModel;
    private final JTable projectTable;

    // Campos de formulário
    private final JTextField txtId = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextArea txtDescription = new JTextArea(3, 20);

    // Campos de data usando JSpinner
    private final JSpinner spinnerStartDate = new JSpinner(new SpinnerDateModel());
    private final JSpinner spinnerEndDatePrev = new JSpinner(new SpinnerDateModel());
    private final JSpinner spinnerEndDateReal = new JSpinner(new SpinnerDateModel());

    // Campo de status usando JComboBox
    private final String[] statusOptions = {"Aberto", "Pendente", "Finalizado"};
    private final JComboBox<String> comboStatus = new JComboBox<>(statusOptions);

    // Campo para ID do gerente
    private final JTextField txtManagerId = new JTextField();

    public ProjectView() {
        setTitle("Gestão de Projetos");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Formata os spinners para mostrar datas no formato dd/MM/yyyy
        spinnerStartDate.setEditor(new JSpinner.DateEditor(spinnerStartDate, "dd/MM/yyyy"));
        spinnerEndDatePrev.setEditor(new JSpinner.DateEditor(spinnerEndDatePrev, "dd/MM/yyyy"));
        spinnerEndDateReal.setEditor(new JSpinner.DateEditor(spinnerEndDateReal, "dd/MM/yyyy"));

        // Painel de formulário com GridLayout
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Projeto"));

        // Campo de ID (não editável)
        txtId.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(txtId);

        // Campos de nome e descrição
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(new JScrollPane(txtDescription));

        // Campos de datas com JSpinner
        formPanel.add(new JLabel("Data Início:"));
        formPanel.add(spinnerStartDate);

        formPanel.add(new JLabel("Data Fim Prevista:"));
        formPanel.add(spinnerEndDatePrev);

        formPanel.add(new JLabel("Data Fim Real:"));
        formPanel.add(spinnerEndDateReal);

        // Campo de status com JComboBox
        formPanel.add(new JLabel("Status:"));
        formPanel.add(comboStatus);

        // Campo para ID do gerente
        formPanel.add(new JLabel("ID do Gerente:"));
        formPanel.add(txtManagerId);

        add(formPanel, BorderLayout.NORTH);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSave = new JButton("Salvar");
        JButton btnUpdate = new JButton("Atualizar");
        JButton btnDelete = new JButton("Deletar");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.CENTER);

        // Configuração da tabela de projetos
        String[] columnNames = {"ID", "Nome", "Status", "Gerente ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        projectTable = new JTable(tableModel);
        add(new JScrollPane(projectTable), BorderLayout.SOUTH);

        // Ações dos botões
        btnSave.addActionListener(e -> saveProject());
        btnUpdate.addActionListener(e -> updateProject());
        btnDelete.addActionListener(e -> deleteProject());

        // Carrega projetos existentes no início
        loadProjects();
    }

    /**
     * Salva um novo projeto.
     */
    private void saveProject() {
        try {
            // Obtém as datas diretamente dos spinners
            Date startDate = (Date) spinnerStartDate.getValue();
            Date endDatePrev = (Date) spinnerEndDatePrev.getValue();
            Date endDateReal = (Date) spinnerEndDateReal.getValue();

            // Obtém o status selecionado
            String status = (String) comboStatus.getSelectedItem();

            // Cria o objeto Project (sem ID, pois será gerado pelo banco)
            Project project = new Project(
                    txtName.getText(),
                    txtDescription.getText(),
                    startDate,
                    endDatePrev,
                    endDateReal,
                    status,
                    Integer.parseInt(txtManagerId.getText())
            );

            // Tenta salvar no banco via controller
            if (projectController.addProject(project)) {
                JOptionPane.showMessageDialog(this, "Projeto salvo com sucesso!");
                loadProjects(); // atualiza tabela
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao salvar o projeto.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Gerente deve ser um número.");
        }
    }

    /**
     * Atualiza um projeto existente.
     */
    private void updateProject() {
        try {
            // Obtém as datas diretamente dos spinners
            Date startDate = (Date) spinnerStartDate.getValue();
            Date endDatePrev = (Date) spinnerEndDatePrev.getValue();
            Date endDateReal = (Date) spinnerEndDateReal.getValue();

            // Obtém o status selecionado
            String status = (String) comboStatus.getSelectedItem();

            // Cria o objeto Project usando o construtor sem ID
            Project project = new Project(
                    txtName.getText(),
                    txtDescription.getText(),
                    startDate,
                    endDatePrev,
                    endDateReal,
                    status,
                    Integer.parseInt(txtManagerId.getText())
            );

            // Define manualmente o ID para atualizar o projeto correto
            project.setId(Integer.parseInt(txtId.getText()));

            // Atualiza via controller
            if (projectController.updateProject(project)) {
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!");
                loadProjects(); // atualiza tabela
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar o projeto.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Selecione um projeto e verifique o ID do Gerente.");
        }
    }

    /**
     * Deleta um projeto selecionado.
     */
    private void deleteProject() {
        try {
            int projectId = Integer.parseInt(txtId.getText());

            // Confirmação antes de deletar
            int confirmation = JOptionPane.showConfirmDialog(this, "Tem certeza?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                if (projectController.deleteProject(projectId)) {
                    JOptionPane.showMessageDialog(this, "Projeto deletado com sucesso!");
                    loadProjects(); // atualiza tabela
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao deletar o projeto.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Selecione um projeto para deletar.");
        }
    }

    /**
     * Carrega todos os projetos do banco e exibe na tabela.
     */
    private void loadProjects() {
        tableModel.setRowCount(0); // limpa tabela
        List<Project> projects = projectController.getAllProjects();
        for (Project p : projects) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getStatus(),
                    p.getGerenteId() // ID do gerente
            });
        }
    }
}
