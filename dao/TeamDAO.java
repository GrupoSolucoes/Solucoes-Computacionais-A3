package dao;

import model.Team;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável por operações CRUD na tabela 'teams'.
 * Conecta o modelo Team ao banco de dados MySQL.
 */
public class TeamDAO {

    /**
     * Adiciona uma nova equipe no banco de dados.
     * @param team Objeto Team a ser inserido.
     */
    public void addTeam(Team team) {
        String sql = "INSERT INTO teams (name, description) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, team.getName());
            stmt.setString(2, team.getDescription());

            stmt.executeUpdate();

            // Recupera o ID gerado pelo banco
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                team.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca todas as equipes do banco de dados.
     * @return Lista de objetos Team.
     */
    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM teams";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Cria o objeto Team usando o construtor existente
                Team team = new Team(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                teams.add(team);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    /**
     * Atualiza os dados de uma equipe no banco de dados.
     * @param team Objeto Team com dados atualizados.
     */
    public void updateTeam(Team team) {
        String sql = "UPDATE teams SET name = ?, description = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, team.getName());
            stmt.setString(2, team.getDescription());
            stmt.setInt(3, team.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove uma equipe do banco de dados pelo ID.
     * @param id ID da equipe a ser removida.
     */
    public void deleteTeam(int id) {
        String sql = "DELETE FROM teams WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
