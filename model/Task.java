package model;

import java.util.Date;

public class Task {
    private int id;
    private String title;
    private String description;
    private String status;
    private Date dataInicioPrevista; // Adicionado
    private Date dataFimPrevista; // Adicionado
    private Date dataFimReal; // Adicionado
    private String prioridade; // Adicionado
    private int projectId;
    private int responsavelId; // Renomeado

    // Construtores, Getters e Setters
    public Task(int id, String title, String description, String status, Date dataInicioPrevista, Date dataFimPrevista, Date dataFimReal, String prioridade, int projectId, int responsavelId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dataInicioPrevista = dataInicioPrevista;
        this.dataFimPrevista = dataFimPrevista;
        this.dataFimReal = dataFimReal;
        this.prioridade = prioridade;
        this.projectId = projectId;
        this.responsavelId = responsavelId;
    }

    // Getters e Setters para todos os campos...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getDataInicioPrevista() { return dataInicioPrevista; }
    public void setDataInicioPrevista(Date dataInicioPrevista) { this.dataInicioPrevista = dataInicioPrevista; }
    public Date getDataFimPrevista() { return dataFimPrevista; }
    public void setDataFimPrevista(Date dataFimPrevista) { this.dataFimPrevista = dataFimPrevista; }
    public Date getDataFimReal() { return dataFimReal; }
    public void setDataFimReal(Date dataFimReal) { this.dataFimReal = dataFimReal; }
    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }
    public int getResponsavelId() { return responsavelId; }
    public void setResponsavelId(int responsavelId) { this.responsavelId = responsavelId; }
}