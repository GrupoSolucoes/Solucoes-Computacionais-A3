package dao;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para a entidade User.
 * Responsável por todas as operações de banco de dados (CRUD) relacionadas a usuários.
 */
public class UserDAO {

    /**
     * Adiciona um novo usuário no banco de dados.
     * @param user Objeto User contendo os dados a serem inseridos.
     */
    public void addUser(User user) {
        // Comando SQL para inserção de um usuário na tabela 'users'
        String sql = "INSERT INTO users (fullName, cpf, email, username, password, role) VALUES (?, ?, ?, ?, ?, ?)";

        // Bloco try-with-resources para garantir que a conexão seja fechada
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Define os parâmetros da query com os dados do objeto User
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getCpf());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUsername());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getRole());

            // Executa o comando de inserção
            stmt.executeUpdate();

            // Recupera o ID gerado pelo banco e o atribui ao objeto User
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            // Em caso de erro, imprime o stack trace
            e.printStackTrace();
        }
    }

    /**
     * Autentica um usuário consultando o username e a senha no banco de dados.
     * @param username O nome de usuário para autenticar.
     * @param password A senha para autenticar.
     * @return um objeto User se a autenticação for bem-sucedida, ou null caso contrário.
     */
    public User authenticate(String username, String password) {
        // Query SQL para buscar um usuário com o username e senha correspondentes
        // A função TRIM() é usada para remover espaços em branco acidentais
        String sql = "SELECT * FROM users WHERE TRIM(username) = ? AND TRIM(password) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            // Executa a consulta
            ResultSet rs = stmt.executeQuery();

            // Se um resultado for encontrado, cria e retorna um objeto User
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Retorna null se nenhum usuário for encontrado ou se ocorrer um erro
        return null;
    }

    /**
     * Retorna uma lista com todos os usuários cadastrados no banco de dados.
     * @return uma List<User> contendo todos os usuários.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Itera sobre o resultado da consulta
            while (rs.next()) {
                // Cria um objeto User para cada linha e o adiciona à lista
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Retorna a lista de usuários (pode estar vazia se não houver usuários)
        return users;
    }
}