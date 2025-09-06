package model;

import java.util.Date;

public class Project {
    private int id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDatePrev;
    private Date dataFimReal; // Adicionado
    private String status;
    private int gerenteId; // Adicionado

    // Construtores, Getters e Setters
    // Adicione este construtor na sua classe model/Project.java

    public Project(String name, String description, Date startDate, Date endDatePrev, Date dataFimReal, String status, int gerenteId) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDatePrev = endDatePrev;
        this.dataFimReal = dataFimReal;
        this.status = status;
        this.gerenteId = gerenteId;
    }

    // Getters e Setters para todos os campos...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDatePrev() { return endDatePrev; }
    public void setEndDatePrev(Date endDatePrev) { this.endDatePrev = endDatePrev; }
    public Date getDataFimReal() { return dataFimReal; }
    public void setDataFimReal(Date dataFimReal) { this.dataFimReal = dataFimReal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getGerenteId() { return gerenteId; }
    public void setGerenteId(int gerenteId) { this.gerenteId = gerenteId; }
}