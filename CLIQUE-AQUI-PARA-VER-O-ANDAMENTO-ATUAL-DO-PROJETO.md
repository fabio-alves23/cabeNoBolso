# Andamento atual do projeto: Cabe no bolso

> Repositório de **colaboração** da equipe. **Não é o repositório de entrega** ao professor.
> **Última atualização:** 24/11/2026 | **Prazo final:** sexta, 27/11/2026, às 23h59 (faltam 3 dias).
> **Legenda:** `[x]` feito | `[ ]` falta.
> **Leia primeiro:** o `README.md` explica como instalar, rodar e testar o programa. Este arquivo mostra **em que ponto o trabalho está**, **o que existe em cada pasta**, **o que será removido na entrega** e **o que falta fazer**.

## Situação em uma olhada

| Parte | Situação |
|---|---|
| Programa (código, interface, arquivos, simulador) | **Pronto e testado** |
| UML (diagrama de classes) | **Pronta e atualizada** |
| README | **Pronto** (com avisos provisórios sobre os prints) |
| GitHub de colaboração e proteção da `master` | **Pronto** |
| Prints das evidências (21 imagens) | **Falta** |
| Repositório final público | **Falta** |
| Relatório em PDF | **Falta** |
| Entrega no AVA | **Falta** (até 27/11, 23h59) |

**Neste arquivo:** 1. Resumo do trabalho · 2. Links · 3. Mapa do repositório · 4. O que remover no repositório final · 5. Regras da branch `master` · 6. Cronograma · 7. O que já foi feito · 8. O que ainda falta · 9. Como contribuir e atualizar este arquivo · 10. Registro de contribuições · 11. Problemas comuns · 12. Glossário · 13. Checklist da rubrica · 14. Ordem sugerida

---

## 1. Resumo do trabalho

Atividade final de Programação Orientada a Objetos: uma aplicação em **Java com interface Swing** que planeja despesas compartilhadas de um passeio em grupo. O programa divide os custos entre os participantes e informa se cada um respeita o seu orçamento.

- **Entrega 1:** relatório técnico em **PDF** no AVA (até 10 páginas, ABNT, uma coluna, sem capa). Cada integrante envia o **mesmo** PDF. **Sem o PDF, a nota é zero.**
- **Entrega 2:** repositório no GitHub (público), com o link na primeira página do relatório.
- **Valor:** 5,0 pontos, mais até 1,0 de bônus opcional (a nota final é limitada a 5,0).
- **Uso de IA:** autorizado pelo professor.

---

## 2. Links

- Repositório de colaboração (privado): https://github.com/JoseAroldoMacielNeto/cabe-no-bolso-colaboracao
- Repositório final (público, para o professor): **ainda não criado**.

---

## 3. Mapa do repositório (o que é cada coisa)

Este mapa vale para o repositório no GitHub e para a pasta do projeto no IntelliJ, que têm o mesmo conteúdo.

```
raiz do repositório/
├── .github/
│   └── CODEOWNERS
├── docs/
│   ├── diagrama-classes.png
│   └── (21 prints das evidências, seção 8.1)
├── src/
│   ├── modelo/
│   │   ├── Participante.java
│   │   ├── Despesa.java
│   │   ├── DespesaFixa.java
│   │   ├── DespesaPorPessoa.java
│   │   ├── Planejamento.java
│   │   ├── PlanejamentoException.java
│   │   ├── Simulador.java
│   │   ├── ResultadoSimulacao.java
│   │   ├── TesteCalculos.java
│   │   └── TesteSimulador.java
│   ├── visao/
│   │   └── JanelaPrincipal.java
│   ├── controle/
│   │   ├── PlanejamentoControlador.java
│   │   └── Main.java
│   └── persistencia/
│       ├── Persistencia.java
│       ├── PersistenciaArquivo.java
│       └── TesteArquivo.java
├── .gitignore
├── CLIQUE-AQUI-PARA-VER-O-ANDAMENTO-ATUAL-DO-PROJETO.md
├── cabe-no-bolso.iml
├── planejamento.dat
└── README.md
```

### 3.1 Arquivos soltos na raiz

| Item | O que é | Vai para o repositório final? |
|---|---|---|
| `README.md` | A "capa" do projeto: integrantes, requisitos, como compilar e rodar, roteiro de testes, tabela de testes, modelagem e simulador. É o que o professor lê primeiro. Tem **três blocos de aviso** "REMOVER NO REPOSITÓRIO FINAL" (seção 4) | Sim, **sem** os três avisos |
| `planejamento.dat` | Arquivo de exemplo com o caso principal (Ana, Bruno e Carla). É **binário** (gerado por serialização): não abre em editor de texto. Para usar: menu *Arquivo > Carregar planejamento* | Sim |
| `.gitignore` | Lista do que o Git **não** deve enviar ao GitHub (`out/`, `.idea/`, `invalido.dat` e arquivos de outros editores, como Eclipse, NetBeans e VS Code) | Sim |
| `cabe-no-bolso.iml` | Arquivo de configuração do projeto no IntelliJ, gerado automaticamente. Inofensivo | Pode ficar |
| `CLIQUE-AQUI-PARA-VER-O-ANDAMENTO-ATUAL-DO-PROJETO.md` | **Este arquivo.** Controle interno do que foi feito e do que falta | **Não** |

