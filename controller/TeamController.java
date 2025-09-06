package controller;

import dao.TeamDAO;
import model.Team;
import java.util.List;

/**
 * Controller para gerenciar a lógica de negócio das Equipes.
 */
public class TeamController {
    private final TeamDAO teamDAO;

    public TeamController() {
        this.teamDAO = new TeamDAO();
    }

    public void addTeam(Team team) {
        teamDAO.addTeam(team);
    }

    public void updateTeam(Team team) {
        teamDAO.updateTeam(team);
    }

    public void deleteTeam(int id) {
        teamDAO.deleteTeam(id);
    }

    public List<Team> getAllTeams() {
        return teamDAO.getAllTeams();
    }
}