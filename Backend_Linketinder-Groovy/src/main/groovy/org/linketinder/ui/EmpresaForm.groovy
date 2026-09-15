package org.linketinder.ui

import org.linketinder.services.EmpresaService

class EmpresaForm {
    static void formularioEmpresa (EmpresaService service) {
        Scanner scanner = new Scanner(System.in)

        println "=== CADASTRO DE EMPRESA ==="

        print "Nome: "
        def nome = scanner.nextLine().trim()

        print "Email Corporativo: "
        def emailCorporativo = scanner.nextLine().trim()

        print "CNPJ ( xx.xxx.xxx/xxxx-xx ): "
        def cpf = scanner.nextLine().trim()

        print "País: "
        def pais = scanner.nextLine().trim()

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

        println "\n=== DADOS DA EMPRESA PREENCHIDO ==="
        println "Nome: $nome"
        println "Email Corporativo: $emailCorporativo"
        println "CNPJ: $cpf"
        println "País: $pais"
        println "Estado: $estado"
        println "Descrição: $descricao"
        println "Competências: $competencias"
        println()
        println()

        try {

            service.createEmpresa(nome, emailCorporativo, cpf, pais, estado, cep, descricao, competencias)

            println("========================================")
            println("Empresa $nome criada com sucesso!")
            println("========================================")

        } catch (IllegalArgumentException e) {

            println("======================================================================")
            println("Erro ao criar empresa")
            println(e.message)
            println("======================================================================")

        }

    }
}