### 3.2 Pasta `docs/`

| Item | O que é |
|---|---|
| `diagrama-classes.png` | O diagrama de classes (UML), já com o simulador. O nome precisa ser **exatamente** esse (com "s"), porque o README aponta para ele |
| Prints `01` a `13` | As capturas de tela das evidências dos testes (seção 8.1). O README cita a maioria deles; o relatório usa todos |

### 3.3 Pasta `src/`: arquivo por arquivo

**Pacote `modelo` (o "M" do MVC: regras e cálculos)**

| Arquivo | O que faz |
|---|---|
| `Participante.java` | Uma pessoa do passeio: código único (não muda), nome e orçamento, com validações |
| `Despesa.java` | Classe **abstrata**: descrição, valor, participantes envolvidos e prioridade. Declara `calcularRateio()` |
| `DespesaFixa.java` | Divide o valor entre os participantes selecionados. Os centavos que sobram vão para os menores códigos |
| `DespesaPorPessoa.java` | Cobra o valor informado de **cada** participante selecionado |
| `Planejamento.java` | O "dono" de tudo: guarda participantes (`TreeMap`) e despesas (`ArrayList`), faz cadastro, alteração e exclusão, e calcula total individual, custo total e excedente |
| `PlanejamentoException.java` | Exceção própria, com mensagens claras. A tela mostra a mensagem ao usuário |
| `Simulador.java` | Objetivo extra: testa as combinações de despesas opcionais e escolhe a de maior prioridade que respeita os orçamentos |
| `ResultadoSimulacao.java` | Guarda o resultado do simulador: mantidas, retiradas, novos totais e excessos |
| `TesteCalculos.java` | Programa de teste (sem tela): caso principal, centavos, entradas inválidas, orçamento e exclusão |
| `TesteSimulador.java` | Programa de teste (sem tela): caso de referência, caso sem solução e original preservado |

**Pacote `visao` (o "V": a tela)**

| Arquivo | O que faz |
|---|---|
| `JanelaPrincipal.java` | A janela Swing: abas Participantes, Despesas, Resumo e Simulador, e menu Arquivo (salvar, carregar e novo). Não calcula nada: só recebe entradas e mostra resultados |

**Pacote `controle` (o "C": o intermediário)**

| Arquivo | O que faz |
|---|---|
| `PlanejamentoControlador.java` | Converte o que é digitado, encaminha ao modelo e devolve resultado ou mensagem de erro |
| `Main.java` | Ponto de partida: monta as três partes do MVC e abre a janela |

**Pacote `persistencia` (salvar e carregar)**

| Arquivo | O que faz |
|---|---|
| `Persistencia.java` | Interface (o "contrato"): `salvar` e `carregar` |
| `PersistenciaArquivo.java` | Cumpre o contrato gravando o objeto inteiro em arquivo (serialização) |
| `TesteArquivo.java` | Programa de teste (sem tela): salvar e recuperar, arquivo ausente e arquivo inválido |

### 3.4 Itens que existem só neste repositório de colaboração

| Item | O que é e para que serve |
|---|---|
| `.github/` | Pasta de **configurações do próprio GitHub**. Não faz parte do programa Java. O GitHub procura nela arquivos especiais, como o `CODEOWNERS` |
| `.github/CODEOWNERS` | Define quem é o **responsável por revisar** as mudanças. Seu conteúdo é `* @JoseAroldoMacielNeto`: o `*` significa "todos os arquivos", seguido do usuário do dono. Junto com a regra de proteção da `master` (seção 5), faz o GitHub exigir a revisão do dono em todo Pull Request |
| `CLIQUE-AQUI-PARA-VER-O-ANDAMENTO-ATUAL-DO-PROJETO.md` | Este arquivo |
| Três blocos de aviso no `README.md` | Um no topo (aponta para este arquivo) e dois **provisórios** sobre prints ainda não adicionados. Todos começam com "REMOVER NO REPOSITÓRIO FINAL" |

### 3.5 Itens que existem só no computador de cada um (o Git não envia)

| Item | O que é |
|---|---|
| `.idea/` | Configurações pessoais do IntelliJ (janelas abertas, preferências) |
| `out/` | Arquivos compilados (`.class`), gerados automaticamente a cada execução |
| `invalido.dat` | Arquivo de mentira, criado pelo `TesteArquivo` para testar o tratamento de arquivo inválido |

