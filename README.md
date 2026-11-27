# Cabe no Bolso — Planejador de Despesas Compartilhadas

Aplicação desktop desenvolvida em Java com interface gráfica Swing para organizar despesas compartilhadas entre participantes, controlar orçamentos e simular alternativas de gastos.

## Tecnologias utilizadas

- Java (JDK 17 ou superior)
- Java Swing (interface gráfica)
- Programação Orientada a Objetos (POO)
- Persistência de dados com serialização de objetos
- IntelliJ IDEA

O projeto não utiliza bibliotecas externas. Os recursos necessários fazem parte do próprio JDK.

## Estrutura do projeto

```text
src/
├── model/
│   ├── TripPlan.java
│   ├── Participant.java
│   ├── Expense.java
│   ├── FixedExpense.java
│   ├── PerPersonExpense.java
│   ├── PlanningException.java
│   ├── SimulationResult.java
│   └── Simulator.java
├── controller/
│   ├── Main.java
│   └── PlanningController.java
├── persistence/
│   ├── Persistence.java
│   └── FilePersistence.java
└── view/
    ├── MainWindow.java
    ├── ParticipantPanel.java
    ├── ExpensePanel.java
    ├── SummaryPanel.java
    └── SimulatorPanel.java
```

## Como executar

### Pelo IntelliJ IDEA

1. Abra o projeto no IntelliJ IDEA.
2. Verifique se o JDK está configurado.
3. Localize `src/controller/Main.java`.
4. Clique com o botão direito no arquivo.
5. Selecione **Run 'Main.main()'**.

### Pelo terminal

Na pasta principal do projeto, execute:

```bash
javac -encoding UTF-8 -d out src/model/*.java src/persistence/*.java src/controller/*.java src/view/*.java
```

Depois, execute:

```bash
java -cp out controller.Main
```

## Funcionalidades

### Participantes

- Cadastro de participantes com código gerado automaticamente.
- Registro de nome e orçamento individual.
- Edição dos dados dos participantes.
- Exclusão de participantes que não estejam vinculados a despesas.

### Despesas

- Cadastro, edição e exclusão de despesas.
- Seleção dos participantes envolvidos por meio de caixas de seleção.
- Despesas fixas: o valor é dividido entre os participantes selecionados.
- Despesas por pessoa: cada participante selecionado paga o valor integral informado.
- Validação dos dados para evitar valores inválidos e despesas sem participantes.

### Resumo financeiro

- Listagem das despesas cadastradas.
- Cálculo do custo total do planejamento.
- Cálculo do valor individual de cada participante.
- Comparação entre os custos e os orçamentos.
- Identificação de participantes dentro ou acima do orçamento.

### Simulação de despesas

- Identificação de despesas essenciais e opcionais.
- Definição de prioridades para despesas opcionais.
- Seleção da melhor combinação de despesas opcionais respeitando os orçamentos.
- Exibição das despesas mantidas e removidas.
- Apresentação dos custos após a simulação.
- Preservação do planejamento original, sem alterar as despesas cadastradas.

### Persistência de dados

- Salvamento do planejamento em arquivo.
- Carregamento dos dados salvos.
- Recuperação dos participantes, despesas e vínculos entre eles.

## Organização e conceitos de programação

O projeto utiliza o padrão MVC para separar as responsabilidades:

- **Model:** contém as classes de dados e as regras de negócio.
- **View:** contém as telas e os componentes da interface gráfica.
- **Controller:** realiza a comunicação entre a interface e as regras de negócio.
- **Persistence:** define e implementa as operações de salvamento e carregamento.

A classe abstrata `Expense` representa uma despesa de forma genérica. As classes `FixedExpense` e `PerPersonExpense` especializam seu comportamento por meio de sobrescrita de métodos, aplicando o conceito de polimorfismo.

O projeto também utiliza encapsulamento, coleções genéricas, exceções personalizadas e serialização de objetos.

## Testes realizados

Foram realizados testes manuais para verificar:

- Cadastro, edição e exclusão de participantes.
- Cadastro, edição e exclusão de despesas.
- Rateio de despesas fixas, incluindo distribuição de centavos restantes.
- Cálculo de despesas por pessoa.
- Atualização dos valores após alterações no planejamento.
- Identificação de participantes acima do orçamento.
- Salvamento e carregamento do planejamento.
- Simulação com despesas opcionais.
- Situação sem solução quando as despesas essenciais ultrapassam o orçamento.
- Preservação do planejamento original após a simulação.

As evidências dos testes devem ser disponibilizadas na documentação ou na pasta de imagens do projeto, conforme a organização da entrega.

## Limitações e melhorias futuras

- Implementar testes automatizados.
- Melhorar a apresentação visual da interface.
- Permitir exportação dos relatórios financeiros.
- Implementar suporte a múltiplos planejamentos.
- Adicionar novas opções de análise e simulação.

## Autores

Projeto acadêmico desenvolvido como atividade prática de programação em Java.
-JOSÉ ARÔLDO MACIEL NETO 
-LUANA MOREIRA COSTA 
-FÁBIO ALVES DO NASCIMENTO 
-Jonh Elber Silva SOUSA
-ALEX BONFIM BARROS 
-ANTÔNIO FELIPE MIRANDA DA SILVA 
