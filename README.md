# Linketinder-Project

Projeto **Linketinder**, com backend em **Groovy** e uma interface frontend em **TypeScript**.

## Tecnologias

* Groovy 5.1.1
* Gradle
* JUnit 6
* TypeScript 6
* Vite 8
* Chart.js 4

## Estrutura

```text
src/main/groovy/org/linketinder/
├── data/
├── model/
├── repository/
├── services/
├── ui/
└── Main.groovy

Frontend_linketinder-TypeScript/
├── index.html
└── src/
    ├── main.ts
    ├── model/
    ├── repository/
    ├── service/
    ├── utils/
    └── style.css
```

## Frontend

A interface web permite que candidatos e empresas criem contas, entrem no sistema e mantenham seus perfis. Candidatos podem apresentar formação e competências; empresas podem cadastrar e remover vagas. O painel da empresa também mostra um gráfico com a quantidade de candidatos por competência.

O frontend foi desenvolvido em **TypeScript**, com **Vite** para desenvolvimento e build, e **Chart.js** para os gráficos. Os dados e a sessão são armazenados no `localStorage` do navegador. A interface funciona de forma independente e, atualmente, não consome o backend Groovy.

### Como executar

Tenha o [Node.js](https://nodejs.org/) e o npm instalados. No terminal, entre na pasta do frontend:

```bash
cd Frontend_linketinder-TypeScript
npm install
npm run dev
```

O Vite exibirá no terminal o endereço local para abrir no navegador. Para gerar a versão de produção ou pré-visualizá-la:

```bash
npm run build
npm run preview
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
