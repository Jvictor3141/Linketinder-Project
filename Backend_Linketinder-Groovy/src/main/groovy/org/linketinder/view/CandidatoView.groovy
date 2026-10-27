package org.linketinder.view

import org.linketinder.model.Candidato

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

class CandidatoView {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter
            .ofPattern('dd-MM-uuuu').withResolverStyle(ResolverStyle.STRICT)

    private final ConsoleIO console
    private final CompetenciasView competenciasView

    CandidatoView(ConsoleIO console) {
        this.console = console
        this.competenciasView = new CompetenciasView(console)
    }

    Map<String, Object> lerCadastro() {
        console.exibir('=== CADASTRO DE CANDIDATO ===')

        Map<String, Object> dados = [
                nome          : console.lerTexto('Nome: '),
                sobrenome     : console.lerTexto('Sobrenome: '),
                email         : console.lerTexto('Email: '),
                cpf           : console.lerTexto('CPF ( xxx.xxx.xxx-xx ): '),
                dataNascimento: console.lerTexto('Data de Nascimento ( dd-MM-yyyy ): '),
                estado        : console.lerTexto('Estado: '),
                cep           : console.lerTexto('CEP ( xxxxx-xxx ): '),
                descricao     : console.lerTexto('Descrição: '),
                competencias  : competenciasView.ler(true)
        ]
        dados.dataNascimento = converterData(dados.dataNascimento as String)
        return dados
    }

    Map<String, Object> lerEdicao(Candidato atual) {
        console.exibir('=== ATUALIZAR CADASTRO DE CANDIDATO ===')
        console.exibir('Deixe o campo vazio para manter o valor atual.')

        Map<String, Object> dados = [
                nome     : lerCampoEdicao('Nome', 'Novo Nome: ', atual.nome),
                sobrenome: lerCampoEdicao('Sobrenome', 'Novo Sobrenome: ', atual.sobrenome),
                email    : lerCampoEdicao('Email', 'Novo Email: ', atual.email),
                cpf      : lerCampoEdicao('CPF', 'Novo CPF ( xxx.xxx.xxx-xx ): ', atual.cpf)
        ]

        console.exibir("Data de Nascimento atual: ${atual.dataNascimento}")
        String dataNascimento = console.lerTexto('Nova Data de Nascimento ( dd-MM-yyyy ): ')

        dados.estado = lerCampoEdicao('Estado', 'Novo Estado: ', atual.estado)
        dados.cep = lerCampoEdicao('CEP', 'Novo CEP ( xxxxx-xxx ): ', atual.cep)
        dados.descricao = lerCampoEdicao('Descrição', 'Nova Descrição: ', atual.descricao)
        console.exibir("Competências atuais: ${atual.competencias*.competencia}")
        dados.competencias = competenciasView.ler(false)
        dados.dataNascimento = dataNascimento ? converterData(dataNascimento) : atual.dataNascimento
        return dados
    }

    void exibirLista(List<Candidato> candidatos) {
        if (!candidatos) {
            console.exibir('Nenhum candidato cadastrado.')
            return
        }

        candidatos.each { Candidato candidato ->
            console.exibir("""
====================================================
    ID: ${candidato.id}
    Nome: ${candidato.nome}
    Sobrenome: ${candidato.sobrenome}
    E-mail: ${candidato.email}
    CPF: ${candidato.cpf}
    Data de nascimento: ${candidato.dataNascimento}
    Estado: ${candidato.estado}
    CEP: ${candidato.cep}
    Descrição: ${candidato.descricao}
    Competências: ${candidato.competencias*.competencia}
====================================================
""")
        }
    }

    void exibirMensagem(String mensagem) {
        console.exibir(mensagem)
    }

    private String lerCampoEdicao(String rotulo, String pergunta, String atual) {
        console.exibir("${rotulo} atual: ${atual}")
        String informado = console.lerTexto(pergunta)
        return informado ? informado : atual
    }

    private static LocalDate converterData(String texto) {
        try {
            return LocalDate.parse(texto, FORMATO_DATA)
        } catch (DateTimeParseException erro) {
            throw new IllegalArgumentException('Data de nascimento inválida. Digite uma data válida no formato dd-MM-yyyy.', erro)
        }
    }
}
