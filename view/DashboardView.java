package view;

import model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Tela principal após login.
 * Agora com todos os botões de navegação funcionais.
 */
public class DashboardView extends JFrame {

    private User loggedInUser;

    public DashboardView(User user) {
        this.loggedInUser = user;

        setTitle("Dashboard - Sistema de Gestão de Projetos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel de boas-vindas
        JLabel welcomeLabel = new JLabel(
                "Bem-vindo ao Sistema, " + loggedInUser.getFullName() + " (" + loggedInUser.getRole() + ")",
                SwingConstants.CENTER
        );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        // Painel de navegação
        JPanel navigationPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnManageProjects = new JButton("Gerenciar Projetos");
        JButton btnManageTasks = new JButton("Gerenciar Tarefas");
        JButton btnManageTeams = new JButton("Gerenciar Equipes");
        JButton btnManageReports = new JButton("Gerar Relatórios");

        navigationPanel.add(btnManageProjects);
        navigationPanel.add(btnManageTasks);
        navigationPanel.add(btnManageTeams);
        navigationPanel.add(btnManageReports);

        add(navigationPanel, BorderLayout.CENTER);

        // Ações dos botões
        btnManageProjects.addActionListener(e -> new ProjectView().setVisible(true));
        btnManageTasks.addActionListener(e -> new TaskView().setVisible(true));
        btnManageTeams.addActionListener(e -> new TeamView().setVisible(true));
        btnManageReports.addActionListener(e -> new ReportView().setVisible(true));
    }
}