# Sistema de Avaliação de Atividades Complementares

Este projeto, desenvolvido para a disciplina de Projeto de Sistemas do IFSC - Câmpus Tubarão, é uma aplicação Java de console que automatiza a avaliação de requerimentos de atividades complementares, baseando-se no regulamento da instituição.

O sistema permite que um aluno submeta suas atividades em quatro modalidades distintas (Ensino, Pesquisa e Inovação, Extensão e Complementação). Ele aplica as regras de validação em dois níveis: primeiro, no nível da atividade individual (usando limites, valores fixos ou multiplicadores) e, em seguida, no nível da modalidade (aplicando os tetos de aproveitamento percentual). Ao final, um parecer completo e formatado é gerado para o aluno.

**Componentes do Grupo:**
* Marcus Vinicius Medeiros Teixeira
* Pedro Paulo Fernandes Cardoso
* Artur Paes de Medeiros

---

## 2. Diagramas UML e Descrição

A arquitetura do sistema foi modelada para garantir baixo acoplamento e alta coesão entre os componentes. A seguir, uma descrição das principais classes e seus relacionamentos, representando o diagrama de classes do sistema.

### Descrição das Classes Principais

* **`Main`**: Ponto de entrada da aplicação. Sua única responsabilidade é instanciar e iniciar a `ConsoleUI`.
* **`ConsoleUI`**: Responsável por toda a interação com o usuário. Exibe menus, coleta dados e orquestra as chamadas para as camadas de serviço e de criação.
* **`Requerimento`**: Objeto de modelo que representa a sessão de um aluno, agregando a matrícula e a lista de todas as `AtividadeRequerida`s submetidas.
* **`Modalidade`**: Modela uma das quatro categorias de atividades (ex: "Ensino"). Contém seu nome, seu limite percentual (ex: 0.40 para 40%) e uma lista das atividades disponíveis.
* **`AtividadeComplementar`**: Define um tipo específico de atividade (ex: "Visita técnica"). Crucialmente, esta classe **contém** a sua própria regra de validação através de uma referência a `Validação`.
* **`AtividadeRequerida`**: Representa a submissão de uma atividade pelo aluno, guardando o valor que ele declarou e o resultado da validação (horas validadas e observação).
* **`AvaliacaoService`**: Serviço responsável por orquestrar a validação de uma única atividade. Ele delega o cálculo para a estratégia contida na atividade.
* **`ModalidadeFactory`**: Implementa o padrão Factory. É a única classe responsável por saber como criar e configurar os objetos `Modalidade` com suas respectivas atividades e estratégias de validação.
* **`Parecer`**: Serviço responsável por gerar o relatório final. Ele agrupa os resultados, aplica os limites de aproveitamento por modalidade e formata a saída.
* **`Validacao`**: Interface do padrão Strategy. Define o contrato (`validar()` e `getDescricaoRegra()`) que todas as regras de validação de horas devem seguir.
* **Implementações da Strategy** (`ValidacaoHoraFixa`, `ValidacaoPorMultiplicador`, etc.): Classes concretas, cada uma encapsulando um algoritmo específico de cálculo de horas.

---

## 3. Princípios de Design e Padrões de Projeto Utilizados

A estrutura do projeto foi guiada por princípios de design que visam a manutenibilidade, flexibilidade e clareza do código.

### Padrão de Projeto: Strategy

* Na validação das horas das atividades. A interface `Validacao` e suas implementações (`ValidacaoHoraFixa`, `ValidacaoHoraDeclaradaComLimite`, `ValidacaoPorMultiplicador`, `ValidacaoPadrao`) são o coração deste padrão.
* O regulamento define múltiplas e distintas formas de calcular as horas validadas. Em vez de criar um método com uma estrutura `if-else` complexa na `AvaliacaoService`, o padrão Strategy foi escolhido para encapsular cada regra de cálculo em sua própria classe. Isso torna o sistema:
    * **Extensível**: Adicionar uma nova regra de validação no futuro (ex: validação por créditos) exige apenas a criação de uma nova classe que implemente a interface, sem alterar o código existente. Isso adere ao **Princípio Aberto/Fechado**.
    * **Coeso e Desacoplado**: Cada classe de estratégia tem uma única e bem definida responsabilidade. A `AvaliacaoService` não precisa conhecer os detalhes de cada cálculo, apenas o contrato definido pela interface.

### Padrão de Projeto: Factory Method

* Na classe `ModalidadeFactory`.
* A criação de um objeto `Modalidade` é um processo complexo que envolve: criar a modalidade em si, criar uma lista de `AtividadeComplementar` e, para cada uma delas, instanciar a `Validacao` correta. Centralizar essa lógica de criação na `ModalidadeFactory` traz os seguintes benefícios:
    * **Encapsulamento**: O resto da aplicação (especificamente a `ConsoleUI`) não precisa saber como uma modalidade é montada. Ele simplesmente pede à fábrica: "me dê a modalidade do tipo 1".
    * **Organização e Manutenção**: Se as atividades de uma modalidade mudarem, ou se uma nova modalidade for criada, as alterações ficam concentradas em um único local, facilitando a manutenção e evitando que a lógica de criação se espalhe pelo código.

### Princípio da Responsabilidade Única (Single Responsibility Principle - SRP)

* O projeto foi dividido em classes com responsabilidades bem definidas:
    * `ConsoleUI`: Lida **apenas** com entrada e saída de dados no console.
    * `AvaliacaoService`: Lida **apenas** com a orquestração da validação de uma atividade.
    * `Parecer`: Lida **apenas** com a geração do relatório final e a aplicação dos limites globais.
    * Classes de `Strategy`: Cada uma lida **apenas** com seu algoritmo de cálculo específico.
    * `ModalidadeFactory`: Lida **apenas** com a criação de objetos complexos.
* Essa separação de responsabilidades torna o sistema muito mais fácil de entender e modificar.

---

## 4. Instruções de Uso Passo a Passo

### Pré-requisitos
* Java Development Kit (JDK) instalado (versão 11 ou superior).

### Passo 1: Executar o Programa
Uma vez compilado, execute o programa a partir do diretório raiz do projeto, no arquivo `Main.java`, garantindo que o Java possa encontrar as classes no diretório `main`.

### Passo 2: Interação com o Sistema
1.  O programa solicitará a **matrícula** do aluno. Digite e pressione Enter.
2.  O **menu de modalidades** será exibido. Digite um número de 1 a 4 para escolher uma modalidade.
3.  O **submenu de atividades** da modalidade escolhida será exibido, mostrando a regra de cada uma. Escolha uma atividade pelo número.
4.  O programa solicitará o **valor a ser declarado** (horas, quantidade de meses, etc., conforme a regra). Digite o valor e pressione Enter.
5.  Você pode adicionar quantas atividades quiser, navegando entre as modalidades.
6.  Para voltar ao menu principal a partir de um submenu de atividades, digite `0`.
7.  No menu principal, digite `0` para **finalizar o processo e gerar o parecer completo**.