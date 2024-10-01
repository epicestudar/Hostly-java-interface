<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Contexto do Projeto" alt="Typing SVG" /></a>
### Contexto Inicial
**Gerenciamento de Reservas de Hotel:**
O sistema será um gerenciamento de reservas de hotel, no qual o funcionário poderá cadastrar quartos, gerenciar reservas e ter acesso aos extratos de emissão fiscal, enquanto que o hóspede poderá solicitar a sua reserva em quartos disponíveis caso preenche os requisitos necessários.

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
Desenvolver um sistema para gerenciamento de reservas de hotel, onde os funcionários poderão cadastrar quartos, gerenciar reservas e acessar extratos de emissão fiscal. Hóspedes poderão solicitar reservas em quartos disponíveis, atendendo aos requisitos necessários. A aplicação utilizará tecnologias modernas e práticas de mercado, garantindo segurança, escalabilidade e uma experiência de usuário fluida por meio de Java para a interface e Node.js para a API, com MongoDB como banco de dados.

**Por Que Este Projeto?**
A nossa empresa chamada Hostly, dedicada a inovar no campo da gestão de serviços, está em processo de criação de um sistema de gerenciamento de reservas de hotel. Este projeto visa proporcionar uma experiência eficiente tanto para os funcionários quanto para os hóspedes, facilitando o processo de reserva e gerenciamento dos quartos. Com o objetivo de transformar a forma como os hotéis gerenciam suas reservas e atendem aos hóspedes, estamos desenvolvendo uma solução tecnológica avançada que ofereça uma interface amigável e funcionalidades robustas.
<br><br><br><br><br>
<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Escopo" alt="Typing SVG" /></a>

O sistema de gerenciamento de reservas será desenvolvido utilizando Node.js para a API e Java para a interface, com MongoDB como banco de dados. A solução visa proporcionar uma experiência de usuário intuitiva, com recursos para cadastro e gerenciamento de quartos, reservas, emissão de contratos e relatórios financeiros.
<br><br><br><br><br>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Recursos" alt="Typing SVG"/></a>

**Equipe:**
- 1 Gerente de Projetos
- 3 Desenvolvedores Full-Stack (Node.js e Java)
- 1 Administrador de Banco de Dados
- 1 Especialista em Segurança

**Tecnologias:**
- **Node.js** (API)
- **Java Swing** (Interface)
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
    class Adm {
        +int id
        +string email
        +string senha
        +post()
    }

    class Hospede {
        +int id
        +string nome
        +date data_nascimento
        +string telefone
        +string cpf
        +string email
        +string senha
        +post()
        +get()
        +put()
        +delete()
    }

    class Quarto {
        +int id
        +int numero_quarto
        +string tipo_quarto
        +int andar
        +string status
        +float preco
        +int capacidade
        +post()
        +get()
        +put()
        +delete()
    }

    class Reservas {
        +int id
        +int fk_numero_quarto
        +string fk_cpf
        +int quantidade_de_diarias
        +date data_check_in
        +date data_check_out
        +string status_reserva
        +date data_reserva
        +post()
        +get()
        +put()
        +delete()
    }

    class Pagamento {
        +int id
        +int fk_reserva
        +date data_pagamento
        +float valor_pago
        +string metodo_pagamento
        +post()
        +get()
        +put()
        +delete()
    }

    Adm "1" -- "0..*" Quarto : "gerencia"
    Hospede "1" -- "0..*" Reservas : "realiza"
    Quarto "1" -- "0..*" Reservas : "é reservado em"
    Reservas "1" -- "0..1" Pagamento : "possui"


```
<br><br><br><br><br>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Diagrama de Uso" alt="Typing SVG" /></a>
```mermaid
flowchart TD
    U[Usuário] -->|Criar Conta| A(Registrar-se)
    A -->|Fazer Login| B(Fazer Login)
    B -->|Acessar Plataforma| C(Plataforma de Enquetes)
    
    C -->|Criar Enquete| D(Criar Nova Enquete)
    D -->|Adicionar Opções| E(Adicionar Opções à Enquete)
    
    C -->|Votar em Enquete| F(Votar em Enquete Existente)
    F -->|Selecionar Opção| G(Escolher Opção e Confirmar Voto)
    
    C -->|Acompanhar Resultados| H(Visualizar Resultados das Enquetes)
    
    I[Criador da Enquete] -->|Gerenciar Enquetes| J(Gerenciar Enquetes que ele criou)
    J -->|Editar Enquete| K(Editar Detalhes da Enquete)
    J -->|Excluir Enquete| L(Excluir Enquete)
    
    U -->|Editar Perfil| M(Atualizar Informações de Usuário)
    U -->|Excluir Conta| N(Excluir Conta)

