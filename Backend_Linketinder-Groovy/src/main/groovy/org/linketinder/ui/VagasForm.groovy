package org.linketinder.ui

import org.linketinder.model.Competencia
import org.linketinder.model.Vagas
import org.linketinder.services.VagaService

class VagasForm {
    static void formularioVaga (VagaService service) {
        Scanner scanner = new Scanner(System.in)

        println "=== CADASTRO DE VAGA ==="

        print "Nome: "
        String nome = scanner.nextLine().trim()

        print "Descrição: "
        String descricao = scanner.nextLine().trim()

        print "Endereço "
        String endereco = scanner.nextLine().trim()

        print "ID Empresa "
        String idEmpresaText = scanner.nextLine().trim()
        while (!idEmpresaText.isInteger()) {
            print "Aviso: digite um número válido: "
            idEmpresaText = scanner.nextLine().trim()
        }

        println("=========================")
        println "Requisitos:"
        List<Competencia> competencias = []

        print "Digite uma competência: "
        String competenciaText = scanner.nextLine().trim()

        while (competenciaText.isEmpty()) {
            print "Aviso: precisa digitar pelo menos uma competência: "
            competenciaText = scanner.nextLine().trim()
        }

        competencias << new Competencia(competencia: competenciaText)

        print "Digite outra competência (ou deixe vazio para finalizar): "
        competenciaText = scanner.nextLine().trim()

        while (!competenciaText.isEmpty()) {
            competencias << new Competencia(competencia: competenciaText)

            print "Digite outra competência (ou deixe vazio para finalizar): "
            competenciaText = scanner.nextLine().trim()
        }

        println "\n=== DADOS DA VAGA PREENCHIDO ==="
        println "Nome: $nome"
        println "Descrição: $descricao"
        println "Endereço: $endereco"
        println "ID Empresa: $idEmpresaText"
        println "Competências: $competencias"
        println()
        println()

        try {
            Integer idEmpresa = idEmpresaText.toInteger()

            Vagas vagaInsert = new Vagas(
                    nome: nome,
                    descricao: descricao,
                    endereco: endereco,
                    empresa: idEmpresa,
                    competencias: competencias
            )

            service.createVaga(vagaInsert)

            println("========================================")
            println("Vaga $nome criada com sucesso!")
            println("========================================")

        } catch (IllegalArgumentException e) {

            println("======================================================================")
            println("Erro ao criar vaga")
            println(e.message)
            println("======================================================================")

        }

    }
}
