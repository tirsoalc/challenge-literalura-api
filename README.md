# Literalura API

## Descrição

O **Literalura API** é uma aplicação console desenvolvida em Java que permite buscar, armazenar e consultar informações sobre livros através da integração com a API do Gutendex. A aplicação foi criada como parte do desafio do programa ONE (Oracle Next Education) da Alura.

## Funcionalidades Principais

### 1. Buscar Livro pelo Título
- Pesquisa livros na API do Gutendex por título
- Armazena automaticamente os dados no banco PostgreSQL
- Evita duplicação de livros já registrados
- Exibe informações detalhadas do livro encontrado

### 2. Listar Livros Registrados
- Exibe todos os livros salvos no banco de dados
- Mostra título, autor, idioma e número de downloads
- Inclui contador total de livros registrados

### 3. Listar Autores Registrados
- Apresenta todos os autores cadastrados
- Mostra ano de nascimento e falecimento
- Lista todos os livros associados a cada autor
- Inclui contador total de autores

### 4. Filtrar Autores por Ano
- Permite buscar autores que estavam vivos em um ano específico
- Considera autores ainda vivos (sem ano de falecimento)
- Valida entrada do usuário para anos

### 5. Filtrar Livros por Idioma
- Filtra livros por idioma (português, inglês, espanhol, francês)
- Apresenta estatísticas detalhadas:
  - Total de livros no idioma
  - Soma e média de downloads
  - Número de autores únicos
  - Livro mais baixado

## Regras de Negócio

### Gestão de Livros
- **Prevenção de Duplicatas**: Livros são identificados por título e autor para evitar registros duplicados
- **Relacionamento com Autores**: Cada livro está associado a exatamente um autor
- **Constraint de Idioma**: Apenas um idioma por livro (primeiro da lista da API)

### Gestão de Autores
- **Criação Automática**: Autores são criados automaticamente quando um novo livro é registrado
- **Relacionamento Um-para-Muitos**: Um autor pode ter vários livros associados
- **Validação de Vida**: Sistema determina se autor estava vivo em determinado ano

### Integração com API
- **Fonte de Dados**: Utiliza a API do Gutendex (https://gutendex.com/)
- **Tratamento de Erros**: Gerencia timeouts, falhas de conexão e respostas inválidas
- **Encoding**: Suporte a caracteres especiais na busca

## Tecnologias Utilizadas

- **Java 21**: Linguagem principal
- **Spring Boot 3.5.3**: Framework de desenvolvimento
- **Spring Data JPA**: ORM para persistência
- **PostgreSQL**: Banco de dados relacional
- **Jackson 2.16**: Processamento JSON
- **Maven**: Gerenciamento de dependências
- **HttpClient**: Cliente HTTP nativo do Java

## Arquitetura

### Camadas da Aplicação
- **View**: Interface de console (`MenuView`)
- **Service**: Lógica de negócio (`MenuService`, `BookService`, `AuthorService`, `GutendexService`)
- **Repository**: Acesso a dados (`BookRepository`, `AuthorRepository`)
- **Model**: Entidades JPA (`Book`, `Author`, `Language`)
- **DTO**: Transferência de dados (`BookDto`, `AuthorDto`, `GutendexResponse`)

### Padrões Implementados
- **Dependency Injection**: Injeção de dependências com Spring
- **Repository Pattern**: Acesso padronizado aos dados
- **DTO Pattern**: Separação entre dados da API e entidades
- **Service Layer**: Separação da lógica de negócio

## Como Executar

### Pré-requisitos
- Java 21 ou superior
- PostgreSQL instalado e configurado
- Maven 3.6 ou superior

### Configuração
Configure as variáveis de ambiente para o banco:
   ```
   DB_HOST=host
   DB_PORT=porta
   DB_NAME=nome_do_banco
   DB_USERNAME=seu_usuario
   DB_PASSWORD=sua_senha
   ```

## Menu de Opções

```
Escolha o número da sua opção
1- buscar livro pelo título
2- listar livros registrados
3- listar autores registrados
4- listar autores vivos em um determinado ano
5- listar livros em um determinado idioma
0- sair
```