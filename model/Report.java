package model;

import java.util.Date;

/**
 * Classe Report representa um relatório de projeto.
 * Contém informações como tipo, data de geração, conteúdo e ID do projeto associado.
 */
public class Report {

    private int id; // ID do relatório
    private String type; // Tipo do relatório (ex: semanal, mensal)
    private Date generationDate; // Data de geração do relatório
    private String content; // Conteúdo do relatório
    private int projectId; // ID do projeto associado

    // Construtor completo
    public Report(int id, String type, Date generationDate, String content, int projectId) {
        this.id = id;
        this.type = type;
        this.generationDate = generationDate;
        this.content = content;
        this.projectId = projectId;
    }

    // Construtor sem ID (para criação antes de inserir no banco)
    public Report(String type, Date generationDate, String content, int projectId) {
        this.type = type;
        this.generationDate = generationDate;
        this.content = content;
        this.projectId = projectId;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
