package org.linketinder.ui

import org.linketinder.model.Competencia
import org.linketinder.model.Vaga
import org.linketinder.services.VagaService

class VagaEditForm {
    static void editFormVaga (VagaService service, Vaga vaga) {
        Scanner scanner = new Scanner(System.in)

        println "=== ATUALIZAR CADASTRO DE VAGA ==="

        println "Nome atual: $vaga.nome"
        print "Nome: "
        String nome = scanner.nextLine().trim()

        println "Descrição atual: $vaga.descricao"
        print "Descrição: "
        String descricao = scanner.nextLine().trim()

        println "Endereço atual: $vaga.endereco"
        print "Endereço "
        String endereco = scanner.nextLine().trim()

        println("=========================")
        println "Requisitos atuais: $vaga.competencias"
        List<Competencia> competencias = []

        print "Digite novas competências (ou deixe vazio para manter as atuais): "
        String competenciaText = scanner.nextLine().trim()

        while (!competenciaText.isEmpty()) {
            competencias << new Competencia(competencia: competenciaText)

            print "Digite outra competência (ou deixe vazio para finalizar): "
            competenciaText = scanner.nextLine().trim()
        }

        try {

            if(!nome.isEmpty()){
                vaga.nome = nome
            }
            if(!descricao.isEmpty()){
                vaga.descricao = descricao
            }
            if(!endereco.isEmpty()){
                vaga.endereco = endereco
            }
            if (competencias) {
                vaga.competencias = competencias
            }

            Vaga vagaAtualizada = service.atualizaVaga(vaga)

            println("========================================")
            println("Vaga $vagaAtualizada.nome atualizada com sucesso!")
            println("========================================")

        } catch (IllegalArgumentException e) {

            println("======================================================================")
            println("Erro ao atualizar vaga")
            println(e.message)
            println("======================================================================")

        }

    }
}
