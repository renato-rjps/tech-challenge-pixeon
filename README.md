# Tech Challenge Pixeon
Regras do desafio técnico [Project Roles](https://github.com/Pixeon/tech-challenge)

### Arquitetura do Projeto

Esse projeto possui uma arquitetura baseada em micro serviços utilizando como base ferramentas oferecidas pelo framework Spring.

O projeto é composto por três módulos, são eles:

* Discovery - Este projeto é o service-discovery e é onde os serviços são registrados
* Gateway - Esse projeto possui duas funcionalidades que consistem em funcionar como uma API-Gateway e balancear as chamadas para os serviços. 
* Exam-API -  Esse é o projeto que possui a lógica do negócio conforme requisitos do projeto. 

Os projetos mencionados acima estão utilizando uma estrutura de parent-pom para melhor organizar o build e dependências do projeto.



### Tecnologias Utilizadas

Esse projeto está utilizando diversas tecnologias oferecidas pelo framework Spring, entre elas vale destacar as seguintes: 

* Spring Data JPA - Aborda o conceito de repositórios e simplifica a implementação JPA no projeto.
* Spring HATEOAS - API que possibilita a criação de APIs REST seguindo os princípios do HATEOAS. [Leia mais](https://martinfowler.com/articles/richardsonMaturityModel.html)
* Spring Data REST - API poderosa que reduz significantemente a quantidade de código boilerplate code em uma API desenvolvida com Spring.
*  Spring Cloud Gateway - Biblioteca para criar um gateway de API.
* Spring Cloud Netflix - Integrações do Netflix OSS para o Spring Boot. 
* Spring Actuator - Biblioteca que possibilita adicionar a aplicação a capacidade de coletar métricas, tráfego , estado do banco de dados etc.
* Spring Hal Browser - Biblioteca que adiciona ao projeto a capacidade de se testar a API Rest de forma simples. Semelhante ao swagger só que para a arquitetura HATEOAS. [Leia mais](http://stateless.co/hal_specification.html) 

### Curiosidades

- Auditoria 
  - Esse projeto está configurado para auditar as entidades e colocar automaticamente as datas de criação e edição de cada entidade.
- Mensagens
  - O projeto usufrui do spring-mvc e utiliza mensagens internacionalizadas através do locale do servidor. Nesse projeto só existem dois arquivos messages.properties e messages_en.properties e neles estão os termos que foram necessários para esse projeto.
- Identificação da Instancia do Serviço
  - Foi criado nesse projeto um serviço para retornar um identificador da instancia de uma mesma api que está sendo chamada. Isso facilita para identificar como o loadbalancer está distribuindo as chamadas. O serviço é http://localhost:8080/info

### Como rodar o projeto?

Abaixo estão listados os passos necessários para rodar o projeto.

1. Baixar o projeto do repositório através do comando: git clone https://github.com/renato-rjps/tech-challenge-pixeon.git

2. Após baixar o projeto acessar a pasta root através do comando: cd tech-challenge-pixeon

3. Após acessar a estrutura raiz do projeto, é necessário realizar o build do projeto através do comando:  mvn clean install

4. Agora iremos executar o projeto que representa o service-discovery através do comando: java -jar discovery/target/discovery-1.0.0.jar

5. Antes de executar essa etapa tenha certeza que o serviço anterior já  está de pé. Abra uma nova janela do prompt e agora vamos executar o projeto que representa a api-gateway através do comando: java -jar gateway/target/gateway-1.0.0.jar

6. O ultimo passo é executar o serviço que representa a API de negócio. Abra uma nova janela do prompt e execute o comando : java -jar exam-api/target/exam-api-1.0.0.jar. Vale ressaltar que podem ser levantadas várias instancias desse serviço, não é necessário informar a porta, basta apenas executar o mesmo comando em uma nova janela do prompt. 

7. Acesse através do seu navegador o endereço http://:localhost:8080 . Você deverá acessar o HAL Browser  da aplicação.

> OBS: É necessário esperar alguns segundos para que uma instancia se registre no service-discovery.
> Por isso, caso você se depare com um erro 503 não se preocupe, aguarde alguns segundos e tente acessar novamente. 

### Postman 

Na estrutura raiz do projeto existe um arquivo com o nome 'pixeon-tech-challenge.postman_collection.json'. Esse é o arquivo do workspace do postman, caso você esteja familiarizado com essa ferramenta basta importar esse arquivo. 

Também será necessário declarar uma variável chamada "API_URL" apontando para "http://localhost:8080" 

Após isso basta brincar com as chamadas para a API.

### H2

> *Para simplificar a utilização desse projeto foi configurado o H2 em memória. No entanto, caso queira testar múltiplas instancias do serviço exam-api é necessário instalar o h2 na maquina ou utilizar uma instancia única do banco de dados de sua preferência.* 

Esse projeto está utilizando o H2 em memória. Para acessar  o console do h2 basta acessar o endereço http://localhost:8080/h2-console. 

JDBC url: jdbc:h2:mem:pixeon

Usuário: Sa 

Senha:  

### PostgreSQL

O projeto possui um profile do Spring configurado para rodar com PostgreSql, para isso basta rodar a aplicação da seguinte forma: java -Dspring.profiles.active=postgres -jar exam-api/target/exam-api-1.0.0.jar

Caso seja necessário mudar (usuário,senha ou url) do banco de dados, basta passar as seguintes variáveis ao subir a aplicação:

> -Dspring.datasource.url=jdbc:postgresql://192.168.99.100:5432/pixeon
>
> -Dspring.datasource.username=postgres
>
> -Dspring.datasource.password=



Exemplo:

`java -Dspring.profiles.active=postgres -Dspring.datasource.url=jdbc:postgresql://192.168.99.100:5432/pixeon
-Dspring.datasource.username=postgres
-Dspring.datasource.password=123456 -jar exam-api/target/exam-api-1.0.0.jar`

### Observações 

#### Regras de Negócio

Para saber quais exames pertencem a uma instituição é necessário consultar a listagem de exames de uma determinada instituição. Como não existia uma regra nos requisitos para essa situação o serviço que lista os exames não desconta do budget, apenas será descontado do budget quando for consultado um exame específico passado o id do exame. Porém, caso o budget já tenha excedido será aplicada a seguinte regra: 

- Já visualizou algum exame? exibe só os que já foram lidos 
- Não visualizou nenhum exame? Exibe mensagem de erro.

#### Cadastro de Registros Com Relacionamento

Essa aplicação esta utilizando Hypermedia As The Engine Of Application State (HATEOAS). E por tanto, é necessário seguir os princípios do HATEOAS para utilizar a API. Para criar um relacionamento por exemplo você deve informar o link do relacionamento e não apenas o id. Veja o exemplo abaixo: 

{
	"physicianName": "Nome",
    "physicianCrm":123,
    "healthcareInstitution": "healthcareInstitutions/1",
    "patientName": "Nome",
    "procedureName": "Nome"
}

Para saber mais sobre HATEOAS e HAL acesse os links: 

[Passos em direção à glória do REST](https://martinfowler.com/articles/richardsonMaturityModel.html)

[Hipermídia enxuta](http://stateless.co/hal_specification.html) 