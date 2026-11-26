> [!WARNING]
> **AVISO PARA A EQUIPE: REMOVER NO REPOSITÓRIO FINAL**
>
> Este é o repositório de **colaboração**, e **não** a versão de entrega ao professor.
> Para ver o que já foi feito e o que falta, abra o arquivo [CLIQUE-AQUI-PARA-VER-O-ANDAMENTO-ATUAL-DO-PROJETO.md](CLIQUE-AQUI-PARA-VER-O-ANDAMENTO-ATUAL-DO-PROJETO.md).
>
> **Tudo que estiver marcado com "REMOVER NO REPOSITÓRIO FINAL" neste README deve ser apagado antes da entrega**, inclusive este aviso.

---

# Cabe no bolso - Planejador de despesas compartilhadas

Atividade final de Programação Orientada a Objetos (Unifor).
Aplicação Java com interface Swing que organiza despesas de um passeio em grupo,
calcula quanto cada participante gasta e informa se o valor respeita o orçamento de cada um.

## Integrantes

- Alex Bonfim Barros - 2640891
- Antônio Felipe Miranda da Silva - 2641637
- Fábio Alves do Nascimento - 2641597
- Jonh Elber Silva Sousa - 2641623
- José Arôldo Maciel Neto - 2641698
- Luana Moreira Costa - 2641564

## Requisitos

- **JDK 17 ou superior** (desenvolvido e testado com JDK 25, Eclipse Temurin)
- Nenhuma dependência externa (apenas a biblioteca padrão do Java, incluindo Swing)

## Organização das pastas

```
cabe-no-bolso/
├── src/
│   ├── modelo/        Regras e cálculos (Participante, Despesa, DespesaFixa,
│   │                  DespesaPorPessoa, Planejamento, PlanejamentoException),
│   │                  simulador (Simulador, ResultadoSimulacao) e testes
│   │                  (TesteCalculos, TesteSimulador)
│   ├── visao/         Interface gráfica Swing (JanelaPrincipal)
│   ├── controle/      Controlador (PlanejamentoControlador) e ponto de partida (Main)
│   └── persistencia/  Interface Persistencia, PersistenciaArquivo e TesteArquivo
├── docs/              Diagrama de classes (UML) e capturas de tela dos testes
├── planejamento.dat   Arquivo de exemplo com o caso principal
└── README.md
```

## Compilação e execução

**Pelo IntelliJ IDEA:** abra a pasta do projeto, clique com o botão direito em
`src/controle/Main.java` e escolha *Run 'Main.main()'*.

**Pelo terminal** (na pasta do projeto):

```
javac -encoding UTF-8 -d out src/modelo/*.java src/persistencia/*.java src/controle/*.java src/visao/*.java
java -cp out controle.Main
```

## Como usar

1. Aba **Participantes**: informe código, nome e orçamento e clique em *Cadastrar*.
   Clique numa linha da tabela para alterar nome e orçamento ou excluir.
2. Aba **Despesas**: informe descrição, valor, tipo (Fixa ou Por pessoa) e marque os
   participantes envolvidos (use Ctrl para marcar vários). O campo *Prioridade* é usado
   só pelo simulador: deixe vazio para despesa essencial ou informe um inteiro positivo
   para despesa opcional.
3. Aba **Resumo**: mostra as despesas, o custo total e a situação de cada participante.
4. Aba **Simulador**: botão *Simular alternativas* (objetivo extra, descrito abaixo).
5. Menu **Arquivo**: *Salvar*, *Carregar* ou iniciar um *Novo* planejamento vazio.

Os valores são digitados em reais (ex.: `70,00`) e tratados internamente em centavos.

## Como reproduzir os testes

**Roteiro para cadastrar o caso principal (pela janela):**

| Código | Participante | Orçamento |
|---|---|---|
| 1 | Ana | R$ 70,00 |
| 2 | Bruno | R$ 60,00 |
| 3 | Carla | R$ 70,00 |

| Despesa | Tipo | Valor | Participantes |
|---|---|---|---|
| Transporte | Fixa | R$ 90,00 | Ana, Bruno e Carla |
| Almoço | Por pessoa | R$ 20,00 | Ana, Bruno e Carla |
| Atividade recreativa | Fixa | R$ 30,00 | Ana, Bruno e Carla |
| Sobremesa | Por pessoa | R$ 15,00 | Ana e Carla |

**Testes automatizados (programas de teste, sem tela):**

```
java -cp out modelo.TesteCalculos
java -cp out persistencia.TesteArquivo
```

O `TesteArquivo` também regrava o arquivo `planejamento.dat` com o caso principal.
Para usar o arquivo de exemplo na janela: menu *Arquivo > Carregar planejamento*.

### Tabela de testes

> [!NOTE]
> **PROVISÓRIO: REMOVER NO REPOSITÓRIO FINAL.** Os prints desta tabela ainda não foram adicionados à pasta `docs/`; os nomes abaixo são os que serão usados. Só apague este aviso quando todas as imagens estiverem em `docs/`.

