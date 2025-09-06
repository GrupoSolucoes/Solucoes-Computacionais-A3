package controller;

import dao.ProjectDAO;
import model.Project;
import java.util.List;

/**
 * Classe Controller que gerencia a lógica de negócios dos Projetos.
 */
public class ProjectController {

    private final ProjectDAO projectDAO;

    public ProjectController() {
        this.projectDAO = new ProjectDAO();
    }

    public boolean addProject(Project project) {
        return projectDAO.addProject(project);
    }

    public boolean updateProject(Project project) {
        return projectDAO.updateProject(project);
    }

    public boolean deleteProject(int projectId) {
        return projectDAO.deleteProject(projectId);
    }

    public List<Project> getAllProjects() {
        return projectDAO.getAllProjects();
    }
}