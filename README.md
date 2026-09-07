# Linketinder-Project

Backend do projeto **Linketinder**, desenvolvido em **Groovy** com **Gradle**.

## Tecnologias

* Groovy 5.1.1
* Gradle
* JUnit 6

## Estrutura

```text
src/main/groovy/org/linketinder/
├── data/
├── model/
├── repository/
├── services/
├── ui/
└── Main.groovy
```

## Como executar

Clone o repositório:

```bash
git clone https://github.com/Jvictor3141/Linketinder-Project
```

Entre no diretório:

```bash
cd Linketinder-Project/Backend_Linketinder-Groovy
```

### Linux/macOS

Dê permissão de execução ao Gradle Wrapper, caso necessário:

```bash
chmod +x gradlew
```

Execute o projeto:

```bash
./gradlew run
```

### Windows

Execute:

```bash
gradlew.bat run
```

## Comandos Gradle úteis

Executar a aplicação:

```bash
./gradlew run
```

Compilar o projeto:

```bash
./gradlew build
```

## Funcionalidades

Atualmente, o menu permite:

* Listar candidatos
* Listar empresas
* Encerrar a aplicação

## Objetivo

Projeto desenvolvido para praticar **Groovy**, **Gradle** e organização de uma aplicação backend voltada para uma plataforma de conexão entre candidatos e empresas.
