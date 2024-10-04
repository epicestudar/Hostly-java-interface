<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Contexto do Projeto" alt="Typing SVG" /></a>
### Contexto Inicial
**Gerenciamento de Reservas de Hotel:**
O sistema será um gerenciamento de reservas de hotel, no qual o administrador poderá cadastrar hóspedes e funcionários, gerenciar reservas e acessar extratos de emissão fiscal, enquanto que o hóspede poderá solicitar a sua reserva em quartos disponíveis e efetuar o pagamento.

<br>
<br>
<br>
<p align="center">
   <img src="/src/logo/logo.png" alt="logo" width=250px>
</p>

<p align="center">
   <img src="https://img.shields.io/badge/API-FAZENDO-red?style=for-the-badge" alt="backend" />
  <img src="https://img.shields.io/badge/Documentação-FAZENDO-red?style=for-the-badge" alt="documentação" />
  <img src="https://img.shields.io/badge/Manual-FAZENDO-red?style=for-the-badge" alt="mobile" />
  <img src="https://img.shields.io/badge/Interface-FAZENDO-red?style=for-the-badge" alt="site" />
</p>
<hr>
<br>
<br><br><br>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Gerenciamento de Reservas" alt="Typing SVG" /></a>

## Apresentação do Projeto: Desenvolvimento de Aplicação sobre Gerenciamento de Reservas

### Visão Geral do Projeto
**Objetivo:**
Desenvolver um sistema para gerenciamento de reservas de hotel, onde o administrador poderá cadastrar hóspedes e quartos, gerenciar reservas e acessar extratos de emissão fiscal. Hóspedes poderão solicitar reservas em quartos disponíveis e efetuar o pagamento. A aplicação utilizará tecnologias modernas e práticas de mercado, garantindo segurança, escalabilidade e uma experiência de usuário fluida por meio de Java Swing para a interface administrativa e Spring Boot para a API, com MongoDB como banco de dados.

**Por Que Este Projeto?**
A nossa empresa chamada Hostly, dedicada a inovar no campo da gestão de serviços, está em processo de criação de um sistema de gerenciamento de reservas de hotel. Este projeto visa proporcionar uma experiência eficiente tanto para os funcionários quanto para os hóspedes, facilitando o processo de reserva e gerenciamento dos quartos. Com o objetivo de transformar a forma como os hotéis gerenciam suas reservas e atendem aos hóspedes, estamos desenvolvendo uma solução tecnológica avançada que ofereça uma interface amigável e funcionalidades robustas.
<br><br><br><br><br>
<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Escopo" alt="Typing SVG" /></a>

O sistema de gerenciamento de reservas será desenvolvido utilizando Spring Boot para a API e Java Swing para a interface administrativa, com MongoDB como banco de dados. A solução visa proporcionar uma experiência de usuário intuitiva, com recursos para cadastro e gerenciamento de quartos, reservas, emissão de contratos e relatórios financeiros.
<br><br><br><br><br>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Recursos" alt="Typing SVG"/></a>

**Equipe:**
- 1 Gerente de Projetos
- 3 Desenvolvedores Full-Stack (Spring Boot e Java)
- 1 Administrador de Banco de Dados
- 1 Especialista em Segurança

**Tecnologias:**
- **Spring Boot** (API e Interface do Usuário)
- **Java Swing** (Interface Administrativa)
- **MongoDB** (Banco de Dados)
- **Git/GitHub** (Controle de Versão)

**Ferramentas de Gestão:**
- Trello para gerenciamento de tarefas
- Slack para comunicação interna
- Mermaid para montagem dos diagramas

<br><br><br><br><br>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Diagrama de Classe" alt="Typing SVG" /></a>

