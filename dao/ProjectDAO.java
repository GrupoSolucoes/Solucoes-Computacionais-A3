package dao;

import model.Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO para a entidade Project.
 * Responsável pelas operações de CRUD na tabela 'projects'.
 */
public class ProjectDAO {

    /**
     * Adiciona um novo projeto no banco de dados.
     * @param project Projeto a ser inserido.
     * @return true se a operação for bem-sucedida, false caso contrário.
     */
    public boolean addProject(Project project) {
        // SQL para inserir um novo projeto (ID é auto-gerado pelo banco)
        String sql = "INSERT INTO projects (nome, descricao, dataInicio, dataFimPrevista, data_fim_real, status, gerente_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Preenchimento dos parâmetros do PreparedStatement
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setDate(3, new java.sql.Date(project.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(project.getEndDatePrev().getTime()));
            stmt.setDate(5, project.getDataFimReal() != null ? new java.sql.Date(project.getDataFimReal().getTime()) : null);
            stmt.setString(6, project.getStatus());
            stmt.setInt(7, project.getGerenteId());

            // Executa o insert e retorna true se pelo menos uma linha foi afetada
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Atualiza um projeto existente no banco.
     * @param project Projeto com os dados atualizados.
     * @return true se a operação for bem-sucedida, false caso contrário.
     */
    public boolean updateProject(Project project) {
        // SQL para atualizar o projeto identificado pelo ID
        String sql = "UPDATE projects SET nome=?, descricao=?, dataInicio=?, dataFimPrevista=?, data_fim_real=?, status=?, gerente_id=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Preenchimento dos parâmetros
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setDate(3, new java.sql.Date(project.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(project.getEndDatePrev().getTime()));
            stmt.setDate(5, project.getDataFimReal() != null ? new java.sql.Date(project.getDataFimReal().getTime()) : null);
            stmt.setString(6, project.getStatus());
            stmt.setInt(7, project.getGerenteId());
            stmt.setInt(8, project.getId()); // ID do projeto a ser atualizado

            // Executa o update e retorna true se pelo menos uma linha foi afetada
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deleta um projeto pelo seu ID.
     * @param projectId O ID do projeto a ser deletado.
     * @return true se a operação for bem-sucedida, false caso contrário.
     */
    public boolean deleteProject(int projectId) {
        // SQL para deletar o projeto pelo ID
        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);

            // Executa o delete e retorna true se pelo menos uma linha foi afetada
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retorna uma lista com todos os projetos do banco de dados.
     * @return List<Project> com todos os projetos.
     */
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Percorre o ResultSet
            while (rs.next()) {
                // Converte java.sql.Date para java.util.Date
                java.util.Date startDate = new java.util.Date(rs.getDate("dataInicio").getTime());
                java.util.Date endDatePrev = new java.util.Date(rs.getDate("dataFimPrevista").getTime());
                java.util.Date endDateReal = rs.getDate("data_fim_real") != null ?
                        new java.util.Date(rs.getDate("data_fim_real").getTime()) : null;

                // Cria o objeto Project usando o construtor sem ID
                Project project = new Project(
                        rs.getString("nome"),       // Nome do projeto
                        rs.getString("descricao"),  // Descrição
                        startDate,                  // Data de início
                        endDatePrev,                // Data prevista de término
                        endDateReal,                // Data real de término (pode ser null)
                        rs.getString("status"),     // Status
                        rs.getInt("gerente_id")     // ID do gerente
                );

                // Define manualmente o ID do projeto
                project.setId(rs.getInt("id"));

                // Adiciona à lista
                projects.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }
}
