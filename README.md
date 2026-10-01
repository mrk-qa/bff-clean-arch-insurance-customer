# BFF Clean Architecture - Insurance Customer

POC de um **Backend for Frontend (BFF)** desenvolvido em Java e Spring Boot, utilizando conceitos de **Clean Architecture** para organizar responsabilidades e reduzir o acoplamento entre regras de negócio, APIs externas e camada HTTP.

O projeto simula um cenário de seguros onde o BFF recebe uma requisição do frontend e consolida informações de:

* Cliente
* Apólices
* Veículos

O projeto também possui APIs mockadas para simular os serviços externos consumidos pelo BFF.

---

## Tecnologias

* Java 21
* Spring Boot
* Spring Web
* Maven
* Maven Wrapper
* REST APIs
* IntelliJ IDEA

---

# Arquitetura

O projeto utiliza uma estrutura baseada em **Clean Architecture**, separando o código em camadas de acordo com suas responsabilidades.

Fluxo principal:

```text
Frontend
    |
    | GET /api/v1/customers/{customerId}/dashboard
    v
+--------------------------------+
| CustomerDashboardController    |
| Adapter / Web                  |
+--------------------------------+
                |
                v
+--------------------------------+
| GetCustomerDashboardUseCase    |
| Application                    |
+--------------------------------+
                |
        +-------+-------+
        |       |       |
        v       v       v
 CustomerApi PolicyApi VehicleApi
        |       |       |
        v       v       v
 CustomerApiClient
 PolicyApiClient
 VehicleApiClient
        |
        v
+--------------------------------+
| Mock APIs                      |
| Port 8081                      |
+--------------------------------+
```

A ideia principal é que a camada de negócio não dependa diretamente de detalhes de implementação HTTP.

Por exemplo, o Use Case conhece:

```java
VehicleApi
```

mas não precisa conhecer diretamente:

```java
VehicleApiClient
```

A implementação concreta é fornecida pelo Spring através de injeção de dependência.

---

# Estrutura de diretórios

```text
src/
└── main/
    ├── java/
    │   ├── com/poc/bff_clean_arch_insurance_customer/
    │   │
    │   │   ├── adapter/
    │   │   │   └── in/
    │   │   │       └── web/
    │   │   │           ├── CustomerDashboardController.java
    │   │   │           ├── CustomerDashboardMapper.java
    │   │   │           ├── CustomerDashboardResponse.java
    │   │   │           ├── GlobalExceptionHandler.java
    │   │   │           └── dto/
    │   │   │               └── ErrorResponse.java
    │   │   │
    │   │   ├── application/
    │   │   │   └── usecase/
    │   │   │       └── GetCustomerDashboardUseCase.java
    │   │   │
    │   │   ├── domain/
    │   │   │   ├── exception/
    │   │   │   │   ├── BadRequestException.java
    │   │   │   │   └── ResourceNotFoundException.java
    │   │   │   │
    │   │   │   ├── model/
    │   │   │   │   ├── Customer.java
    │   │   │   │   ├── CustomerDashboard.java
    │   │   │   │   ├── Policy.java
    │   │   │   │   └── Vehicle.java
    │   │   │   │
    │   │   │   └── port/
    │   │   │       ├── CustomerApi.java
    │   │   │       ├── PolicyApi.java
    │   │   │       └── VehicleApi.java
    │   │   │
    │   │   ├── infrastructure/
    │   │   │   ├── client/
    │   │   │   │   ├── CustomerApiClient.java
    │   │   │   │   ├── PolicyApiClient.java
    │   │   │   │   └── VehicleApiClient.java
    │   │   │   │
    │   │   │   ├── config/
    │   │   │   │   ├── ApplicationConfig.java
    │   │   │   │   └── RestClientConfig.java
    │   │   │   │
    │   │   │   ├── dto/
    │   │   │   │   └── PolicyApiResponse.java
    │   │   │   │
    │   │   │   └── mapper/
    │   │   │       └── PolicyMapper.java
    │   │   │
    │   │   └── BffCleanArchInsuranceCustomerApplication.java
    │   │
    │   └── com/poc/insurance_mock/
    │       ├── MockApiApplication.java
    │       └── controller/
    │           ├── MockCustomerController.java
    │           ├── MockPolicyController.java
    │           └── MockVehicleController.java
    │
    └── resources/
        ├── application.yml
        └── application-mock.yml
```