---

## 4. O que remover no repositório final

O repositório final é uma **cópia limpa** do projeto, que vai para o professor. Tudo que serve só à colaboração da equipe precisa sair dela.

### 4.1 Lista de remoção

| O que remover | Onde fica | Como remover |
|---|---|---|
| Pasta `.git` (histórico de colaboração) | Raiz da cópia (pasta oculta) | Apagar a pasta na cópia. O repositório final começa com histórico limpo |
| Pasta `.github` (com o `CODEOWNERS`) | Raiz da cópia | Apagar a pasta |
| `CLIQUE-AQUI-PARA-VER-O-ANDAMENTO-ATUAL-DO-PROJETO.md` | Raiz da cópia | Apagar o arquivo |
| **Aviso 1 do README:** caixa amarela do topo ("AVISO PARA A EQUIPE") **e a linha `---` logo abaixo dela** | Início do `README.md` | Apagar o bloco inteiro, incluindo as linhas que começam com `>` |
| **Aviso 2 do README:** caixa azul "PROVISÓRIO" acima da tabela de testes | Logo abaixo do título `### Tabela de testes` | Apagar a caixa, **só depois** de todos os prints estarem em `docs/` |
| **Aviso 3 do README:** caixa azul "PROVISÓRIO" acima da tabela do simulador | Logo acima da tabela `Caso / Entrada / Resultado esperado` | Apagar a caixa, **só depois** dos prints 11, 12 e 13 estarem em `docs/` |

### 4.2 Como achar os avisos do README

1. No IntelliJ: `Ctrl+Shift+F` e buscar por `REMOVER NO REPOSITÓRIO FINAL`. Devem aparecer **exatamente 3 resultados**.
2. Depois de apagar, buscar por `ANDAMENTO`: **não pode sobrar nenhum resultado** no README.
3. Buscar de novo por `PROVISÓRIO`: **não pode sobrar nenhum resultado**.

### 4.3 Como o README está organizado

| Parte do README | Situação |
|---|---|
| Caixa amarela do topo (e a linha `---` abaixo) | **Provisório.** Só fala deste arquivo de andamento. Sai na entrega |
| Do título `# Cabe no bolso` até o fim | **Versão final do README**, que o professor lê |
| Duas caixas azuis "PROVISÓRIO" (tabela de testes e tabela do simulador) | **Provisório.** Saem quando os prints estiverem em `docs/` |

O README **não menciona** este arquivo nem a pasta `.github` na parte final (a estrutura de pastas dele já está como deve ficar). Por isso, depois de remover os três avisos, **não é preciso editar mais nada** no texto.

### 4.4 O que continua no repositório final

`README.md` (limpo), `src/`, `docs/` (UML e os 21 prints), `planejamento.dat`, `.gitignore` e, se quiserem, `cabe-no-bolso.iml`.

---

## 5. Regras da branch `master` (proteção)

A `master` é a branch principal. Neste repositório ela está **protegida** por um *ruleset* (status: ativo), para que ninguém altere o código principal sem revisão.

- **Ninguém envia direto para a `master`:** as mudanças entram por **Pull Request**. A única exceção é o dono do repositório (*Repository admin*), que está na lista de exceções (*bypass*).
- **1 aprovação obrigatória**, e a revisão precisa vir do **Code Owner** (José Arôldo).
- **Aprovações antigas são descartadas** se novos commits forem enviados depois da aprovação.
- **Todas as conversas do Pull Request precisam estar resolvidas** antes do merge.
- **Só o método "Squash and merge"** é permitido: os commits do Pull Request viram um único commit na `master`.
- **Não é possível apagar a `master`** nem forçar o envio (*force push*).
- **Cada pessoa pode criar e mexer nas próprias branches à vontade.** A regra vale só para a `master`.

---

## 6. Cronograma e situação

- [x] 1. Combinar a equipe e as frentes de trabalho
- [x] 2. Desenhar a UML (diagrama de classes)
- [x] 3. Programar o modelo (cadastros, rateio, centavos, validações, orçamento)
- [x] 4. Testar os cálculos, sem tela
- [x] 5. Programar a persistência (salvar e carregar em arquivo)
- [x] 6. Fazer a interface Swing em MVC
- [x] 7. README e envio ao GitHub (repositório de colaboração)
- [x] 8. Objetivo extra: simulador de alternativas (código, teste e janela)
- [x] 9. UML e README atualizados com o simulador
- [x] 10. Proteção da `master` configurada (`CODEOWNERS` e regras)
- [x] 11. Avisos visíveis no README e este arquivo de andamento
- [ ] 12. Tirar os prints das evidências (21 imagens, seção 8.1)
- [ ] 13. Conferir o README final e a pasta `docs`
- [ ] 14. Criar o repositório final público e conferir
- [ ] 15. Escrever o relatório em PDF
- [ ] 16. Preencher o registro de contribuições (seção 10)
- [ ] 17. Entregar o PDF no AVA (cada integrante)

