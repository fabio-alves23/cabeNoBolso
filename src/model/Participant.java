package model;

import java.io.Serializable;

public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int code;
    private String name;
    private long budget;

    public Participant(int code, String name, long budget)
            throws PlanningException {

        if (code <= 0) {
            throw new PlanningException("O código deve ser maior que zero.");
        }

        validateName(name);
        validateBudget(budget);

        this.code = code;
        this.name = name.trim();
        this.budget = budget;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws PlanningException {
        validateName(name);
        this.name = name.trim();
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) throws PlanningException {
        validateBudget(budget);
        this.budget = budget;
    }

    // Impede nomes vazios ou nulos
    private void validateName(String name) throws PlanningException {
        if (name == null || name.trim().isEmpty()) {
            throw new PlanningException("O nome não pode estar vazio.");
        }
    }

    // O orçamento não pode ser negativo
    private void validateBudget(long budget) throws PlanningException {
        if (budget < 0) {
            throw new PlanningException("O orçamento não pode ser negativo.");
        }
    }
}