---

# Camada Adapter

Localização:

```text
adapter/in/web/
```

Essa camada representa a entrada HTTP da aplicação.

Ela recebe requisições externas e transforma os dados para o formato utilizado pela aplicação.

## CustomerDashboardController

Responsável pelo endpoint principal do BFF:

```http
GET /api/v1/customers/{customerId}/dashboard
```

Exemplo:

```http
GET http://localhost:8080/api/v1/customers/1/dashboard
```

O Controller não possui a regra de negócio de agregação.

Ele apenas:

1. Recebe o `customerId`
2. Chama o Use Case
3. Converte o resultado para o Response
4. Retorna a resposta HTTP

---

## CustomerDashboardMapper

Responsável por converter o modelo de domínio:

```text
CustomerDashboard
```

para o modelo de resposta HTTP:

```text
CustomerDashboardResponse
```

Isso evita expor diretamente os objetos de domínio para o consumidor da API.

---

## CustomerDashboardResponse

Representa o contrato de resposta do endpoint do BFF.

A resposta contém:

```text
customer
policies
vehicles
```

Exemplo simplificado:

```json
{
  "customer": {
    "id": 1,
    "name": "João da Silva"
  },
  "policies": [
    {
      "id": 10,
      "policyNumber": "POL-2026-001",
      "status": "ACTIVE",
      "coverage": [
        "COLLISION",
        "THEFT"
      ],
      "vehicleId": 101
    }
  ],
  "vehicles": [
    {
      "id": 101,
      "brand": "Toyota",
      "model": "Corolla",
      "year": 2025
    }
  ]
}
```

---

## GlobalExceptionHandler

Responsável pelo tratamento global das exceções da aplicação.

Atualmente o projeto trata:

### 400 Bad Request

Quando uma API externa rejeita a requisição por dados inválidos.

A exceção:

```text
BadRequestException
```

é convertida para HTTP `400`.

### 404 Not Found

Quando um recurso não é encontrado.

A exceção:

```text
ResourceNotFoundException
```

é convertida para HTTP `404`.

---

## ErrorResponse

Padroniza o formato das respostas de erro:

```json
{
  "timestamp": "2026-10-01T20:00:00Z",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Customer 999 not found.",
  "path": "/api/v1/customers/999/dashboard"
}
```

---

# Camada Application

Localização:

```text
application/usecase/
```

Essa camada contém os casos de uso da aplicação.

## GetCustomerDashboardUseCase

É responsável pela principal regra de negócio do POC.

O fluxo executado é:

```text
1. Buscar Customer
        ↓
2. Buscar Policies
        ↓
3. Para cada Policy:
       buscar Vehicle
        ↓
4. Montar CustomerDashboard
```

O Use Case depende das interfaces:

```java
CustomerApi
PolicyApi
VehicleApi
```

e não diretamente das implementações HTTP.

Isso reduz o acoplamento entre a regra de negócio e a infraestrutura.

---

# Camada Domain

Localização:

```text
domain/
```

Representa os conceitos centrais do domínio da aplicação.

## Models

Localização:

```text
domain/model/
```

### Customer

Representa o cliente:

```java
Customer(
    Long id,
    String name
)
```

### Policy

Representa uma apólice:

```java
Policy(
    Long id,
    String policyNumber,
    String status,
    List<String> coverage,
    Long vehicleId
)
```

### Vehicle

Representa um veículo:

```java
Vehicle(
    Long id,
    String brand,
    String model,
    Integer year
)
```

### CustomerDashboard

Representa o resultado consolidado pelo BFF:

```java
CustomerDashboard(
    Customer customer,
    List<Policy> policies,
    List<Vehicle> vehicles
)
```

---

# Ports

Localização:

```text
domain/port/
```

Os Ports definem os contratos que a aplicação precisa para acessar recursos externos.

Existem três Ports:

```text
CustomerApi
PolicyApi
VehicleApi
```

Por exemplo:

```java
public interface VehicleApi {

    Vehicle findVehicle(Long vehicleId);

}
```

O domínio conhece a interface, mas não precisa saber como a implementação funciona.

A implementação está na infraestrutura:

```text
VehicleApi
    ↑
    |
VehicleApiClient
```

---

# Exceptions

Localização:

```text
domain/exception/
```

Atualmente existem duas exceções específicas:

```text
BadRequestException
ResourceNotFoundException
```

Elas representam erros de negócio/aplicação de forma independente da implementação HTTP.

Por exemplo, a infraestrutura pode receber:

```text
HTTP 404
```

e transformá-lo em:

```text
ResourceNotFoundException
```

Depois, o Adapter transforma essa exceção novamente em:

```text
HTTP 404
```

---

# Camada Infrastructure

Localização:

```text
infrastructure/
```

Essa camada contém detalhes técnicos necessários para executar a aplicação.

## Clients

Localização:

```text
infrastructure/client/
```

Existem três clientes HTTP:

```text
CustomerApiClient
PolicyApiClient
VehicleApiClient
```

Eles implementam os Ports definidos no domínio.

### CustomerApiClient

Implementa:

```java
CustomerApi
```

Responsável por chamar:

```http
GET /customers/{id}
```

### PolicyApiClient

Implementa:

```java
PolicyApi
```

Responsável por chamar:

```http
GET /policies/customer/{id}
```

### VehicleApiClient

Implementa:

```java
VehicleApi
```

Responsável por chamar:

```http
GET /vehicles/{id}
```

Os três clientes utilizam o `RestClient` do Spring para realizar as chamadas HTTP.

---

# DTO externo

Localização:

```text
infrastructure/dto/
```

## PolicyApiResponse

Representa o formato retornado pela API externa de Policy.

O projeto mantém esse DTO separado do modelo de domínio:

```text
PolicyApiResponse
        ↓
PolicyMapper
        ↓
Policy
```

Isso evita acoplar diretamente o domínio ao contrato externo da API.

---

# Mappers

Localização:

```text
infrastructure/mapper/
```

## PolicyMapper

Responsável por converter:

```text
PolicyApiResponse
```

em:

```text
Policy
```

Dessa forma, o modelo externo da API não precisa ser utilizado diretamente pelo Use Case.

---

# Configuração

Localização:

```text
infrastructure/config/
```

## RestClientConfig

Cria o `RestClient.Builder` utilizado pelos clientes HTTP.

## ApplicationConfig

Registra o:

```text
GetCustomerDashboardUseCase
```

como Bean do Spring e injeta suas dependências:

```text
CustomerApi
PolicyApi
VehicleApi
```

e:

```text
VehicleApi
```

---

# APIs Mock

O projeto possui uma segunda aplicação Spring Boot para simular os serviços externos.

Localização:

```text
com/poc/insurance_mock/
```

A aplicação mock utiliza a porta:

```text
8081
```

Enquanto o BFF utiliza:

```text
8080
```

## MockApiApplication

É o ponto de entrada da aplicação mock.

Ela utiliza o profile:

```text
mock
```

e executa na porta `8081`.

---

## MockCustomerController

Endpoint:

```http
GET /customers/{customerId}
```

Atualmente o mock possui o cliente:

```text
ID: 1
Nome: João da Silva
```

Clientes inexistentes retornam:

```http
404 Not Found
```

---

## MockPolicyController

Endpoint:

```http
GET /policies/customer/{customerId}
```

O mock possui políticas para o cliente `1`.

Quando um cliente não possui políticas, o mock retorna:

```http
404 Not Found
```

---

## MockVehicleController

Endpoint:

```http
GET /vehicles/{vehicleId}
```

Atualmente existem dois veículos:

```text
101 → Toyota Corolla 2025
102 → Honda Civic 2024
```

Veículos inexistentes retornam:

```http
404 Not Found
```

---

# Fluxo completo da aplicação

Quando o frontend solicita:

```http
GET http://localhost:8080/api/v1/customers/1/dashboard
```

o fluxo é:

```text
Frontend
   |
   v
CustomerDashboardController
   |
   v
GetCustomerDashboardUseCase
   |
   +--------------------+
   |                    |
   v                    v
CustomerApi          PolicyApi
   |                    |
   v                    v
CustomerApiClient   PolicyApiClient
   |                    |
   +--------+-----------+
            |
            v
       Mock API :8081
            |
            v
      Lista de Policies
            |
            v
      Para cada Policy
            |
            v
       VehicleApi
            |
            v
    VehicleApiClient
            |
            v
       Mock API :8081
            |
            v
        Vehicle
            |
            v
   CustomerDashboard
            |
            v
CustomerDashboardMapper
            |
            v
CustomerDashboardResponse
            |
            v
       HTTP 200
```