---

## 7. O que já foi feito (detalhado)

### 7.1 Ambiente

- [x] Projeto `cabe-no-bolso` no IntelliJ IDEA.
- [x] JDK 25 (Eclipse Temurin). O README informa JDK 17 ou superior.
- [x] Nenhuma biblioteca externa: só o Java padrão, incluindo o Swing.
- [x] Pacotes: `modelo`, `visao`, `controle` e `persistencia`.

### 7.2 Modelagem (UML)

- [x] Diagrama de classes feito em Mermaid (mermaid.live) e exportado em PNG.
- [x] Inclui atributos, visibilidades, operações, relações e multiplicidades.
- [x] Atualizado com `prioridade` na `Despesa` e com as classes `Simulador` e `ResultadoSimulacao`.
- [x] Imagem em `docs/diagrama-classes.png`.

### 7.3 Código (tudo funcionando e comentado)

- [x] Os 16 arquivos `.java` descritos na seção 3.3.
- [x] **Como o MVC funciona neste projeto:** a **visão** recebe o que o usuário digita e mostra resultados e mensagens; o **controlador** converte os textos e encaminha a ação; o **modelo** calcula e valida e, se recusar, lança `PlanejamentoException`, cuja mensagem a visão mostra.

### 7.4 Regras da proposta já implementadas

- [x] **Participantes:** código único, nome e orçamento; permite alterar nome e orçamento.
- [x] **Despesas:** incluir, editar e excluir, com descrição, valor, tipo e participantes envolvidos.
- [x] **Rateio:** fixo (divide entre os selecionados) e por pessoa (cobra o valor unitário de cada selecionado).
- [x] **Resumo:** despesas, custo total, total individual e situação de cada participante.
- [x] **Persistência:** salva e recupera o planejamento completo, incluindo as relações entre participantes e despesas (serialização com `serialVersionUID` e fluxos fechados por `try-with-resources`).
- [x] **Participação:** cada despesa exige ao menos um participante, sem repetição; quem não participa paga zero.
- [x] **Valores válidos:** orçamento maior ou igual a zero; despesa maior que zero; campos vazios, códigos duplicados e entradas inválidas são recusados **sem alterar o estado anterior**.
- [x] **Centavos:** valores em `long` (centavos). No rateio fixo, os centavos restantes vão para os participantes de menor código.
- [x] **Orçamento:** total igual ao limite conta como dentro do orçamento; acima dele, mostra o valor excedido e permite registrar o planejamento.
- [x] **Alterações:** os totais são recalculados na hora após editar ou excluir despesas ou alterar orçamentos.
- [x] **Exclusão de participante:** bloqueada enquanto houver despesas vinculadas.
- [x] **Coleções genéricas justificadas:** `TreeMap` para participantes (busca por código e ordem crescente) e `ArrayList` para despesas (mantém a ordem e permite editar e excluir por posição).

### 7.5 Testes executados (resultado obtido)

- [x] **Caso principal:** custo total R$ 210,00; Ana R$ 75,00 e Carla R$ 75,00 (excedem R$ 5,00 cada); Bruno R$ 60,00 (dentro do limite).
- [x] **Centavos:** despesa fixa de R$ 100,00 para 3 pessoas resulta em R$ 33,34 / R$ 33,33 / R$ 33,33.
- [x] **Entradas inválidas:** sem participantes, valor negativo e campo vazio são recusados, e as 4 despesas continuam.
- [x] **Alteração de orçamento:** com R$ 80,00, Ana mantém R$ 75,00 e fica dentro do limite.
- [x] **Exclusão de despesa:** sem a atividade recreativa, total de R$ 180,00 (Ana 65,00, Bruno 50,00 e Carla 65,00).
- [x] **Salvar e reiniciar:** 3 participantes, 4 despesas e R$ 210,00, sem duplicar registros.
- [x] **Problemas no arquivo:** arquivo ausente e arquivo inválido geram mensagem, e o planejamento em uso é preservado.
- [x] **Simulador (referência):** mantém transporte, almoço e atividade recreativa; retira a sobremesa; R$ 60,00 para cada um; total de R$ 180,00.
- [x] **Simulador (sem solução):** com Ana em R$ 40,00, informa que não há solução (excesso de R$ 10,00).
- [x] **Simulador (original preservado):** após simular, o planejamento continua com 4 despesas e R$ 210,00.
- [x] **Janela:** abre, e as operações funcionam, inclusive o simulador (testado com Ana em R$ 70,00 e em R$ 40,00).

### 7.6 Objetivo extra: simulador de alternativas

