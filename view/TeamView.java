package view;

import controller.TeamController;
import model.Team;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela para gerenciar (CRUD) as Equipes.
 * Versão final com layout proporcional, validação e código completo.
 */
public class TeamView extends JFrame {

    // --- Paleta de Cores e Fontes ---
    private static final Color COR_FUNDO = new Color(245, 245, 245);
    private static final Color COR_PAINEL_FORMULARIO = Color.WHITE;
    private static final Color COR_BOTAO_SALVAR = new Color(34, 139, 34);
    private static final Color COR_BOTAO_DELETAR = new Color(178, 34, 34);
    private static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONTE_TITULO_PAINEL = new Font("Segoe UI", Font.BOLD, 16);

    private final TeamController teamController = new TeamController();
    private final DefaultTableModel tableModel;
    private final JTable teamTable;

    // Campos do formulário
    private final JTextField txtId = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextArea txtDescription = new JTextArea(3, 20);

    public TeamView() {
        setTitle("Gestão de Equipes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COR_FUNDO);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(COR_FUNDO);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBackground(COR_PAINEL_FORMULARIO);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Dados da Equipe"));
        ((javax.swing.border.TitledBorder) formPanel.getBorder()).setTitleFont(FONTE_TITULO_PAINEL);

        txtId.setEditable(false);
        txtId.setFont(FONTE_PADRAO);
        txtName.setFont(FONTE_PADRAO);
        txtDescription.setFont(FONTE_PADRAO);

        formPanel.add(createStyledLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(createStyledLabel("Nome:"));
        formPanel.add(txtName);
        formPanel.add(createStyledLabel("Descrição:"));
        formPanel.add(new JScrollPane(txtDescription));

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

        String[] columnNames = {"ID", "Nome", "Descrição"};
        tableModel = new DefaultTableModel(columnNames, 0);
        teamTable = new JTable(tableModel);
        teamTable.setFont(FONTE_PADRAO);
        teamTable.setRowHeight(25);
        teamTable.getTableHeader().setFont(FONTE_BOTAO);

        mainPanel.add(new JScrollPane(teamTable), BorderLayout.CENTER);

        // AÇÕES
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

    private void saveTeam() {
        String teamName = txtName.getText().trim();
        if (teamName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da equipe não pode estar em branco.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Team team = new Team(teamName, txtDescription.getText());
        teamController.addTeam(team);
        JOptionPane.showMessageDialog(this, "Equipe salva com sucesso!");
        refreshView();
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
            refreshView();
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
                refreshView();
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

    private void refreshView() {
        clearForm();
        loadTeams();
    }
}
