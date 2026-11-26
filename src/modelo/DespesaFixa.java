package modelo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Despesa de valor fixo: o valor total é dividido entre os participantes selecionados.
 * Exemplo: transporte de R$ 90,00 para 3 pessoas = R$ 30,00 cada.
 */
public class DespesaFixa extends Despesa {
    private static final long serialVersionUID = 1L;

    public DespesaFixa(String descricao, long valor, List<Participante> envolvidos)
            throws PlanejamentoException {
        super(descricao, valor, envolvidos);
    }

    /**
     * Divide o valor entre os envolvidos.
     * Os centavos que sobram são distribuídos um a um, na ordem crescente dos códigos.
     * Exemplo: R$ 100,00 para 3 pessoas = 33,34 / 33,33 / 33,33.
     */
    @Override
    public Map<Integer, Long> calcularRateio() {
        List<Participante> lista = getEnvolvidos();   // já ordenada pelo código
        int quantidade = lista.size();
        long base = getValor() / quantidade;          // parte igual para todos
        long resto = getValor() % quantidade;         // centavos que sobraram

        Map<Integer, Long> rateio = new LinkedHashMap<>();
        for (int i = 0; i < quantidade; i++) {
            // Os primeiros "resto" participantes recebem 1 centavo a mais
            long parte = base + (i < resto ? 1 : 0);
            rateio.put(lista.get(i).getCodigo(), parte);
        }
        return rateio;
    }
}