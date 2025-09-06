package controller;

import dao.ReportDAO;
import model.Report;
import java.util.List;

/**
 * Controller para gerenciar a lógica de negócio dos Relatórios.
 */
public class ReportController {
    private final ReportDAO reportDAO;

    public ReportController() {
        this.reportDAO = new ReportDAO();
    }

    public void addReport(Report report) {
        reportDAO.addReport(report);
    }

    public List<Report> getAllReports() {
        return reportDAO.getAllReports();
    }
}