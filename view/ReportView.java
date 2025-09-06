package view;

import controller.ReportController;
import model.Report;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Tela para criar e visualizar Relatórios.
 */
public class ReportView extends JFrame {

    private final ReportController reportController = new ReportController();
    private final DefaultTableModel tableModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Campos do formulário
    private final JTextField txtType = new JTextField();
    private final JTextArea txtContent = new JTextArea(5, 20);
    private final JTextField txtProjectId = new JTextField();

    public ReportView() {
        setTitle("Gestão de Relatórios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DO FORMULÁRIO ---
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Novo Relatório"));
        formPanel.add(new JLabel("Tipo (ex: Semanal, Mensal):"));
        formPanel.add(txtType);
        formPanel.add(new JLabel("ID do Projeto Associado:"));
        formPanel.add(txtProjectId);
        formPanel.add(new JLabel("Conteúdo:"));
        formPanel.add(new JScrollPane(txtContent));

        JButton btnSave = new JButton("Gerar Relatório");
        formPanel.add(new JLabel());
        formPanel.add(btnSave);
        add(formPanel, BorderLayout.NORTH);

        // --- TABELA DE RELATÓRIOS ---
        String[] columnNames = {"ID", "Tipo", "Data de Geração", "ID do Projeto"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable reportTable = new JTable(tableModel);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        // --- AÇÕES ---
        btnSave.addActionListener(e -> saveReport());

        loadReports();
    }

    private void saveReport() {
        try {
            String type = txtType.getText();
            String content = txtContent.getText();
            int projectId = Integer.parseInt(txtProjectId.getText());

            Report report = new Report(type, new Date(), content, projectId); // Usa a data atual
            reportController.addReport(report);

            JOptionPane.showMessageDialog(this, "Relatório gerado com sucesso!");
            clearForm();
            loadReports();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Projeto deve ser um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReports() {
        tableModel.setRowCount(0);
        List<Report> reports = reportController.getAllReports();
        for (Report r : reports) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getType(),
                    dateFormat.format(r.getGenerationDate()),
                    r.getProjectId()
            });
        }
    }

    private void clearForm() {
        txtType.setText("");
        txtContent.setText("");
        txtProjectId.setText("");
    }
}