| Teste | Resultado esperado | Evidência |
|---|---|---|
| Caso principal | Total R$ 210,00; Ana e Carla excedem R$ 5,00 cada; Bruno dentro do limite (R$ 60,00) | `docs/05-resumo-caso-principal.png` |
| Centavos no rateio | Despesa fixa de R$ 100,00 para os códigos 1, 2 e 3: R$ 33,34, R$ 33,33 e R$ 33,33 | `docs/01-teste-calculos.png` |
| Entrada ou vínculo inválido | Recusa despesa sem participantes, valor negativo ou campo vazio, com mensagem e sem alterar os dados | `docs/06a-erro-sem-participantes.png`, `docs/06b-erro-valor-negativo.png`, `docs/06c-erro-campo-vazio.png`, `docs/06d-sem-alteracao.png` |
| Alteração de orçamento | Orçamento da Ana em R$ 80,00: custo de R$ 75,00, dentro do limite | `docs/07-orcamento-ana.png` |
| Exclusão de despesa | Sem a atividade recreativa: total R$ 180,00 (R$ 60,00 por pessoa em média) | `docs/09-exclusao-atividade.png` |
| Salvar e reiniciar | Recupera participantes, despesas e resultados, sem duplicar registros | `docs/08a-salvar.png`, `docs/08c-carregar.png`, `docs/08d-resumo-recuperado.png` |
| Problema no arquivo | Arquivo ausente ou inválido gera mensagem; o planejamento em uso é preservado | `docs/10a-arquivo-ausente.png`, `docs/10b-arquivo-invalido.png`, `docs/02-teste-arquivo.png` |

## Modelagem

Diagrama de classes em [`docs/diagrama-classes.png`](docs/diagrama-classes.png).

- **Encapsulamento:** atributos privados, acessados por métodos que validam os dados.
- **Herança e polimorfismo:** `Despesa` é abstrata; `DespesaFixa` e `DespesaPorPessoa`
  sobrescrevem `calcularRateio()`, e o `Planejamento` usa a referência do tipo abstrato.
- **Interface de persistência:** `Persistencia` define `salvar` e `carregar`;
  `PersistenciaArquivo` implementa com serialização de objetos.
- **Coleções:** `TreeMap` para participantes (busca pelo código e ordem crescente) e
  `ArrayList` para despesas (mantém a ordem e permite editar e excluir por posição).
- **MVC:** a visão recebe entradas e mostra resultados; o controlador converte os textos
  e encaminha as ações; o modelo calcula e valida.
- **Centavos:** valores em `long` (centavos). No rateio fixo, os centavos restantes são
  distribuídos pela ordem crescente dos códigos.

## Objetivo extra: simulador de alternativas

Na aba **Simulador**, o botão *Simular alternativas* sugere quais despesas opcionais manter para que ninguém ultrapasse o orçamento, sem alterar o planejamento original.

- **Classificação:** na aba Despesas, o campo *Prioridade* vazio indica despesa **essencial**; um inteiro positivo indica despesa **opcional** com essa prioridade.
- **Algoritmo:** todas as essenciais são mantidas. Com até 10 opcionais (no máximo 1.024 combinações), o simulador testa cada combinação, descarta as que estouram o orçamento de alguém e escolhe a de maior soma de prioridades (em empate, a que mantém mais despesas).
- **Sem solução:** se as essenciais já passam do orçamento, o simulador informa o excesso de cada participante.
- **Código:** `src/modelo/Simulador.java`, `src/modelo/ResultadoSimulacao.java` e `src/modelo/TesteSimulador.java`.

**Como reproduzir:**

```
java -cp out modelo.TesteSimulador
```

Na janela: cadastre o caso principal, informe *Prioridade* 3 na atividade recreativa e 1 na sobremesa, e clique em *Simular alternativas*.

> [!NOTE]
> **PROVISÓRIO: REMOVER NO REPOSITÓRIO FINAL.** Os prints 11, 12 e 13 ainda não foram adicionados à pasta `docs/`. Só apague este aviso quando as imagens estiverem lá.

| Caso | Entrada | Resultado esperado | Evidência |
|---|---|---|---|
| Referência | Transporte e almoço essenciais; atividade (prioridade 3); sobremesa (prioridade 1) | Retira a sobremesa e mantém a atividade: R$ 60,00 para cada um, total R$ 180,00 | `docs/11-simulador-referencia.png` |
| Sem solução | Orçamento da Ana em R$ 40,00 | Sem solução: as essenciais dela custam R$ 50,00 (excesso de R$ 10,00) | `docs/12-simulador-sem-solucao.png` |
| Original preservado | Planejamento após simular | 4 despesas e R$ 210,00 | `docs/13-teste-simulador.png` |

**Observação sobre a proposta:** o texto do caso de referência cita Ana e Carla com R$ 65,00 e Bruno com R$ 50,00, que são os valores do cenário oposto (manter a sobremesa e retirar a atividade). Mantendo a atividade, o resultado correto é R$ 60,00 para cada um, com total de R$ 180,00.