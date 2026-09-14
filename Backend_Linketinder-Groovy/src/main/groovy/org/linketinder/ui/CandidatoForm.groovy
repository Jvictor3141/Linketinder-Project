package org.linketinder.ui

import org.linketinder.services.CandidatoService

class CandidatoForm {

    static void formulario (CandidatoService service) {
        Scanner scanner = new Scanner(System.in)

        println "=== CADASTRO DE CANDIDATO ==="

        print "Nome: "
        def nome = scanner.nextLine().trim()

        print "Email: "
        def email = scanner.nextLine().trim()

        print "CPF ( xxx.xxx.xxx-xx ): "
        def cpf = scanner.nextLine().trim()

        print "Idade: "
        def idade = scanner.nextInt()
        scanner.nextLine()

        print "Estado: "
        def estado = scanner.nextLine().trim()

        print "CEP( xxxxx-xxx ): "
        def cep = scanner.nextLine().trim()

        print "Descrição: "
        def descricao = scanner.nextLine().trim()

        println("=========================")
        println "Competências:"
        def competencias = []

        print "Digite uma competência: "
        def competencia = scanner.nextLine().trim()

        while (competencia.isEmpty()) {
            print "Aviso: precisa digitar pelo menos uma competência: "
            competencia = scanner.nextLine().trim()
        }

        competencias.add(competencia)

        print "Digite outra competência (ou deixe vazio para finalizar): "
        competencia = scanner.nextLine().trim()

        while (!competencia.isEmpty()) {
            competencias.add(competencia)

            print "Digite outra competência (ou deixe vazio para finalizar): "
            competencia = scanner.nextLine().trim()
        }

        println "\n=== DADOS DO CANDIDATO PREENCHIDO ==="
        println "Nome: $nome"
        println "Email: $email"
        println "CPF: $cpf"
        println "Idade: $idade"
        println "Estado: $estado"
        println "Descrição: $descricao"
        println "Competências: $competencias"
        println()
        println()

        try {
            service.createCandidate(nome, email, cpf, idade, estado, cep, descricao, competencias)

            println("Candidato $nome criado com sucesso!")

        } catch (IllegalArgumentException e) {

            println("Erro ao criar candidato")
            println(e.message)

        }

    }
}