---

# Tratamento de erros

O BFF possui tratamento para os principais erros considerados neste POC.

## Customer inexistente

```http
GET /api/v1/customers/999/dashboard
```

Fluxo:

```text
CustomerApiClient
        ↓
HTTP 404 da API externa
        ↓
ResourceNotFoundException
        ↓
GlobalExceptionHandler
        ↓
HTTP 404
```

---

## Customer com nenhuma Policy

```http
GET /api/v1/customers/2/dashboard
```

Caso o mock não possua políticas para o cliente:

```text
PolicyApiClient
        ↓
HTTP 404
        ↓
ResourceNotFoundException
        ↓
GlobalExceptionHandler
        ↓
HTTP 404
```

---

## Bad Request

Quando uma API externa retornar:

```http
400 Bad Request
```

o cliente converte o erro para:

```text
BadRequestException
```

e o:

```text
GlobalExceptionHandler
```

retorna:

```http
400 Bad Request
```

---

# Configuração das APIs externas

O BFF utiliza as URLs configuradas em:

```text
src/main/resources/application.yml
```

Atualmente:

```yaml
external:
  customer-api:
    base-url: http://localhost:8081

  policy-api:
    base-url: http://localhost:8081

  vehicle-api:
    base-url: http://localhost:8081
```

Todas as APIs externas são simuladas pela aplicação mock durante o desenvolvimento da POC.

---

# Como executar

O projeto possui duas aplicações Spring Boot:

```text
BFF  → porta 8080
Mock → porta 8081
```

## 1. Executar o BFF

Execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

O BFF ficará disponível em:

```text
http://localhost:8080
```

## 2. Executar o Mock

No IntelliJ, execute a classe:

```text
MockApiApplication
```

A aplicação mock será iniciada na porta:

```text
http://localhost:8081
```

As duas aplicações precisam estar executando simultaneamente.

---

# Testando o fluxo

Com o BFF e o Mock executando, utilize:

```http
GET http://localhost:8080/api/v1/customers/1/dashboard
```

O resultado esperado é:

```http
200 OK
```

contendo:

```text
Customer
Policies
Vehicles
```

Também é possível testar os mocks diretamente:

```http
GET http://localhost:8081/customers/1
```

```http
GET http://localhost:8081/policies/customer/1
```

```http
GET http://localhost:8081/vehicles/101
```

```http
GET http://localhost:8081/vehicles/102
```

---

# Maven Wrapper

O projeto utiliza Maven Wrapper para facilitar a execução do projeto sem depender de uma instalação específica do Maven na máquina.

Arquivos:

```text
mvnw
mvnw.cmd
.mvn/wrapper/maven-wrapper.properties
```

No Windows:

```powershell
.\mvnw.cmd clean package
```

No Linux/macOS:

```bash
./mvnw clean package
```

Os arquivos do Maven Wrapper devem ser versionados no Git.

---

# Objetivo da POC

O objetivo deste projeto é demonstrar, de forma prática, a construção de um BFF utilizando Java e Spring Boot com uma organização baseada em Clean Architecture.

O foco principal está em:

* Separação de responsabilidades
* Ports e adapters
* Casos de uso
* Injeção de dependências
* Integração com APIs REST
* DTOs e mapeamento
* Agregação de dados
* Tratamento de erros HTTP
* Organização de um backend Java
* Simulação de APIs externas

A POC não possui banco de dados e não possui persistência própria. O BFF atua como uma camada de agregação entre o frontend e os serviços externos.

---

# Próximos passos possíveis

A arquitetura foi estruturada de forma que novas funcionalidades possam ser adicionadas posteriormente, como:

* Persistência de dados
* Autenticação e autorização
* Testes automatizados
* Observabilidade
* Logs estruturados
* Resiliência das integrações
* Retry e timeout
* Integração com APIs reais
* Novos endpoints do BFF

Essas funcionalidades não fazem parte do escopo atual da POC.