```
<br><br><br><br><br>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Diagrama de Fluxo" alt="Typing SVG" /></a>
```mermaid
flowchart TD
    Start([Início]) --> |Acessa Plataforma| A[Fazer Login]
    A -->|Login Sucesso| B[Dashboard de Enquetes]
    A -->|Login Falhou| C[Exibir Erro de Login]
    C -->|Tentar Novamente| A
    
    B -->|Criar Nova Enquete| D[Criar Enquete]
    D -->|Inserir Título, Opções e Descrição| E[Confirmar Criação]
    E -->|Sucesso| F[Exibir Enquete no Dashboard]
    
    B -->|Votar em Enquete| G[Selecionar Enquete]
    G -->|Escolher Opção| H[Confirmar Voto]
    H -->|Voto Registrado| I[Atualizar Resultados]
    
    B -->|Acompanhar Resultados| J[Visualizar Resultados em Tempo Real]
    
    B -->|Gerenciar Enquetes| K[Editar ou Excluir Enquete]
    K -->|Confirmar Alterações| F
    
    End([Fim])

```
<br><br><br><br><br>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Identidade Visual" alt="Typing SVG" /></a>

<p align="left">

### Paleta de Cores:

[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&pause=1000&color=1DA1F2&repeat=false&random=false&width=435&lines=Azul+Celeste+-+1DA1F2)](https://git.io/typing-svg)
[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&pause=1000&color=E1E8ED&repeat=false&random=false&width=435&lines=Cinza+Claro+-+E1E8ED)](https://git.io/typing-svg)
[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&pause=1000&color=17BF63&repeat=false&random=false&width=435&lines=Verde+Limão+-+17BF63)](https://git.io/typing-svg)
[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&pause=1000&color=657786&repeat=false&random=false&width=435&lines=Cinza+Médio+-+657786)](https://git.io/typing-svg)
[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&pause=1000&color=FFFFFF&repeat=false&random=false&width=435&lines=Branco+-+FFFFFF)](https://git.io/typing-svg)
[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&pause=1000&color=E0245E&repeat=false&random=false&width=435&lines=Vermelho+Suave+-+E0245E)](https://git.io/typing-svg)
<br><br><br><br>
### Fontes de Texto:

**• Poppins**

**• Roboto**

**• Kanit**
</p>
<br><br><br><br><br>
<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Protótipos" alt="Typing SVG" /></a>

### Baixa Fidelidade:
<p align="center">
   <img src="/src/protótipos/baixa_fidelidade.jpg" alt="logo" width=850px>
</p>

### Média Fidelidade:
<p align="center">
   <img src="/src/protótipos/media_fidelidade.png" alt="logo" width=850px>
</p>

### Alta Fidelidade:
<p align="center">
   <img src="/src/protótipos/alta_fidelidade.png" alt="logo" width=850px>
</p>
<br><br><br><br><br>
 <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Referências 📜" alt="Typing SVG" /></a>

* ### Modelagem de Diagramas:
    - [Mermaid](https://mermaid.live/edit#pako:eNpVjs1qw0AMhF9F6NRC_AI-FBq7zSXQQnPz5iBs2bvE-8NaSwi2373r-NLqJM18M2jG1neMJfajv7eaosClVg7yvDeVjmYSS9MViuJtObGA9Y4fCxxfTh4m7UMwbnjd-eMGQTWfN4xBtHG3dbeqZ_7L8QJ1c6YgPlz_Ope7X-CjMd861_93dOSc-mx6KnsqWopQUXwieEDL0ZLp8vvzpigUzZYVlnntuKc0ikLl1oxSEv_zcC2WEhMfMPo0aMyd45SvFDoSrg0NkeyOrL_WfFuF)

* ### IA's Usadas:
    - [ChatGPT 3.5](https://chat.openai.com/)
    - [Bing - Image Creator](https://www.bing.com/images/create)
 
* ### UX/UI:
  - [Figma](https://www.figma.com/)
  
* ### Outros:
  - [YouTube](https://www.youtube.com/)
  - [Documentação](https://github.com/shyoutarou/README-Model/blob/master/README.md)
  - [Badges](https://dev.to/)
<br><br><br><br><br>
<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=440&size=22&pause=1000&color=38F77CFF&center=false&vCenter=false&repeat=false&width=435&lines=Desenvolvedores do Projeto" alt="Typing SVG" /></a>

<div align=center>
  <table style="width: 100%">
    <tbody>
      <tr align=center>
        <th><strong> Vinícius G. Feitoza </br> epicestudar </strong></th>
        <th><strong> João Victor de Lima </br> JoaovlLima </strong></th>
      </tr>
      <tr align=center>
        <td>
          <a href="https://github.com/epicestudar">
            <img width="250" height="250" style="border-radius: 50%;" src="https://avatars.githubusercontent.com/epicestudar">
          </a>
        </td>
        <td>
          <a href="https://github.com/JoaovlLima">
            <img width="250" height="250" style="border-radius: 50%;" src="https://avatars.githubusercontent.com/JoaovlLima">
          </a>
        </td>
      </tr>
    </tbody>

  </table>
</div>
