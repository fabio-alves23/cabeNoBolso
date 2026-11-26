package model;

// Exceção personalizada para erros nas regras do planejamento
public class PlanningException extends Exception {

    private static final long serialVersionUID = 1L;

    public PlanningException(String message) {
        super(message);
    }
}
