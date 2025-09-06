package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de login de usuários.
 */
public class LoginView extends JFrame {

    private final JTextField tfUsername = new JTextField();
    private final JPasswordField pfPassword = new JPasswordField();
    private final UserController userController = new UserController();

    public LoginView() {
        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 5, 5));

        // Adiciona um preenchimento interno à janela
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel("Username:"));
        add(tfUsername);

        add(new JLabel("Senha:"));
        add(pfPassword);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(e -> login()); // Chama o método login()
        add(btnLogin);

        JButton btnRegister = new JButton("Cadastrar");
        btnRegister.addActionListener(e -> {
            dispose(); // Fecha a tela de login
            new RegisterView().setVisible(true); // Abre a tela de cadastro
        });
        add(btnRegister);
    }

    /**
     * Método que contém a lógica de login.
     * É chamado quando o botão "Entrar" é clicado.
     */
    private void login() {
        // Converte o username para minúsculas para evitar erro de Caps Lock
        String username = tfUsername.getText().toLowerCase();
        String password = new String(pfPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userController.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido! Bem-vindo, " + user.getFullName());
            dispose(); // Fecha a tela de login
            new DashboardView(user).setVisible(true); // Abre o dashboard
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}