```mermaid
classDiagram
    class Administrador {
        +long id_administrador
        +string email
        +string senha
        +post()
    }

    class Hospede {
        +long id_hospede
        +string nome
        +date data_nascimento
        +string telefone
        +string cpf
        +string email
        +string senha
        +list<Reserva> reservas
        +post()
        +get()
        +put()
        +delete()
    }

    class Quarto {
        +int id_quarto
        +string codigo_quarto
        +enum tipo_quarto
        +integer capacidade_quarto
        +double valor_quarto
        +enum status
        +list<Reserva> reservas
        +post()
        +get()
        +put()
        +delete()
    }

    class Reservas {
        +long id_reserva
        +foreignkey codigo_quarto
        +foreignkey cpf_hospede
        +foreignkey nome_hospede
        +integer quantidade_diarias
        +localdate data_check_in
        +localdate data_check_out
        +enum status
        +date data_reserva
        +post()
        +get()
        +put()
        +delete()
    }

    class Pagamento {
        +long id_pagamento
        +foreignkey id_reserva
        +foreignkey cpf_hospede
        +localdate data_pagamento
        +double valor_pago
        +enum metodo_pagamento
        +post()
        +get()
        +put()
        +delete()
        +calcularValorPagamento()
    }

    Administrador "1" -- "0..*" Hospede : "gerencia"
    Administrador "1" -- "0..*" Quarto : "gerencia"
    Hospede "1" -- "0..*" Reservas : "realiza"
    Quarto "1" -- "0..*" Reservas : "é reservado em"
    Reservas "1" -- "0..1" Pagamento : "possui"

```
<br><br><br><br><br>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Diagrama de Uso" alt="Typing SVG" /></a>
```mermaid
flowchart TD
    H[Hóspede] -->|Fazer Login| B(Fazer Login)
    B -->|Acessar Sistema| C(Sistema de Reservas)
    
    C -->|Buscar Quartos Disponíveis| D(Visualizar Quartos)
    D -->|Selecionar Quarto| E(Escolher Quarto e Ver Detalhes)
    E -->|Solicitar Reserva| F(Selecionar Datas e Confirmar Reserva)
    F -->|Efetuar Pagamento| G(Realizar Pagamento)
    G -->|Confirmar| H2(Reserva Confirmada)

    A2[Administrador] -->|Cadastrar Quartos| J(Cadastrar Novos Quartos)
    J -->|Definir Atributos do Quarto| K(Definir Número, Andar, Preço, Capacidade, etc.)
    
    A2 -->|Gerenciar Quartos| L(Gerenciar Quartos Existentes)
    L -->|Editar Quarto| M(Editar Detalhes do Quarto)
    L -->|Excluir Quarto| N(Excluir Quarto)
    
    A2 -->|Cadastrar Hóspede| X(Cadastrar Hóspede)
    A2 -->|Gerenciar Hóspede| Y(Gerenciar Hóspede Existente)
    
    A2 -->|Ver Reservas| Z(Visualizar Reservas)
    Z -->|Ver Pagamentos| W(Emissões de Pagamento)
    
    H -->|Visualizar Reservas| O(Ver Minhas Reservas Atuais)
    O -->|Cancelar Reserva| P(Cancelar Reserva)
    
    H -->|Editar Perfil| Q(Atualizar Informações de Hóspede)


```
<br><br><br><br><br>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Diagrama de Fluxo" alt="Typing SVG" /></a>
```mermaid
flowchart TD
    Start([Início]) -->|Acesso ao Sistema| A[Fazer Login]
    
    A -->|Hóspede| B{Login Bem-sucedido?}
    B -- Sim --> C[Acessar Sistema de Reservas]
    B -- Não --> D[Exibir Erro e Tentar Novamente]

    C -->|Ver Quartos Disponíveis| E[Visualizar Quartos]
    E --> F{Quartos Disponíveis?}
    F -- Sim --> G[Selecionar Quarto]
    F -- Não --> H[Exibir Mensagem: Sem Quartos Disponíveis]
    
    G --> I[Definir Datas de Check-in e Check-out]
    I --> J[Confirmar Reserva]
    J --> K{Efetuar Pagamento?}
    K -- Sim --> L[Escolher Método de Pagamento]
    L --> M[Efetuar Pagamento]
    M --> O[Reserva Confirmada]
    
    O --> End([Fim])

    C -->|Gerenciar Reservas| P[Visualizar Minhas Reservas]
    P --> Q[Cancelar Reserva]
    Q --> R[Reserva Cancelada]

    A -->|Administrador| S{Login Bem-sucedido?}
    S -- Sim --> T[Dashboard de Administração]
    T --> U[Cadastrar Novo Quarto]
    T --> V[Editar Quarto]
    T --> W[Excluir Quarto]
    
    T --> X[Gerenciar Hóspedes]
    X --> Y[Cadastrar Hóspede]
    X --> Z[Editar Hóspede]
    X --> AA[Excluir Hóspede]
    
    T --> AB[Visualizar Reservas]
    AB --> AC[Emitir Pagamento]
    
    S -- Não --> D

```
