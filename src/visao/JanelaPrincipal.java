package visao;

import controle.PlanejamentoControlador;
import modelo.Despesa;
import modelo.Participante;
import modelo.PlanejamentoException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Visão (o "V" do MVC): a janela do programa.
 * Só recebe o que o usuário digita e mostra resultados e mensagens.
 * Toda regra e todo cálculo ficam no modelo; a janela fala só com o controlador.
 */
public class JanelaPrincipal extends JFrame {

    private final PlanejamentoControlador controlador;

    // ----- Aba Participantes -----
    private final JTextField campoCodigo = new JTextField(6);
    private final JTextField campoNome = new JTextField(15);
    private final JTextField campoOrcamento = new JTextField(8);
    private final DefaultTableModel modeloParticipantes =
            modeloSomenteLeitura("Código", "Nome", "Orçamento");
    private final JTable tabelaParticipantes = new JTable(modeloParticipantes);

    // ----- Aba Despesas -----
    private final JTextField campoDescricao = new JTextField(15);
    private final JTextField campoValor = new JTextField(8);
    private final JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Fixa", "Por pessoa"});
    private final JTextField campoPrioridade = new JTextField(5); // vazio = essencial
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final JList<String> listaEnvolvidos = new JList<>(modeloLista);
    private final List<Integer> codigosLista = new ArrayList<>(); // código de cada item da lista
    private final DefaultTableModel modeloDespesas =
            modeloSomenteLeitura("Descrição", "Tipo", "Classe", "Valor", "Participantes");
    private final JTable tabelaDespesas = new JTable(modeloDespesas);

    // ----- Aba Resumo -----
    private final JTextArea areaResumo = new JTextArea();

    // ----- Aba Simulador -----
    private final JTextArea areaSimulador = new JTextArea();

    /** Trecho de código que pode falhar com PlanejamentoException (usado nos botões). */
    @FunctionalInterface
    private interface Acao {
        void executar() throws PlanejamentoException;
    }

    public JanelaPrincipal(PlanejamentoControlador controlador) {
        super("Cabe no bolso - Planejador de despesas compartilhadas");
        this.controlador = controlador;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setJMenuBar(criarMenu());

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Participantes", criarAbaParticipantes());
        abas.addTab("Despesas", criarAbaDespesas());
        abas.addTab("Resumo", criarAbaResumo());
        abas.addTab("Simulador", criarAbaSimulador());
        add(abas);

        atualizarTudo();
        setSize(900, 620);
        setLocationRelativeTo(null); // centraliza na tela
    }

    // ------------------------ MONTAGEM DA TELA ------------------------

    /** Tabela que o usuário não pode editar direto (só pelos campos e botões). */
    private static DefaultTableModel modeloSomenteLeitura(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
    }

    private JMenuBar criarMenu() {
        JMenuItem salvar = new JMenuItem("Salvar planejamento");
        JMenuItem carregar = new JMenuItem("Carregar planejamento");
        JMenuItem novo = new JMenuItem("Novo planejamento (vazio)");

        // Cada item avisa o controlador; o resultado volta como atualização ou mensagem
        salvar.addActionListener(e -> executar(() -> {
            controlador.salvar();
            informar("Planejamento salvo com sucesso.");
        }));
        carregar.addActionListener(e -> executar(() -> {
            controlador.carregar();
            informar("Planejamento carregado com sucesso.");
        }));
        novo.addActionListener(e -> executar(controlador::novo));

        JMenu menu = new JMenu("Arquivo");
        menu.add(salvar);
        menu.add(carregar);
        menu.add(novo);
        JMenuBar barra = new JMenuBar();
        barra.add(menu);
        return barra;
    }

    private JPanel criarAbaParticipantes() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel linhaCampos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaCampos.add(new JLabel("Código:"));
        linhaCampos.add(campoCodigo);
        linhaCampos.add(new JLabel("Nome:"));
        linhaCampos.add(campoNome);
        linhaCampos.add(new JLabel("Orçamento (R$):"));
        linhaCampos.add(campoOrcamento);

