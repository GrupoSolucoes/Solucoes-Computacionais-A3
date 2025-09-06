package dao;

import model.Report;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável por operações CRUD na tabela 'reports'.
 * Ela conecta o modelo Report ao banco de dados MySQL.
 */
public class ReportDAO {

    /**
     * Adiciona um novo relatório no banco de dados.
     * @param report Objeto Report a ser inserido.
     */
    public void addReport(Report report) {
        String sql = "INSERT INTO reports (tipo, dataGeracao, conteudo, projetoId) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, report.getType());
            stmt.setDate(2, new java.sql.Date(report.getGenerationDate().getTime()));
            stmt.setString(3, report.getContent());
            stmt.setInt(4, report.getProjectId());

            stmt.executeUpdate();

            // Recupera o ID gerado pelo banco
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                report.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca todos os relatórios do banco de dados.
     * @return Lista de objetos Report.
     */
    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM reports";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Cria um objeto Report a partir dos dados do banco
                Report report = new Report(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getDate("dataGeracao"),
                        rs.getString("conteudo"),
                        rs.getInt("projetoId")
                );
                reports.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    /**
     * Atualiza um relatório existente no banco de dados.
     * @param report Objeto Report com os dados atualizados.
     */
    public void updateReport(Report report) {
        String sql = "UPDATE reports SET tipo = ?, dataGeracao = ?, conteudo = ?, projetoId = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, report.getType());
            stmt.setDate(2, new java.sql.Date(report.getGenerationDate().getTime()));
            stmt.setString(3, report.getContent());
            stmt.setInt(4, report.getProjectId());
            stmt.setInt(5, report.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove um relatório do banco de dados.
     * @param reportId ID do relatório a ser removido.
     */
    public void deleteReport(int reportId) {
        String sql = "DELETE FROM reports WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reportId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
