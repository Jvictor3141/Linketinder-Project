package org.linketinder.view

class MenuView {
    private final ConsoleIO console

    MenuView(ConsoleIO console) {
        this.console = console
    }

    Integer lerOpcao() {
        console.exibir('''
==========================================
||                MENU                  ||
==========================================

  1 - Listar Candidatos
  2 - Listar Empresas
  3 - Feed Vagas
  4 - Criar Vagas
  5 - Criar Candidato
  6 - Criar Empresa
  7 - Apagar dados
  8 - Atualizar dados
  9 - Sair

==========================================''')
        return console.lerInteiro('>>  ')
    }

    Integer lerTipoExclusao() {
        console.exibir('1 - Apagar Candidato\n2 - Apagar Empresa\n3 - Apagar Vaga')
        return console.lerInteiro('>>  ')
    }

    Integer lerTipoEdicao() {
        console.exibir('1 - Atualizar Candidato\n2 - Atualizar Empresa\n3 - Atualizar Vaga')
        return console.lerInteiro('>>  ')
    }

    Integer lerId(String entidade) {
        return console.lerInteiro("Digite o ID de ${entidade}: ")
    }

    void exibirMensagem(String mensagem) {
        console.exibir(mensagem)
    }
}