- [x] Despesas com **prioridade 0** (campo vazio na janela) são essenciais; com **prioridade maior que 0**, são opcionais.
- [x] Algoritmo: mantém todas as essenciais; testa todas as combinações de opcionais (no máximo 10 opcionais, ou seja, 1.024 combinações); descarta as que estouram o orçamento de alguém; escolhe a de maior soma de prioridades (em empate, a que mantém mais despesas).
- [x] Mostra despesas mantidas e retiradas e os novos totais, **sem alterar o planejamento original**.
- [x] Se as essenciais não couberem, informa o excesso e a ausência de solução.
- [x] Compatível com arquivos antigos: o `planejamento.dat` abre normalmente, e as despesas viram essenciais.

### 7.7 Documentação e GitHub

- [x] `README.md` completo: integrantes, requisitos, pastas, compilação, execução, uso, roteiro de testes, tabela de testes, modelagem e simulador.
- [x] `.gitignore` configurado (`out/`, `.idea/`, `invalido.dat`).
- [x] Projeto enviado ao repositório de colaboração.
- [x] `CODEOWNERS` e proteção da `master` configurados (seção 5).
- [x] Avisos "REMOVER NO REPOSITÓRIO FINAL" visíveis no README (seção 4).
- [x] Convites de colaborador enviados a 3 colegas (1 aceitou até 24/11).

### 7.8 Divergências encontradas na proposta (registrar no relatório)

**1. Valores do simulador.** No caso de referência do simulador, o texto diz que a solução (retirar a sobremesa e manter a atividade) resulta em **Ana e Carla R$ 65,00 e Bruno R$ 50,00**. Esses valores são do cenário **oposto** (manter a sobremesa e retirar a atividade). Mantendo a atividade, como o texto pede, o resultado correto é **R$ 60,00 para cada um**, com total de R$ 180,00. O total e o caso sem solução (Ana com R$ 40,00 e essenciais de R$ 50,00) conferem.

**2. Tabela do caso principal.** A tabela da proposta veio embaralhada ao ser colada. Os valores que fecham com o total de R$ 210,00 são: transporte R$ 90,00 (fixa), almoço R$ 20,00 (por pessoa), atividade R$ 30,00 (fixa) e sobremesa **R$ 15,00 por pessoa** (Ana e Carla).

---

## 8. O que ainda falta fazer (detalhado)

### 8.1 Prints das evidências (21 imagens)

Salvar todos na pasta `docs/`, com **exatamente** estes nomes. Ao terminar um print, marque a primeira coluna com `[x]`. A coluna "Usado em" mostra onde a imagem aparece: o README já cita os de "README e relatório"; os de "Relatório" entram só no PDF.

| Feito | Nº | Onde | O que mostrar | Arquivo | Usado em |
|---|---|---|---|---|---|
| [ ] | 1 | IntelliJ, aba Run | Botão direito em `TesteCalculos` > Run. Resultado inteiro (se precisar rolar, tire 2 prints) | `01-teste-calculos.png` | README e relatório |
| [ ] | 2 | IntelliJ, aba Run | Botão direito em `TesteArquivo` > Run. Resultado inteiro | `02-teste-arquivo.png` | README e relatório |
| [ ] | 3 | Janela, aba Participantes | Ana (R$ 70,00), Bruno (R$ 60,00) e Carla (R$ 70,00) cadastrados | `03-participantes.png` | Relatório |
| [ ] | 4 | Janela, aba Despesas | As 4 despesas do caso principal | `04-despesas.png` | Relatório |
| [ ] | 5 | Janela, aba Resumo | Total R$ 210,00; Ana e Carla excedendo R$ 5,00; Bruno dentro do limite | `05-resumo-caso-principal.png` | README e relatório |
| [ ] | 6a | Janela, aba Despesas | Mensagem de erro ao incluir despesa sem participante | `06a-erro-sem-participantes.png` | README e relatório |
| [ ] | 6b | Janela, aba Despesas | Mensagem de erro com valor negativo (ex.: -10,00) | `06b-erro-valor-negativo.png` | README e relatório |
| [ ] | 6c | Janela, aba Despesas | Mensagem de erro com campo vazio (ex.: descrição em branco) | `06c-erro-campo-vazio.png` | README e relatório |
| [ ] | 6d | Janela, aba Despesas | Tabela mostrando que continuam as 4 despesas | `06d-sem-alteracao.png` | README e relatório |
| [ ] | 7 | Janela, aba Resumo | Orçamento da Ana alterado para 80,00: Ana com R$ 75,00, dentro do orçamento (depois voltar para 70,00) | `07-orcamento-ana.png` | README e relatório |
| [ ] | 8a | Janela | Menu Arquivo > Salvar, mensagem de sucesso | `08a-salvar.png` | README e relatório |
| [ ] | 8b | Janela | Fechar e abrir o programa de novo: janela vazia | `08b-reiniciado.png` | Relatório |
| [ ] | 8c | Janela | Arquivo > Carregar, mensagem de sucesso | `08c-carregar.png` | README e relatório |
| [ ] | 8d | Janela, aba Resumo | Resumo igual ao caso principal, sem registros duplicados | `08d-resumo-recuperado.png` | README e relatório |
| [ ] | 9 | Janela, aba Resumo | Após excluir a atividade recreativa: total R$ 180,00 (Ana 65,00, Bruno 50,00, Carla 65,00) | `09-exclusao-atividade.png` | README e relatório |
| [ ] | 10a | Janela | Com o `planejamento.dat` renomeado: Arquivo > Carregar, mensagem "Arquivo não encontrado" | `10a-arquivo-ausente.png` | README e relatório |
| [ ] | 10b | Janela | Com o conteúdo do arquivo trocado por texto qualquer: mensagem de arquivo inválido e Resumo preservado | `10b-arquivo-invalido.png` | README e relatório |
| [ ] | 10c | Janela | Arquivo > Novo planejamento: janela vazia | `10c-planejamento-vazio.png` | Relatório |
| [ ] | 11 | Janela, aba Simulador | Caso de referência (prioridade 3 na atividade e 1 na sobremesa): sobremesa retirada, R$ 60,00 para cada | `11-simulador-referencia.png` | README e relatório |
| [ ] | 12 | Janela, aba Simulador | Ana com orçamento de 40,00: sem solução, excesso de R$ 10,00 | `12-simulador-sem-solucao.png` | README e relatório |
| [ ] | 13 | IntelliJ, aba Run | Botão direito em `TesteSimulador` > Run. Resultado inteiro | `13-teste-simulador.png` | README e relatório |

