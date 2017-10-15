![SysLODFlow](https://github.com/bruno-edo/syslodflow_01/blob/master/syslodflow/src/main/webapp/imagens/syslodflow.png "SysLODFlow")
===================
Uma ferramenta para automação da publicação de dados conectados utilizando o LodFlow.

## Sumário

- [Visão geral da aplicação](#visão-geral-da-aplicação)
  - [Trabalhos referência](#trabalhos-referência)
- [Dependências](#dependências)
- [Árvore de diretórios](#arvore-de-diretorios)
- [Rodando o projeto no Eclipse JEE IDE](#rodando-o-projeto-no-eclipse-jee-ide)
  - [Setup banco de dados relacional](#setup-banco-de-dados-relacional)
  - [Comunicação do JBOSS com o BD](#comunicação-do-jboss-com-o-bd)
- [Funcionalidades](#utilizacao)
  - [Criando e executando um projeto](#criando-e-executando-um-projeto)
  - [Criando execuções de fluxos e visualizando arquivos gerados](#criando-execucoes-de-fluxos-e-visualizando-arquivos-gerados)
- [Créditos](#creditos)


---

## Visão geral da aplicação

O conceito de dados ligados surgiu para que a publicação e difusão de informações na web pudessem ser aprimoradas. Utilizando estes conceitos é possível ligar dados relevantes, e a partir dessa ligação descobrir informações que de outra maneira passariam despercebidas. Diversas ferramentas foram desenvolvidas com o intuito de auxiliar na extração e limpeza de dados, contudo todas exigem conhecimentos específicos sobre sua aplicação, e a necessidade de integração com outras soluções de terceiros. Dessa maneira o processo para a divulgação do conhecimento gerado seguindo os preceitos de Linked Data se torna extremamente oneroso.

Para tentar abrandar a dificuldade encontrada no processo foi criada a ferramenta SysLODFlow, a qual integra diversas soluções da área, criando um fluxo de trabalho que engloba todas as etapas do processo de publicação.

O SysLODFlow faz uso das funcionalidades do [LODFlow](https://github.com/AKSW/LODFlow), mas fornece opções mais “user friendly” (e automatizadas) para se realizar o ciclo de publicação e manutenção de dados conectados. A ferramenta também foi desenvolvida de maneira a ser um aplicativo web, o que soluciona algumas restrições iniciais do LODFlow, e abrem as portas para funcionalidades que antes não eram possíveis em aplicações standalone, mas que trazem consigo algumas necessidades pontuais de design para web.

O aplicativo web utiliza o Java EE 7 como principal linguagem de desenvolvimento, além de alguns frameworks e bibliotecas, que podem ser vistos, bem como em quais camadas são aplicados, na imagem a seguir.

![Stack de tecnologias do SysLODFlow](https://github.com/bruno-edo/syslodflow_01/blob/master/images/syslodflow-layers.png "Stack de tecnologias do SysLODFlow")

#### Trabalhos referência

O SysLODFlow começou a ser desenvolvido através de Trabalhos de Conclusão de Curso (TCCs) da Universidade Federal de Santa Catarina. Abaixo estão listados os trabalhos relacionados a este projeto:

- [LDWPO - Linked Data Workflow Project Ontology - Sandro Rautenberg](https://github.com/AKSW/ldwpo/blob/master/misc/technicalReport/LDWPO_technicalReport.pdf)
- [SysLODFlow – Uma ferramenta de apoio a automação da publicação e manutenção de Linked Data utilizando o LODFlow - Jean Carlos De Morais e Jhonatan Carlos De Morais - UFSC]()
- [SysLODFlow: Ampliando as funcionalidades na automação da publicação e manutenção de dados abertos conectados usando o LodFlow - Bruno Eduardo D'Angelo de Oliveira - UFSC]()

---

## Dependências

Para rodar o SysLODFlow são necessárias algumas ferramentas de apoio, bem como algumas tecnologias Java:

1. [Java EE 7](https://www.java.com/en/)
2. [JBOSS 7.1.0 (Thunder)](http://www.jboss.org/)
3. [LIMES](http://aksw.org/Projects/LIMES.html)
4. [Sparqlify](http://aksw.org/Projects/Sparqlify.html)
5. [Virtuoso](https://virtuoso.openlinksw.com/)
6. [MySQL](https://www.mysql.com/)

**Bibliotecas e Frameworks:**

1. [Primefaces 6.1](https://www.primefaces.org/)
2. [Bootstrap 4](https://getbootstrap.com/)
3. [SB Admin](https://startbootstrap.com/template-overviews/sb-admin/)
4. [DataTables](https://datatables.net/)

---

## Árvore de diretórios

```
syslodflow
└── scripts
└── src
    └── main
        └── java.br.ufsc.inf.syslodflow
        |   └── business
        |   └── crypto
        |   └── dao
        |   └── dto
        |   └── entity
        |   └── service
        |   └── util
        |   └── web
        |
        └── resources
        |   └── br.com.connectionltda
        |   └── configuration
        |   └── META-INF
        |
        └── webapp
            └── cadastros
            └── css
            └── estrutura
            └── font-awesome
            └── imagens
            └── js
            └── management
            └── ontology
            └── WEB-INF
```

## Rodando o projeto no Eclipse JEE IDE

Após clonar o repositório do projeto, importe-o como um projeto [Maven](https://maven.apache.org/). Dessa maneira o Eclipse se encarregará de criar o arquivo **.war** automáticamente. Este arquivo será deployado para o JBOSS, para que a aplicação seja rodada por ele.

Para criar uma instância do JBOSS pelo Eclipse é necessário baixar o plugin JBOSS Tools, para isso acesse este [link](https://marketplace.eclipse.org/content/jboss-tools), ou (ainda dentro do Eclipse) abra o Eclipse Marketplace e procure por JBOSS Tools.

Após a instalação do plugin, acesse **New -> Other -> Server** e selecione o **JBOSS AS 7.1.0**. Depois deste passo, uma aba de servidores poderá ser observada na barra inferior da IDE, selecione-a e clique com o botão direito do mouse na instância do JBOSS e selecione a opção **Add and Remove...**, em seguida adicione o arquivo **.war**, do SysLODFlow, aos projetos que devem ser publicados pelo servidor e clique em Finish.

Após os outros passos do setup serem completos, basta rodar o servidor e o SysLODFlow deve ser publicado automáticamente por ele.

#### Setup banco de dados relacional

No BD relacional escolhido, crie um esquema relacional chamado **syslodflowds** e rode o [script SQL de configuração](https://github.com/bruno-edo/syslodflow_01/blob/master/syslodflow/scripts/syslodflow.sql). Dessa maneira um usuário administrador padrão será criado com o usuário e senha abaixo:

> **Usuário:** admin
>
> **Senha:** admin

#### Comunicação do JBOSS com o MySQL

É necessário fazer o download do driver JDBC que se pretende utilizar (o conector MySQL pode ser baixado neste [link](https://www.mysql.com/products/connector/). Há também uma cópia do conector na pasta **syslodflow/src/main/resources/META-INF** deste projeto) e disponbilizá-lo para o JBOSS. Para isso siga estes passos:

1. Copie o driver do MySQL para a pasta **/modules/com/mysql/main**, do local onde o JBOSS foi instalado. Provavelmente não existirá a pasta **/mysql/main**, portanto você deverá criá-la e efetuar a cópia.
2. Na mesma pasta crie o documento module.xml contendo o código abaixo:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<module xmlns="urn:jboss:module:1.0" name="com.mysql">
  <resources>
    <resource-root path="mysql-connector-java-5.1.44.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
  </dependencies>
</module>
```

A pasta main deverá conter os seguintes arquivos:

- module.xml
- mysql-connector-java-5.1.44.jar

Para que seja possível que o SysLODFlow se comunique com a instância do banco de dados relacional instalada, se faz necessário criar um **Data Source** no arquivo **standalone.xml** do JBOSS. Dessa maneira, procure em seu sistema pelo arquivo **standalone.xml** e navegue para a seção de datasources, após localizá-la inclua o trecho de código abaixo:

```xml
<datasource jta="true" jndi-name="java:jboss/datasources/syslodflowDS" pool-name="syslodflowDS" enabled="true" use-java-context="true" use-ccm="true">
    <connection-url>jdbc:mysql://localhost:3306/syslodflowds</connection-url>
    <driver>com.mysql</driver>
    <transaction-isolation>TRANSACTION_READ_COMMITTED</transaction-isolation>
    <pool>
        <min-pool-size>5</min-pool-size>
        <max-pool-size>30</max-pool-size>
        <prefill>true</prefill>
        <use-strict-min>false</use-strict-min>
        <flush-strategy>FailingConnectionOnly</flush-strategy>
    </pool>
    <security>
        <user-name>root</user-name>
        <password>root</password>
    </security>
    <statement>
        <prepared-statement-cache-size>32</prepared-statement-cache-size>
    </statement>
</datasource>
```

Navegue até a seção de drivers e insira o trecho abaixo.

```xml
<driver name="com.mysql" module="com.mysql">
    <driver-class>com.mysql.jdbc.Driver</driver-class>
    <xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
</driver>
```

> **Importante:** É preciso que sejam alterados os campos de URL da conexão ao banco, bem como o usuário e senha, para as informações de acesso configuradas para a instalação em seu sistema.
>
> O driver de comunicação também deve ser configurado de acordo com o sistema de banco de dados que se deseja utilizar. Neste caso estamos usando o MySQL como exemplo.

Após realizar essas etapas o projeto está pronto para ser lançado. Para isso, basta ativar o servidor JBOSS e o projeto será iniciado juntamente, no endereço IP e porta configurados.

## Utilização

O fluxo geral de operações do SysLODFlow pode ser visualizado no seguinte diagrama:

![Diagrama de Fluxo do SysLODFlow](https://github.com/bruno-edo/syslodflow_01/blob/master/images/syslodflow%20execution%20flow.png "Diagrama de Fluxo do SysLODFlow")

### Criando e executando um projeto

Para visualizar os LDWProjects já cadastrados, acesse a seção LDWProject. Os projetos já cadastrados serão exibidos em uma lista.

![Lista de Projetos](https://github.com/bruno-edo/syslodflow_01/blob/master/images/ldwproject%20section.png "Lista de Projetos")

Para criar um novo projeto, clique no botão "Novo" e preencha os campos de informação. Após o preenchimento, clique em "Salvar" e o projeto deverá ser exibido na lista da figura anterior.

Para editar ou excluir um projeto, clique nos botões "Editar" e  "Excluir", respectivamente.

![Criando um Novo Projeto](https://github.com/bruno-edo/syslodflow_01/blob/master/images/ldwproject%20new.png "Criando um Novo Projeto")

Para criar um workflow novo, clique no botão "Abrir" da coluna de LDWOrkflows da lista de projetos. Após o carregamento da página, preencha os campos com as informações relacionadas ao workflow que se deseja criar (como base de dados a ser acessada, arquivos de configuração do LIMES e do Sparqlify, entre outros).

![Criando um Novo LDWorkflow](https://github.com/bruno-edo/syslodflow_01/blob/master/images/planejamento%20workflow.png "Criando um Novo LDWorkflow")

### Criando execuções de fluxos e visualizando arquivos gerados

Para criar novos arquivos de execução de fluxo, acesse a parte de LDWorkflowExecutions e clique no botão Novo. Insira as informaçãoes necessárias e clique em "Salvar" para armazenar os dados em disco.

![Criando Novos Arquivos de Execução](https://github.com/bruno-edo/syslodflow_01/blob/master/images/ldwexecution%20new.png "Criando Novos Arquivos de Execução")

Caso se deseje executar um fluxo, basta clicar no botão "Executar", e o fluxo deverá ser enviado ao LODFlow engine. Após o processo ser finalizado, os arquivos gerados poderão ser visualizados na página principal de LDWorkflowExecution, como mostrado abaixo.

![Lista de Arquivos Gerados](https://github.com/bruno-edo/syslodflow_01/blob/master/images/ldwexecution.png "Lista de Arquivos Gerados")

## Créditos

[Bruno Eduardo D'Angelo de Oliveira](https://github.com/bruno-edo)

[Jean Carlos de Morais](https://github.com/jeanmorais/)

[Jhonatan Carlos de Morais]()
