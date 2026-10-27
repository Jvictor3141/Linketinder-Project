package org.linketinder.view

import org.linketinder.model.Vaga

class VagaView {

    private final ConsoleIO console
    private final CompetenciasView competenciasView

    VagaView(ConsoleIO console) {
        this.console = console
        this.competenciasView = new CompetenciasView(console)
    }

    Map<String, Object> lerCadastro() {
        console.exibir('=== CADASTRO DE VAGA ===')
        return [
                nome        : console.lerTexto('Nome: '),
                descricao   : console.lerTexto('Descrição: '),
                endereco    : console.lerTexto('Endereço: '),
                idEmpresa   : lerIdEmpresa(),
                competencias: competenciasView.ler(true)
        ]
    }

    Map<String, Object> lerEdicao(Vaga atual) {
        console.exibir('=== ATUALIZAR CADASTRO DE VAGA ===')
        console.exibir('Deixe o campo vazio para manter o valor atual.')

        Map<String, Object> campos = [
                nome      : lerAlteracao('Nome', atual.nome),
                descricao : lerAlteracao('Descrição', atual.descricao),
                endereco  : lerAlteracao('Endereço', atual.endereco),
                idEmpresa : atual.idEmpresa
        ]
        console.exibir("Requisitos atuais: ${atual.competencias*.competencia}")
        campos.competencias = competenciasView.ler(false)
        return campos
    }

    void exibirLista(List<Vaga> vagas) {
        if (!vagas) {
            console.exibir('Nenhuma vaga cadastrada.')
            return
        }

        vagas.each { Vaga vaga ->
            console.exibir("""
====================================================
    ID: ${vaga.id}
    Nome: ${vaga.nome}
    Descrição: ${vaga.descricao}
    Endereço: ${vaga.endereco}
    ID Empresa: ${vaga.idEmpresa}
    Empresa: ${vaga.nomeEmpresa}
    Competências: ${vaga.competencias*.competencia}
====================================================""")
        }
    }

    void exibirMensagem(String mensagem) {
        console.exibir(mensagem)
    }

    private Integer lerIdEmpresa() {
        String entrada = console.lerTexto('ID Empresa: ')
        while (!entrada.isInteger()) {
            entrada = console.lerTexto('Aviso: digite um número válido: ')
        }
        return entrada.toInteger()
    }

    private String lerAlteracao(String campo, String valorAtual) {
        console.exibir("${campo} atual: ${valorAtual}")
        String entrada = console.lerTexto("Novo valor de ${campo}: ")
        return entrada ?: valorAtual
    }
}