Depois do teste 10, restaurar o `planejamento.dat` original. Se a mensagem de erro sair cortada, tire o print de novo com a janela de aviso inteira.

### 8.2 README e pasta docs

- [ ] Conferir que todos os prints estão em `docs/` com os nomes acima.
- [ ] Conferir que `docs/diagrama-classes.png` é a versão atualizada (com o simulador).
- [ ] Abrir o README no GitHub e ver se as imagens e tabelas aparecem corretamente.

### 8.3 GitHub de colaboração

- [ ] Convidar os colegas que ainda faltam (*Settings > Collaborators > Add people*) e confirmar que todos aceitaram.
- [ ] Conferir que o `CODEOWNERS` contém `* @JoseAroldoMacielNeto`.

### 8.4 Repositório final (o que vai para o professor)

- [ ] Copiar a pasta do projeto já completa (com prints, UML e README).
- [ ] Na cópia, **remover tudo o que está na lista da seção 4.1** (pasta `.git`, pasta `.github`, este arquivo e os três avisos do README).
- [ ] Conferir com as buscas da seção 4.2 (`REMOVER NO REPOSITÓRIO FINAL`, `ANDAMENTO` e `PROVISÓRIO` sem resultados).
- [ ] Criar um repositório novo no GitHub, sem README, `.gitignore` ou licença.
- [ ] Enviar o projeto (commit e push).
- [ ] Deixar o repositório **Public**: Settings > General > Danger Zone > Change visibility.
- [ ] Abrir o link em **aba anônima** do navegador, para confirmar que abre sem login.
- [ ] **Teste de conferência:** baixar o projeto do GitHub em outra pasta, compilar e rodar seguindo só o README, incluindo salvar, carregar e o simulador. É o passo que mais evita surpresa na nota, porque a rubrica só dá ponto cheio ao que é verificável.

### 8.5 Relatório técnico em PDF

**Regras de formato**

- [ ] Até **10 páginas**, texto em **uma coluna**, padrão **ABNT** (citações e referências).
- [ ] **Sem capa.** A primeira página tem título, nomes completos, matrículas, objetivo e link do GitHub final.
- [ ] Figuras e tabelas **numeradas**, com legenda e explicação; capturas legíveis.

**Seções (sugestão de espaço)**

- [ ] **Apresentação** (meia página): problema escolhido, objetivo, público e escopo.
- [ ] **Modelagem e organização** (cerca de 2 páginas): UML, responsabilidades das classes, relações e MVC.
- [ ] **Implementação** (cerca de 2 páginas): funcionalidades, cálculos, validações, coleções (com justificativa) e persistência.
- [ ] **Testes e resultados** (cerca de 3 páginas): tabela com entrada, esperado, obtido e evidência; capturas; caso principal; comparação com o caso de referência; simulador.
- [ ] **Avaliação e melhorias** (cerca de 1 página): decisões de POO, dificuldades, correções, limitações e melhorias.
- [ ] **Referências:** material da disciplina, documentação do Java, Mermaid e demais fontes (citar o uso de IA, autorizado pelo professor).
- [ ] **Contribuição de cada integrante:** implementação, integração, validação e documentação (usar a seção 10).

