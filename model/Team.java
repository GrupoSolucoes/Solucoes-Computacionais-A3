package model;

/**
 * Classe modelo que representa uma Equipe (Team)
 */
public class Team {

    private int id; // ID da equipe no banco
    private String name;
    private String description;

    // Construtor sem ID (para inserir nova equipe)
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Construtor com ID (para buscar do banco)
    public Team(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
