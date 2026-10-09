package org.linketinder.ui

import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.services.CandidatoService

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class CandidatoForm {

    static void formularioCandidato (CandidatoService service) {
        Scanner scanner = new Scanner(System.in)

        println "=== CADASTRO DE CANDIDATO ==="

        print "Nome: "
        String nome = scanner.nextLine().trim()

        print "Sobrenome: "
        String sobrenome = scanner.nextLine().trim()

        print "Email: "
        String email = scanner.nextLine().trim()

        print "CPF ( xxx.xxx.xxx-xx ): "
        String cpf = scanner.nextLine().trim()

        print "Data de Nascimento ( dd-MM-yyyy ): "
        String dataNascimento = scanner.nextLine().trim()

        print "Estado: "
        String estado = scanner.nextLine().trim()

        print "CEP( xxxxx-xxx ): "
        String cep = scanner.nextLine().trim()

        print "Descrição: "
        String descricao = scanner.nextLine().trim()

        println("=========================")
        println "Competências:"
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

        println "\n=== DADOS DO CANDIDATO PREENCHIDO ==="
        println "Nome: $nome"
        println "Sobrenome: $sobrenome"
        println "Email: $email"
        println "CPF: $cpf"
        println "Data de Nascimento: $dataNascimento"
        println "Estado: $estado"
        println "Descrição: $descricao"
        println "Competências: $competencias"
        println()
        println()

        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            LocalDate dataNascimentoFormatada = LocalDate.parse(dataNascimento, formato)

            if(!dataNascimentoFormatada.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Data inválida, por favor digite uma data menor que hoje.")
            }

            Candidato candidatoInsert = new Candidato(
                    nome: nome,
                    sobrenome: sobrenome,
                    email: email,
                    cpf: cpf,
                    dataNascimento: dataNascimentoFormatada,
                    estado: estado,
                    cep: cep,
                    descricao: descricao,
                    competencias: competencias
            )

            service.createCandidate(candidatoInsert)

            println("========================================")
            println("Candidato $nome criado com sucesso!")
            println("========================================")

        }catch (DateTimeParseException e) {

            println("======================================================================")
            println("Erro ao criar candidato. Digite uma data válida!")
            println("======================================================================")

        } catch (IllegalArgumentException e) {

            println("======================================================================")
            println("Erro ao criar candidato")
            println(e.message)
            println("======================================================================")

        }

    }
}