        JButton cadastrar = new JButton("Cadastrar");
        JButton alterar = new JButton("Alterar nome e orçamento");
        JButton excluir = new JButton("Excluir");
        JPanel linhaBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaBotoes.add(cadastrar);
        linhaBotoes.add(alterar);
        linhaBotoes.add(excluir);

        // Eventos: cada clique chama o controlador
        cadastrar.addActionListener(e -> executar(() -> {
            controlador.cadastrarParticipante(
                    campoCodigo.getText(), campoNome.getText(), campoOrcamento.getText());
            limparParticipante();
        }));
        alterar.addActionListener(e -> executar(() -> {
            controlador.alterarParticipante(
                    campoCodigo.getText(), campoNome.getText(), campoOrcamento.getText());
            limparParticipante();
        }));
        excluir.addActionListener(e -> executar(() -> {
            controlador.excluirParticipante(campoCodigo.getText());
            limparParticipante();
        }));

        // Ao clicar numa linha da tabela, os dados vão para os campos
        tabelaParticipantes.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaParticipantes.getSelectedRow();
            if (!e.getValueIsAdjusting() && linha >= 0) {
                Participante p = new ArrayList<Participante>(
                        controlador.listarParticipantes()).get(linha);
                campoCodigo.setText(String.valueOf(p.getCodigo()));
                campoNome.setText(p.getNome());
                campoOrcamento.setText(PlanejamentoControlador.formatarNumero(p.getOrcamento()));
            }
        });

        JPanel topo = new JPanel(new GridLayout(2, 1));
        topo.add(linhaCampos);
        topo.add(linhaBotoes);
        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaParticipantes), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarAbaDespesas() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        campoPrioridade.setToolTipText("Deixe vazio para despesa essencial; "
                + "digite um número inteiro positivo para despesa opcional");

        JPanel linhaCampos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaCampos.add(new JLabel("Descrição:"));
        linhaCampos.add(campoDescricao);
        linhaCampos.add(new JLabel("Valor (R$):"));
        linhaCampos.add(campoValor);
        linhaCampos.add(new JLabel("Tipo:"));
        linhaCampos.add(comboTipo);
        linhaCampos.add(new JLabel("Prioridade:"));
        linhaCampos.add(campoPrioridade);

        listaEnvolvidos.setVisibleRowCount(4);
        JScrollPane rolagemLista = new JScrollPane(listaEnvolvidos);
        rolagemLista.setBorder(BorderFactory.createTitledBorder(
                "Participantes envolvidos (use Ctrl para marcar vários)"));

        JButton incluir = new JButton("Incluir");
        JButton editar = new JButton("Editar selecionada");
        JButton excluir = new JButton("Excluir selecionada");
        JPanel linhaBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaBotoes.add(incluir);
        linhaBotoes.add(editar);
        linhaBotoes.add(excluir);

        incluir.addActionListener(e -> executar(() -> {
            controlador.incluirDespesa(campoDescricao.getText(), campoValor.getText(),
                    porPessoaSelecionado(), codigosSelecionados(), campoPrioridade.getText());
            limparDespesa();
        }));
        editar.addActionListener(e -> executar(() -> {
            controlador.editarDespesa(linhaDespesaSelecionada(), campoDescricao.getText(),
                    campoValor.getText(), porPessoaSelecionado(), codigosSelecionados(),
                    campoPrioridade.getText());
            limparDespesa();
        }));
        excluir.addActionListener(e -> executar(() -> {
            controlador.excluirDespesa(linhaDespesaSelecionada());
            limparDespesa();
        }));

        // Ao clicar numa despesa da tabela, os dados vão para os campos
        tabelaDespesas.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaDespesas.getSelectedRow();
            if (!e.getValueIsAdjusting() && linha >= 0) {
                Despesa d = controlador.listarDespesas().get(linha);
                campoDescricao.setText(d.getDescricao());
                campoValor.setText(PlanejamentoControlador.formatarNumero(d.getValor()));
                comboTipo.setSelectedIndex(controlador.tipoDe(d).equals("Fixa") ? 0 : 1);
                campoPrioridade.setText(d.isEssencial() ? "" : String.valueOf(d.getPrioridade()));
                listaEnvolvidos.clearSelection();
                for (Participante p : d.getEnvolvidos()) {
                    int i = codigosLista.indexOf(p.getCodigo());
                    if (i >= 0) {
                        listaEnvolvidos.addSelectionInterval(i, i);
                    }
                }
            }
        });

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(linhaCampos, BorderLayout.NORTH);
        topo.add(rolagemLista, BorderLayout.CENTER);
        topo.add(linhaBotoes, BorderLayout.SOUTH);
        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaDespesas), BorderLayout.CENTER);
        return painel;
    }

    private JScrollPane criarAbaResumo() {
        areaResumo.setEditable(false);
        areaResumo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        areaResumo.setMargin(new java.awt.Insets(10, 10, 10, 10));
        return new JScrollPane(areaResumo);
    }

    /** Aba do simulador: um botão que mostra as alternativas sem alterar o planejamento. */
    private JPanel criarAbaSimulador() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton simular = new JButton("Simular alternativas");
        simular.addActionListener(e -> executar(() ->
                areaSimulador.setText(controlador.simular())));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(simular);
        topo.add(new JLabel("Despesas sem prioridade são essenciais e nunca são retiradas."));

        areaSimulador.setEditable(false);
        areaSimulador.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        areaSimulador.setMargin(new java.awt.Insets(10, 10, 10, 10));

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(areaSimulador), BorderLayout.CENTER);
        return painel;
    }

    // --------------------------- AUXILIARES ---------------------------

    /**
     * Executa uma ação do botão. Se der certo, atualiza a tela.
     * Se o modelo recusar, mostra a mensagem e nada é alterado.
     */
    private void executar(Acao acao) {
        try {
            acao.executar();
            atualizarTudo();
        } catch (PlanejamentoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void informar(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Informação",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /** Redesenha tabelas, lista de participantes e resumo com os dados atuais. */
    private void atualizarTudo() {
        modeloParticipantes.setRowCount(0);
        modeloLista.clear();
        codigosLista.clear();
        for (Participante p : controlador.listarParticipantes()) {
            modeloParticipantes.addRow(new Object[]{p.getCodigo(), p.getNome(),
                    PlanejamentoControlador.formatarDinheiro(p.getOrcamento())});
            modeloLista.addElement(p.getCodigo() + " - " + p.getNome());
            codigosLista.add(p.getCodigo());
        }

        modeloDespesas.setRowCount(0);
        for (Despesa d : controlador.listarDespesas()) {
            List<String> nomes = new ArrayList<>();
            for (Participante p : d.getEnvolvidos()) {
                nomes.add(p.getNome());
            }
            modeloDespesas.addRow(new Object[]{d.getDescricao(), controlador.tipoDe(d),
                    controlador.classeDe(d),
                    PlanejamentoControlador.formatarDinheiro(d.getValor()),
                    String.join(", ", nomes)});
        }

        areaResumo.setText(controlador.gerarResumo());
    }

    private void limparParticipante() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoOrcamento.setText("");
    }

    private void limparDespesa() {
        campoDescricao.setText("");
        campoValor.setText("");
        campoPrioridade.setText("");
        comboTipo.setSelectedIndex(0);
        listaEnvolvidos.clearSelection();
    }

    private boolean porPessoaSelecionado() {
        return comboTipo.getSelectedIndex() == 1;
    }

    /** Códigos dos participantes marcados na lista. */
    private List<Integer> codigosSelecionados() {
        List<Integer> codigos = new ArrayList<>();
        for (int indice : listaEnvolvidos.getSelectedIndices()) {
            codigos.add(codigosLista.get(indice));
        }
        return codigos;
    }

    /** Posição da despesa escolhida na tabela; erro se nenhuma foi escolhida. */
    private int linhaDespesaSelecionada() throws PlanejamentoException {
        int linha = tabelaDespesas.getSelectedRow();
        if (linha < 0) {
            throw new PlanejamentoException("Selecione uma despesa na tabela.");
        }
        return linha;
    }
}