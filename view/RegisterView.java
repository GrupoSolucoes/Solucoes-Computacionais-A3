package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Classe View responsável pela tela de cadastro de novos usuários.
 */
public class RegisterView extends JFrame {

    // Componentes da interface gráfica
    private final JTextField tfFullName = new JTextField();
    private final JTextField tfCpf = new JTextField(); // Campo para CPF
    private final JTextField tfEmail = new JTextField(); // Campo para Email
    private final JTextField tfUsername = new JTextField();
    private final JPasswordField pfPassword = new JPasswordField();
    private final JComboBox<String> cbRole = new JComboBox<>(new String[]{"gerente", "funcionario"});

    // Controller para intermediar as ações
    private final UserController userController = new UserController();

    /**
     * Construtor da tela de Registro.
     * Inicializa e organiza todos os componentes da interface.
     */
    public RegisterView() {
        setTitle("Cadastro de Usuário");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setLocationRelativeTo(null);

        // Define o layout como uma grade e adiciona bordas internas
        setLayout(new GridLayout(7, 2, 10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adiciona os componentes (rótulos e campos) à tela
        add(new JLabel("Nome completo:"));
        add(tfFullName);
        add(new JLabel("CPF:"));
        add(tfCpf);
        add(new JLabel("Email:"));
        add(tfEmail);
        add(new JLabel("Username:"));
        add(tfUsername);
        add(new JLabel("Senha:"));
        add(pfPassword);
        add(new JLabel("Perfil:"));
        add(cbRole);

        JButton btnRegister = new JButton("Cadastrar");
        // Define a ação a ser executada quando o botão for clicado
        btnRegister.addActionListener(e -> registerUser());
        add(new JLabel()); // Espaço em branco para alinhar o botão
        add(btnRegister);
    }

    /**
     * Método privado chamado pelo botão "Cadastrar".
     * Coleta os dados do formulário, cria um objeto User e o envia para o Controller.
     */
    private void registerUser() {
        // Coleta os dados dos campos de texto
        String fullName = tfFullName.getText();
        String cpf = tfCpf.getText();
        String email = tfEmail.getText();
        String username = tfUsername.getText();
        String password = new String(pfPassword.getPassword());
        String role = cbRole.getSelectedItem().toString();

        // Cria um novo objeto User com os dados coletados
        User user = new User(fullName, cpf, email, username, password, role);

        // Envia o objeto para o controller, que se encarregará de passá-lo ao DAO
        userController.createUser(user);

        // Exibe uma mensagem de sucesso e retorna para a tela de login
        JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
        dispose(); // Fecha a tela de cadastro
        new LoginView().setVisible(true); // Abre novamente a tela de login
    }
}