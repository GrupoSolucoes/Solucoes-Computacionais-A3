package view;

import controller.ReportController;
import model.Report;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Tela para criar e visualizar Relatórios.
 * Versão com layout proporcional e design consistente com o resto do app.
 */
public class ReportView extends JFrame {

    // --- Paleta de Cores e Fontes ---
    private static final Color COR_FUNDO = new Color(245, 245, 245);
    private static final Color COR_PAINEL_FORMULARIO = Color.WHITE;
    private static final Color COR_BOTAO_SALVAR = new Color(34, 139, 34);
    private static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONTE_TITULO_PAINEL = new Font("Segoe UI", Font.BOLD, 16);

    private final ReportController reportController = new ReportController();
    private final DefaultTableModel tableModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Campos do formulário
    private final JTextField txtType = new JTextField();
    private final JTextArea txtContent = new JTextArea(5, 20);
    private final JTextField txtProjectId = new JTextField();

    public ReportView() {
        setTitle("Gestão de Relatórios");
        setSize(800, 600);
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
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Novo Relatório"));
        ((javax.swing.border.TitledBorder) formPanel.getBorder()).setTitleFont(FONTE_TITULO_PAINEL);

        txtType.setFont(FONTE_PADRAO);
        txtContent.setFont(FONTE_PADRAO);
        txtProjectId.setFont(FONTE_PADRAO);

        formPanel.add(createStyledLabel("Tipo (ex: Semanal, Mensal):"));
        formPanel.add(txtType);
        formPanel.add(createStyledLabel("ID do Projeto Associado:"));
        formPanel.add(txtProjectId);
        formPanel.add(createStyledLabel("Conteúdo:"));
        formPanel.add(new JScrollPane(txtContent));

        topPanel.add(formPanel, BorderLayout.CENTER);

        // --- PAINEL DE BOTÕES ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(COR_FUNDO);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton btnSave = createStyledButton("Gerar Relatório", COR_BOTAO_SALVAR);
        buttonPanel.add(btnSave);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- TABELA DE RELATÓRIOS ---
        String[] columnNames = {"ID", "Tipo", "Data de Geração", "ID do Projeto"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable reportTable = new JTable(tableModel);
        reportTable.setFont(FONTE_PADRAO);
        reportTable.setRowHeight(25);
        reportTable.getTableHeader().setFont(FONTE_BOTAO);

        mainPanel.add(new JScrollPane(reportTable), BorderLayout.CENTER);

        // --- AÇÕES ---
        btnSave.addActionListener(e -> saveReport());

        loadReports();
    }

    // --- MÉTODOS AUXILIARES E DE LÓGICA ---
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

    private void saveReport() {
        String type = txtType.getText().trim();
        String content = txtContent.getText().trim();

        if (type.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tipo e Conteúdo são campos obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int projectId = Integer.parseInt(txtProjectId.getText());
            Report report = new Report(type, new Date(), content, projectId);
            reportController.addReport(report);
            JOptionPane.showMessageDialog(this, "Relatório gerado com sucesso!");
            refreshView();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Projeto deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
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

    private void refreshView() {
        clearForm();
        loadReports();
    }
}
