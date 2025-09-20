package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Classe View responsável pela tela de cadastro de novos usuários com design moderno.
 */
public class RegisterView extends JFrame {

    // --- Paleta de Cores e Fontes ---
    private static final Color COR_FUNDO = new Color(245, 245, 245);
    private static final Color COR_BOTAO_PRINCIPAL = new Color(34, 139, 34); // Verde
    private static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);

    // Componentes da interface gráfica
    // Aumentei o número de colunas para dar mais largura preferencial
    private final JTextField tfFullName = new JTextField(20); // Aumentado de 15 para 20
    private final JTextField tfCpf = new JTextField(20);     // Aumentado de 15 para 20
    private final JTextField tfEmail = new JTextField(20);    // Aumentado de 15 para 20
    private final JTextField tfUsername = new JTextField(20); // Aumentado de 15 para 20
    private final JPasswordField pfPassword = new JPasswordField(20); // Aumentado de 15 para 20
    private final JComboBox<String> cbRole = new JComboBox<>(new String[]{"gerente", "colaborador"});

    private final UserController userController = new UserController();

    public RegisterView() {
        setTitle("Cadastro de Novo Usuário");
        setSize(500, 450); // Aumentei um pouco o tamanho da janela também
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- Painel Principal ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COR_FUNDO);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // --- Título (NORTE) ---
        JLabel titleLabel = new JLabel("Criar Nova Conta", SwingConstants.CENTER);
        titleLabel.setFont(FONTE_TITULO);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- Painel de Formulário (CENTRO) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Configuração para esticar os campos na horizontal
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz o componente preencher o espaço horizontal
        gbc.weightx = 1.0; // Dá peso para a coluna, fazendo-a expandir

        // Adiciona os componentes (rótulos e campos) à tela
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; // Labels não devem expandir
        formPanel.add(createStyledLabel("Nome completo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; // Campos devem expandir
        tfFullName.setFont(FONTE_PADRAO); formPanel.add(tfFullName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createStyledLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        tfCpf.setFont(FONTE_PADRAO); formPanel.add(tfCpf, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        tfEmail.setFont(FONTE_PADRAO); formPanel.add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(createStyledLabel("Usuário:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        tfUsername.setFont(FONTE_PADRAO); formPanel.add(tfUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(createStyledLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        pfPassword.setFont(FONTE_PADRAO); formPanel.add(pfPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(createStyledLabel("Perfil:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbRole.setFont(FONTE_PADRAO); formPanel.add(cbRole, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- Painel de Botões (SUL) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(COR_FUNDO);

        JButton btnRegister = createStyledButton("Cadastrar", COR_BOTAO_PRINCIPAL);
        buttonPanel.add(btnRegister);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- AÇÕES ---
        btnRegister.addActionListener(e -> registerUser());
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

    private void registerUser() {
        String fullName = tfFullName.getText().trim();
        String cpf = tfCpf.getText().trim();
        String email = tfEmail.getText().trim();
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword());
        String role = cbRole.getSelectedItem().toString();

        // Validação de campos
        if (fullName.isEmpty() || cpf.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User(fullName, cpf, email, username, password, role);
        userController.createUser(user);

        JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
        dispose();
        new LoginView().setVisible(true);
    }
}
