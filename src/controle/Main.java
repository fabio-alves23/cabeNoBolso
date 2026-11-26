package controle;

import persistencia.PersistenciaArquivo;
import visao.JanelaPrincipal;

import javax.swing.SwingUtilities;

/**
 * Ponto de entrada: monta as três partes do MVC e abre a janela.
 * O arquivo "planejamento.dat" fica na pasta do projeto.
 */
public class Main {
    public static void main(String[] args) {
        // Swing deve ser iniciado na thread própria da interface
        SwingUtilities.invokeLater(() -> {
            PlanejamentoControlador controlador =
                    new PlanejamentoControlador(new PersistenciaArquivo("planejamento.dat"));
            new JanelaPrincipal(controlador).setVisible(true);
        });
    }
}