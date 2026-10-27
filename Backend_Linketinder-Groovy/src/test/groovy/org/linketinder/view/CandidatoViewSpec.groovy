package org.linketinder.view

import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

class CandidatoViewSpec extends Specification {

    private final ByteArrayOutputStream saida = new ByteArrayOutputStream()

    private CandidatoView viewComEntrada(String entrada) {
        new CandidatoView(new ConsoleIO(new Scanner(entrada), new PrintStream(saida, true, 'UTF-8')))
    }

    private static Candidato candidatoAtual() {
        new Candidato(
                id: 7, nome: 'João', sobrenome: 'Silva', email: 'joao@email.com',
                cpf: '123.456.789-00', dataNascimento: LocalDate.of(2000, 1, 1),
                estado: 'CE', cep: '60000-000', descricao: 'Desenvolvedor',
                competencias: [new Competencia(id: 4, competencia: 'Java')]
        )
    }

    def 'cadastro converte a entrada do terminal em dados sem aplicar regras de negócio'() {
        given:
        CandidatoView view = viewComEntrada('João\nSilva\njoao@email.com\n123.456.789-00\n29-02-2000\nCE\n60000-000\nDesenvolvedor\nJava\n\n')

        when:
        Map dados = view.lerCadastro()

        then:
        dados.nome == 'João'
        dados.dataNascimento == LocalDate.of(2000, 2, 29)
        dados.competencias*.competencia == ['Java']
        !dados.containsKey('id')
    }

    @Unroll
    def 'rejeita a data inválida #data sem normalizar o calendário'() {
        given:
        CandidatoView view = viewComEntrada("João\nSilva\njoao@email.com\n123.456.789-00\n${data}\nCE\n60000-000\nDesenvolvedor\nJava\n\n")

        when:
        view.lerCadastro()

        then:
        IllegalArgumentException erro = thrown()
        erro.message.contains('dd-MM-yyyy')

        where:
        data << ['31-02-2000', '29-02-2001', '2000-01-01']
    }

    def 'edição em branco preserva os campos e sinaliza que os vínculos de competências devem permanecer'() {
        given:
        Candidato atual = candidatoAtual()
        CandidatoView view = viewComEntrada('\n' * 9)

        when:
        Map dados = view.lerEdicao(atual)

        then:
        dados.nome == atual.nome
        dados.sobrenome == atual.sobrenome
        dados.email == atual.email
        dados.cpf == atual.cpf
        dados.dataNascimento == atual.dataNascimento
        dados.estado == atual.estado
        dados.cep == atual.cep
        dados.descricao == atual.descricao
        dados.containsKey('competencias')
        dados.competencias == null
        !dados.containsKey('id')
        atual.competencias*.id == [4]
    }

    def 'edição coleta novos valores sem modificar a entidade consultada'() {
        given:
        Candidato atual = candidatoAtual()
        Competencia competenciaOriginal = atual.competencias.first()
        CandidatoView view = viewComEntrada('Maria\n\n\n\n29-02-2000\n\n\n\nGroovy\n\n')

        when:
        Map dados = view.lerEdicao(atual)

        then:
        dados.nome == 'Maria'
        dados.dataNascimento == LocalDate.of(2000, 2, 29)
        dados.competencias*.competencia == ['Groovy']
        atual.nome == 'João'
        atual.dataNascimento == LocalDate.of(2000, 1, 1)
        atual.competencias == [competenciaOriginal]
        atual.competencias.first().id == 4
    }

    def 'listagem apresenta os campos e o identificador na view'() {
        when:
        viewComEntrada('').exibirLista([candidatoAtual()])

        then:
        String texto = saida.toString('UTF-8')
        texto.contains('ID: 7')
        texto.contains('Nome: João')
        texto.contains('Data de nascimento: 2000-01-01')
        texto.contains('Competências: [Java]')
    }
}
