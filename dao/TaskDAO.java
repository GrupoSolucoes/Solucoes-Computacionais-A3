package dao;

import model.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO para a entidade Task.
 * Responsável pelas operações de CRUD na tabela 'tasks'.
 */
public class TaskDAO {

    /**
     * Adiciona uma nova tarefa no banco de dados.
     * @param task Objeto Task com os dados a serem inseridos.
     */
    public void addTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, status, data_inicio_prevista, data_fim_prevista, data_fim_real, prioridade, projectId, responsavel_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getStatus());
            pstmt.setDate(4, new java.sql.Date(task.getDataInicioPrevista().getTime()));
            pstmt.setDate(5, new java.sql.Date(task.getDataFimPrevista().getTime()));
            pstmt.setDate(6, task.getDataFimReal() != null ? new java.sql.Date(task.getDataFimReal().getTime()) : null);
            pstmt.setString(7, task.getPrioridade());
            pstmt.setInt(8, task.getProjectId());
            pstmt.setInt(9, task.getResponsavelId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Atualiza uma tarefa existente no banco de dados.
     * @param task Objeto Task com os dados atualizados.
     */
    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET title=?, description=?, status=?, data_inicio_prevista=?, data_fim_prevista=?, data_fim_real=?, prioridade=?, projectId=?, responsavel_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getStatus());
            pstmt.setDate(4, new java.sql.Date(task.getDataInicioPrevista().getTime()));
            pstmt.setDate(5, new java.sql.Date(task.getDataFimPrevista().getTime()));
            pstmt.setDate(6, task.getDataFimReal() != null ? new java.sql.Date(task.getDataFimReal().getTime()) : null);
            pstmt.setString(7, task.getPrioridade());
            pstmt.setInt(8, task.getProjectId());
            pstmt.setInt(9, task.getResponsavelId());
            pstmt.setInt(10, task.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deleta uma tarefa pelo seu ID.
     * @param taskId O ID da tarefa a ser deletada.
     */
    public void deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna uma lista com todas as tarefas do banco de dados.
     * @return uma List<Task>.
     */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getDate("data_inicio_prevista"),
                        rs.getDate("data_fim_prevista"),
                        rs.getDate("data_fim_real"),
                        rs.getString("prioridade"),
                        rs.getInt("projectId"),
                        rs.getInt("responsavel_id")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}