**A discussão precisa responder**

- [ ] **Modelagem:** onde aparecem encapsulamento, herança, polimorfismo e a interface de persistência.
- [ ] **MVC:** como uma ação da tela chega ao modelo e volta como resultado ou mensagem.
- [ ] **Precisão e consistência:** como foram tratados os centavos, as alterações, os vínculos e a recuperação dos dados.
- [ ] **Usabilidade e limitações:** dificuldades e melhorias mais úteis.
- [ ] **Comparação com o caso de referência:** divergências, regra envolvida, causa e correção (usar a seção 7.8).
- [ ] **Aprendizado** ao validar o próprio cenário.

### 8.6 Entrega

- [ ] Repositório final **público** e conferido em aba anônima.
- [ ] **Cada integrante** envia o **mesmo PDF** no AVA até **sexta, 27/11, às 23h59**.

---

## 9. Como contribuir e atualizar este arquivo

### 9.1 Primeiro acesso

1. Ter conta no GitHub e **aceitar o convite** de colaborador (e-mail ou github.com/notifications).
2. No IntelliJ: **File > New > Project from Version Control**, colar a URL do repositório e clicar em **Clone**.
3. Rodar: botão direito em `src/controle/Main.java` > **Run 'Main.main()'**.

### 9.2 Fluxo para enviar uma mudança (a `master` é protegida, veja a seção 5)

1. **Criar sua branch:** menu **Git > New Branch...** e dar um nome que diga quem e o quê (ex.: `luana-prints`, `felipe-relatorio`). **Uma branch por tarefa.**
2. Fazer a alteração (por exemplo, adicionar prints em `docs/`).
3. **Marcar o que foi feito neste arquivo** (seção 9.3).
4. **Commit:** `Ctrl+K`, mensagem clara (ex.: `Adiciona prints 03 e 04`) e **Commit**. Prefira commits pequenos.
5. **Push:** `Ctrl+Shift+K` para enviar a **sua branch** ao GitHub.
6. No site do repositório, clicar em **Compare & pull request**. Preencher o título e a descrição ("O que eu fiz" e "Itens marcados") e clicar em **Create pull request**.
7. **Avisar o José Arôldo** (grupo da equipe) que o Pull Request foi aberto. Ele revisa e faz o **Squash and merge**.
8. Depois do merge: voltar para a `master` e fazer **Git > Pull** (`Ctrl+T`). Pode apagar a branch antiga.

### 9.3 Como atualizar este arquivo quando terminar algo

- Trocar `[ ]` por `[x]` no item concluído: no cronograma (seção 6), na lista do que falta (seção 8) e, para os prints, na **coluna "Feito"** da tabela 8.1.
- Atualizar a data de **Última atualização**, no topo.
- Se concluiu uma etapa inteira, atualizar também a tabela **"Situação em uma olhada"**.
- Só marcar `[x]` quando **o arquivo realmente estiver na branch**, com o nome exato combinado.
- Registrar o que você fez na tabela da **seção 10**.
- Fazer tudo isso **na sua branch** e incluir no mesmo Pull Request.

### 9.4 Boas práticas

- **Combinem no grupo quem mexe em qual arquivo.** Duas pessoas editando o mesmo arquivo ao mesmo tempo gera conflito (principalmente este arquivo e o `README.md`). Alterem só as suas linhas.
- **Antes de começar,** atualizem a `master` (**Git > Pull**) e criem a branch a partir dela.
- **Não** mexer na pasta `.github`, nem no `CODEOWNERS`, nem nas regras da `master`.
- **Não** enviar `out/`, `.idea/` nem `invalido.dat` (o `.gitignore` já cuida disso; se aparecerem na lista de commit, desmarque).
- **Não** renomear os prints: os nomes da seção 8.1 já estão escritos no README.
- **Não** apagar nem alterar os avisos "REMOVER NO REPOSITÓRIO FINAL". Quem remove é o José Arôldo, na cópia do repositório final.
- Se o Pull Request pedir correção, **conversem no próprio PR** e resolvam as conversas (a regra da `master` exige isso antes do merge).

---

## 10. Registro de contribuições

O relatório exige registrar a contribuição de cada integrante. Cada um preenche a **própria linha** (na sua branch) com o que realmente fez: implementação, integração, validação e documentação.

