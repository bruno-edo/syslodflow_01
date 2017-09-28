SysLODFlow
===================
Uma ferramenta para automação da publicação de dados conectados utilizando o LodFlow.

## Sumário

- [Visão geral da aplicação](#visão-geral-da-aplicação)
    - [Trabalhos referência](#trabalhos-referência)
- [Dependências](#dependências)
- [Instalação](#instalação)
    - [Setup banco de dados relacional](#setup-banco-de-dados-relacional)
    - [Comunicação do JBOSS com o BD](#comunicação-do-jboss-com-o-bd)

---

## Visão geral da aplicação

O conceito de dados ligados surgiu para que a publicação e difusão de informações na web pudessem ser aprimoradas. Utilizando estes conceitos é possível ligar dados relevantes, e a partir dessa ligação descobrir informações que de outra maneira passariam despercebidas. Diversas ferramentas foram desenvolvidas com o intuito de auxiliar na extração e limpeza de dados, contudo todas exigem conhecimentos específicos sobre sua aplicação, e a necessidade de integração com outras soluções de terceiros. Dessa maneira o processo para a divulgação do conhecimento gerado seguindo os preceitos de Linked Data se torna extremamente oneroso.

Para tentar abrandar a dificuldade encontrada no processo foi criada a ferramenta SysLODFlow, a qual integra diversas soluções da área, criando um fluxo de trabalho que engloba todas as etapas do processo de publicação.

O SysLODFlow faz uso das funcionalidades do [LODFlow](https://github.com/AKSW/LODFlow), mas fornece opções mais “user friendly” (e automatizadas) para se realizar o ciclo de publicação e manutenção de dados conectados. A ferramenta também foi desenvolvida de maneira a ser um aplicativo web, o que soluciona algumas restrições iniciais do LODFlow, e abrem as portas para funcionalidades que antes não eram possíveis em aplicações standalone, mas que trazem consigo algumas necessidades pontuais de design para web.

O aplicativo web utiliza o Java EE como principal linguagem de desenvolvimento, além de alguns frameworks e bibliotecas, que podem ser vistos, bem como em quais camadas são aplicados, na imagem a seguir.

![Stack de tecnologias do SysLODFlow](https://lh6.googleusercontent.com/JLsc8wZQgT_URKZOftFF_NAIeAFYC3magjLRiNvFa2yyh9OjHo12mZ7xYRcjfmmr8oQrCbfkwrkFPQ=w1920-h944)

#### Trabalhos referência

O SysLODFlow começou a ser desenvolvido através de Trabalhos de Conclusão de Curso (TCCs) da Universidade Federal de Santa Catarina. Abaixo estão listados os trabalhos relacionados a este projeto:

- [LDWPO - Linked Data Workflow Project ontology - Sandro Rautenberg](https://github.com/AKSW/ldwpo/blob/master/misc/technicalReport/LDWPO_technicalReport.pdf)
- [SysLODFlow – Uma ferramenta de apoio a automação da publicação e manutenção de Linked Data utilizando o LODFlow - Jean Carlos De Morais e Jhonatan Carlos De Morais - UFSC]()
- [SysLODFlow: Ampliando as funcionalidades na automação da publicação e manutenção de dados abertos conectados usando o LodFlow - Bruno Eduardo D'Angelo de Oliveira - UFSC]()

TODO: colocar links quando eles estiverem disponíveis (colocar trabalhos no repositório do github é uma idéia possível, perguntar ao tite)

---

## Dependências

Para rodar o SysLODFlow são necessárias algumas ferramentas de apoio, bem como algumas tecnologias Java, as quais são listadas abaixo:

1. Java EE 7
2. JBOSS 7.1 (Thunder)
3. [LIMES](http://aksw.org/Projects/LIMES.html)
4. [Sparqlify](http://aksw.org/Projects/Sparqlify.html)
5. [Virtuoso](https://virtuoso.openlinksw.com/)
6. MySQL (ou outro banco de dados SQL)

---

## Instalação

TODO: falar sobre a geração do arquivo WAR e de como lançar a execução do mesmo pelo JBOSS

[Maven](https://maven.apache.org/)

#### Setup banco de dados relacional

No BD relacional escolhido, crie um esquema relacional chamado **syslodflowds** e rode o [script SQL de configuração](https://github.com/bruno-edo/syslodflow_01/blob/master/syslodflow/scripts/syslodflow.sql). Dessa maneira um usuário administrador padrão será criado com o usuário e senha abaixo:

> **Usuário:** admin
>
> **Senha:** admin

#### Comunicação do JBOSS com o BD

Para que seja possível que o SysLODFlow se comunique com a instância do banco de dados relacional instalada, se faz necessário criar um **Data Source** no arquivo **standalone.xml** do JBOSS. Dessa maneira, procure em seu sistema pelo arquivo **standalone.xml** e navegue para a seção de datasources, após localizá-la inclua o código abaixo para criar o **Data Source**.

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

Também é necessário fazer o download do driver JDBC que se pretende utilizar (o conector MySQL pode ser baixado neste [link](https://www.mysql.com/products/connector/)) e incluí-lo no arquivo standalone.xml.

Após o download e instalação do conector, navegue até a seção de drivers e insira o código abaixo.

```xml
<driver name="com.mysql" module="com.mysql">
    <driver-class>com.mysql.jdbc.Driver</driver-class>
    <xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
</driver>
```

> **Importante:** É preciso que sejam alterados os campos de URL da conexão ao banco, bem como o usuário e senha, para as informações de acesso configuradas para a instalação em seu sistema.
>
> O driver de comunicação também deve ser configurado de acordo com o sistema de banco de dados que se deseja utilizar. Neste caso estamos usando o MySQL como exemplo.

Após realizar essas etapas o projeto está pronto para ser lançado, para isso basta inciar o servidor JBOSS e o projeto será iniciado juntamente, no endereço IP e porta configurados.
