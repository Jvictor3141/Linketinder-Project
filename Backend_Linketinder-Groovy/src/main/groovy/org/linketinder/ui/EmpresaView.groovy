package org.linketinder.ui

import org.linketinder.model.Empresa

class EmpresaView {

    private final ConsoleIO console

    EmpresaView(ConsoleIO console) {
        this.console = console
    }

    Map<String, Object> lerCadastro() {
        console.exibir('=== CADASTRO DE EMPRESA ===')
        return [
                nome             : console.lerTexto('Nome: '),
                emailCorporativo : console.lerTexto('Email Corporativo: '),
                cnpj             : console.lerTexto('CNPJ ( xx.xxx.xxx/xxxx-xx ): '),
                pais             : console.lerTexto('País: '),
                cep              : console.lerTexto('CEP ( xxxxx-xxx ): '),
                descricao        : console.lerTexto('Descrição: ')
        ]
    }

    Map<String, Object> lerEdicao(Empresa atual) {
        console.exibir('=== ATUALIZAR CADASTRO DE EMPRESA ===')
        console.exibir('Deixe o campo vazio para manter o valor atual.')
        return [
                nome             : lerAlteracao('Nome', atual.nome),
                emailCorporativo : lerAlteracao('Email Corporativo', atual.emailCorporativo),
                cnpj             : lerAlteracao('CNPJ ( xx.xxx.xxx/xxxx-xx )', atual.cnpj),
                pais             : lerAlteracao('País', atual.pais),
                cep              : lerAlteracao('CEP ( xxxxx-xxx )', atual.cep),
                descricao        : lerAlteracao('Descrição', atual.descricao)
        ]
    }

    void exibirLista(List<Empresa> empresas) {
        if (!empresas) {
            console.exibir('Nenhuma empresa cadastrada.')
            return
        }

        empresas.each { Empresa empresa ->
            console.exibir("""
====================================================
    ID: ${empresa.id}
    Nome: ${empresa.nome}
    Email Corporativo: ${empresa.emailCorporativo}
    CNPJ: ${empresa.cnpj}
    País: ${empresa.pais}
    CEP: ${empresa.cep}
    Descrição: ${empresa.descricao}
====================================================""")
        }
    }

    void exibirMensagem(String mensagem) {
        console.exibir(mensagem)
    }

    private String lerAlteracao(String campo, String valorAtual) {
        console.exibir("${campo} atual: ${valorAtual}")
        String entrada = console.lerTexto("Novo valor de ${campo}: ")
        return entrada ?: valorAtual
    }
}
