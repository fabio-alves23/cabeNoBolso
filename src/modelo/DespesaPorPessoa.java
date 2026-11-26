package modelo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Despesa cobrada por pessoa: o valor informado é o valor unitário,
 * e cada participante selecionado paga esse valor inteiro.
 * Exemplo: almoço de R$ 20,00 por pessoa para 3 pessoas = R$ 20,00 cada.
 */
public class DespesaPorPessoa extends Despesa {
    private static final long serialVersionUID = 1L;

    public DespesaPorPessoa(String descricao, long valor, List<Participante> envolvidos)
            throws PlanejamentoException {
        super(descricao, valor, envolvidos);
    }

    /** Cobra o valor unitário de cada envolvido. */
    @Override
    public Map<Integer, Long> calcularRateio() {
        Map<Integer, Long> rateio = new LinkedHashMap<>();
        for (Participante p : getEnvolvidos()) {
            rateio.put(p.getCodigo(), getValor());
        }
        return rateio;
    }
}