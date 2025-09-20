package view;

import controller.TeamController;
import model.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela para gerenciar (CRUD) as Equipes.
 * Versão corrigida com validação de entrada e código refatorado.
 */
public class TeamView extends JFrame {

    private final TeamController teamController = new TeamController();
    private final DefaultTableModel tableModel;
    private final JTable teamTable;

    // Campos do formulário
    private final JTextField txtId = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextArea txtDescription = new JTextArea(3, 20);

    private final JButton btnSave = new JButton("Salvar");
    private final JButton btnUpdate = new JButton("Atualizar");
    private final JButton btnDelete = new JButton("Deletar");

    public TeamView() {
        setTitle("Gestão de Equipes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DO FORMULÁRIO ---
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Equipe"));
        txtId.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(new JScrollPane(txtDescription));
        add(formPanel, BorderLayout.NORTH);

        // --- PAINEL DE BOTÕES ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.CENTER);

        // --- TABELA DE EQUIPES ---
        String[] columnNames = {"ID", "Nome", "Descrição"};
        tableModel = new DefaultTableModel(columnNames, 0);
        teamTable = new JTable(tableModel);
        add(new JScrollPane(teamTable), BorderLayout.SOUTH);

        // --- AÇÕES ---
        btnSave.addActionListener(e -> saveTeam());
        btnUpdate.addActionListener(e -> updateTeam());
        btnDelete.addActionListener(e -> deleteTeam());

        teamTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && teamTable.getSelectedRow() != -1) {
                populateFormFromSelectedRow();
            }
        });

        loadTeams();
    }

    private void saveTeam() {
        String teamName = txtName.getText().trim(); // .trim() remove espaços em branco

        // Validação para impedir nome em branco
        if (teamName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da equipe não pode estar em branco.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return; // Impede a execução do resto do método
        }

        Team team = new Team(teamName, txtDescription.getText());
        teamController.addTeam(team);
        JOptionPane.showMessageDialog(this, "Equipe salva com sucesso!");
        refreshView(); // Chamada unificada
    }

    private void updateTeam() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String teamName = txtName.getText().trim();

            if (teamName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome da equipe não pode estar em branco.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Team team = new Team(id, teamName, txtDescription.getText());
            teamController.updateTeam(team);
            JOptionPane.showMessageDialog(this, "Equipe atualizada com sucesso!");
            refreshView(); // Chamada unificada
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipe na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTeam() {
        try {
            int id = Integer.parseInt(txtId.getText());
            int confirmation = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar esta equipe?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                teamController.deleteTeam(id);
                JOptionPane.showMessageDialog(this, "Equipe deletada com sucesso!");
                refreshView(); // Chamada unificada
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipe na tabela para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = teamTable.getSelectedRow();
        if (selectedRow != -1) {
            txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtDescription.setText(tableModel.getValueAt(selectedRow, 2).toString());
        }
    }

    private void loadTeams() {
        tableModel.setRowCount(0);
        List<Team> teams = teamController.getAllTeams();
        for (Team t : teams) {
            tableModel.addRow(new Object[]{t.getId(), t.getName(), t.getDescription()});
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtDescription.setText("");
        teamTable.clearSelection();
    }

    /**
     * Novo método que agrupa as ações de limpar o formulário e recarregar a tabela.
     */
    private void refreshView() {
        clearForm();
        loadTeams();
    }
}
