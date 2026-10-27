package org.linketinder.controller

import org.linketinder.ui.MenuView

class MenuController {
    private final MenuView view
    private final CandidatoController candidatos
    private final EmpresaController empresas
    private final VagaController vagas

    MenuController(MenuView view, CandidatoController candidatos,
                   EmpresaController empresas, VagaController vagas) {
        this.view = view
        this.candidatos = candidatos
        this.empresas = empresas
        this.vagas = vagas
    }

    void iniciar() {
        while (true) {
            try {
                switch (view.lerOpcao()) {
                    case 1:
                        candidatos.listar()
                        break
                    case 2:
                        empresas.listar()
                        break
                    case 3:
                        vagas.listar()
                        break
                    case 4:
                        vagas.criar()
                        break
                    case 5:
                        candidatos.criar()
                        break
                    case 6:
                        empresas.criar()
                        break
                    case 7:
                        excluir()
                        break
                    case 8:
                        atualizar()
                        break
                    case 9:
                        view.exibirMensagem('Saindo...')
                        return
                    default:
                        view.exibirMensagem('Opção inválida. Escolha um número de 1 a 9.')
                }
            } catch (EOFException e) {
                view.exibirMensagem('Entrada encerrada.')
                return
            } catch (NumberFormatException e) {
                view.exibirMensagem('Opção ou ID inválido. Digite apenas números.')
            } catch (IllegalArgumentException e) {
                view.exibirMensagem("Não foi possível concluir a operação: ${e.message}")
            } catch (IllegalStateException e) {
                view.exibirMensagem("Falha ao acessar os dados. ${e.message}")
            }
        }
    }

    private void excluir() {
        switch (view.lerTipoExclusao()) {
            case 1:
                candidatos.excluir(view.lerId('candidato'))
                break
            case 2:
                empresas.excluir(view.lerId('empresa'))
                break
            case 3:
                vagas.excluir(view.lerId('vaga'))
                break
            default:
                view.exibirMensagem('Opção inválida. Escolha um número de 1 a 3.')
        }
    }

    private void atualizar() {
        switch (view.lerTipoEdicao()) {
            case 1:
                candidatos.atualizar(view.lerId('candidato'))
                break
            case 2:
                empresas.atualizar(view.lerId('empresa'))
                break
            case 3:
                vagas.atualizar(view.lerId('vaga'))
                break
            default:
                view.exibirMensagem('Opção inválida. Escolha um número de 1 a 3.')
        }
    }
}