| Integrante | Matrícula | Implementação | Integração | Validação (testes e prints) | Documentação (README e relatório) |
|---|---|---|---|---|---|
| Alex Bonfim Barros | 2640891 | (preencher) | (preencher) | (preencher) | (preencher) |
| Antônio Felipe Miranda da Silva | 2641637 | (preencher) | (preencher) | (preencher) | (preencher) |
| Fábio Alves do Nascimento | 2641597 | (preencher) | (preencher) | (preencher) | (preencher) |
| Jonh Elber Silva Sousa | 2641623 | (preencher) | (preencher) | (preencher) | (preencher) |
| José Arôldo Maciel Neto | 2641698 | (preencher) | (preencher) | (preencher) | (preencher) |
| Luana Moreira Costa | 2641564 | (preencher) | (preencher) | (preencher) | (preencher) |

Cada integrante precisa participar da **implementação ou integração** e da **validação**, conforme a proposta.

---

## 11. Problemas comuns

| O que aparece | O que significa e o que fazer |
|---|---|
| "Commit and Push checks failed" com vários *warnings* | Só avisos de estilo do IntelliJ. Não impedem o programa. Clicar em **Commit Anyway and Push** |
| "Push rejected ... was rejected by remote" | A `master` é protegida (seção 5). Não enviar direto: criar uma branch e abrir um Pull Request (seção 9) |
| "No usages" ao lado de uma classe | Aviso do IntelliJ de que nada usa aquele item ainda. Não é erro |
| Imagem quebrada no README | O print ainda não está em `docs/` ou o nome está diferente do escrito no README |
| Erro de compilação depois de colar código | Confira se colou o arquivo **inteiro**, começando pela linha `package ...`, e se atualizou todos os arquivos pedidos antes de rodar |
| Convite do GitHub não aparece | Abrir github.com/notifications ou o e-mail do GitHub; o convite vale por 7 dias |
| "Safe delete" ao apagar um arquivo | O IntelliJ avisa que o arquivo é citado em outro lugar (ex.: o README). Confirmar com **Delete Anyway** |
| Conflito ao tentar juntar branches | Duas pessoas mexeram no mesmo trecho. Avisar o José Arôldo antes de resolver, e combinar quem mexe em qual arquivo |

---

## 12. Glossário rápido

| Termo | Significado |
|---|---|
| **Repositório** | A pasta do projeto guardada no GitHub, com todo o histórico de mudanças |
| **Clone** | Baixar uma cópia do repositório para o seu computador |
| **Commit** | Um "ponto salvo": registra as mudanças feitas, com uma mensagem |
| **Push** | Enviar seus commits do computador para o GitHub |
| **Pull** | Trazer do GitHub as mudanças dos outros para o seu computador |
| **Branch** | Uma "linha de trabalho" separada. Você mexe nela sem afetar a principal |
| **`master`** | A branch principal do projeto (protegida) |
| **Pull Request (PR)** | Pedido para juntar a sua branch à `master`, com revisão do dono |
| **Merge** | A junção da sua branch com a `master` (aqui, no formato *squash*) |
| **Code Owner** | Pessoa responsável por revisar as mudanças (definida no `CODEOWNERS`) |
| **Ruleset** | Conjunto de regras de proteção de uma branch, configurado no GitHub |
| **Bypass** | Exceção na regra: quem está na lista pode enviar direto (aqui, só o dono) |

---

## 13. Checklist da rubrica (para conferir antes de entregar)

| Critério | Valor | Onde está |
|---|---|---|
| Modelagem e conceitos de POO | 1,50 | UML atualizada; classes `Despesa` (abstrata), `DespesaFixa`, `DespesaPorPessoa`; atributos privados; interface `Persistencia` usada pelo controlador |
| Rateio e regras de negócio | 1,25 | `Planejamento`, `DespesaFixa`, `DespesaPorPessoa`; testes de centavos, limites e alterações |
| Interface e organização MVC | 1,00 | `JanelaPrincipal` (Swing, eventos e layouts), `PlanejamentoControlador`, pacotes separados |
| Coleções, exceções e arquivos | 0,75 | `TreeMap` e `ArrayList` justificados; `PlanejamentoException`; serialização com `serialVersionUID` |
| Testes, documentação e entrega | 0,50 | Prints, tabela de testes, README, relatório, contribuições e referências |
| **Bônus (simulador)** | até 1,00 | `Simulador`, `ResultadoSimulacao`, `TesteSimulador`, prints 11, 12 e 13 |

Cada subitem vale integral quando está **completo, correto e verificável**, metade quando parcial e zero quando ausente ou impossível de verificar. Por isso o teste de conferência da seção 8.4 é importante.

---

## 14. Sugestão de ordem para os próximos dias

1. **Hoje (24/11):** tirar os prints e salvar em `docs/`.
2. **25/11:** montar o repositório final, deixá-lo público e fazer o teste de conferência.
3. **26/11:** escrever o relatório e gerar o PDF.
4. **27/11:** revisar o PDF (páginas, legendas, link) e enviar no AVA **antes das 23h59**, de preferência durante o dia.