package org.linketinder.ui

import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.services.CandidatoService

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class CandidatoEditForm {

    static void editFormCandidato (CandidatoService service, Candidato candidato) {
        Scanner scanner = new Scanner(System.in)

        println "=== ATUALIZAR CADASTRO DE CANDIDATO ==="

        println "Nome atual: $candidato.nome"
        print "Novo Nome: "
        String nome = scanner.nextLine().trim()

        println "Sobrenome atual: $candidato.sobrenome"
        print "Novo Sobrenome: "
        String sobrenome = scanner.nextLine().trim()

        println "Email atual: $candidato.email"
        print "Novo Email: "
        String email = scanner.nextLine().trim()

        println "CPF atual: $candidato.cpf"
        print "Novo CPF ( xxx.xxx.xxx-xx ): "
        String cpf = scanner.nextLine().trim()

        println "Data de Nascimento atual: $candidato.dataNascimento"
        print "Nova Data de Nascimento ( dd-MM-yyyy ): "
        String dataNascimento = scanner.nextLine().trim()

        println "Estado atual: $candidato.estado"
        print "Novo Estado: "
        String estado = scanner.nextLine().trim()

        println "CEP atual: $candidato.cep"
        print "Novo CEP( xxxxx-xxx ): "
        String cep = scanner.nextLine().trim()

        println("Descrição atual: $candidato.descricao")
        print "Nova Descrição: "
        String descricao = scanner.nextLine().trim()

        println("=========================")
        println "Competências Atuais $candidato.competencias"
        List<Competencia> competencias = []

        print "Digite novamente suas competências: "
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

        try {

            if(!dataNascimento.isEmpty()) {
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                LocalDate dataNascimentoFormatada = LocalDate.parse(dataNascimento, formato)

                if(!dataNascimentoFormatada.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("Data inválida, por favor digite uma data menor que hoje.")
                }

                candidato.dataNascimento = dataNascimentoFormatada
            }

            if(!nome.isEmpty()) {
                candidato.nome = nome
            }
            if(!sobrenome.isEmpty()) {
                candidato.sobrenome = sobrenome
            }
            if(!email.isEmpty()) {
                candidato.email = email
            }
            if(!cpf.isEmpty()) {
                candidato.cpf = cpf
            }
            if(!estado.isEmpty()) {
                candidato.estado = estado
            }
            if(!cep.isEmpty()) {
                candidato.cep = cep
            }
            if(!descricao.isEmpty()) {
                candidato.descricao = descricao
            }
            if(!competencias.isEmpty()) {
                candidato.competencias = competencias
            }

            Candidato candidatoAtualizado = service.attCandidato(candidato)

            println("========================================")
            println("Candidato $candidatoAtualizado.nome atualizado com sucesso!")
            println("========================================")

        }catch (DateTimeParseException e) {

            println("======================================================================")
            println("Erro ao atualizar candidato. Digite uma data válida!")
            println("======================================================================")

        } catch (IllegalArgumentException e) {

            println("======================================================================")
            println("Erro ao atualizar candidato")
            println(e.message)
            println("======================================================================")

        }

    }
}
