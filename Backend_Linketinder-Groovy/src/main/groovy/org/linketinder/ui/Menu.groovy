package org.linketinder.ui

import org.linketinder.model.Candidato
import org.linketinder.model.Empresa
import org.linketinder.model.Vaga
import org.linketinder.services.CandidatoService
import org.linketinder.services.EmpresaService
import org.linketinder.services.VagaService

class Menu {

    private final CandidatoService candidatoService
    private final EmpresaService empresaService
    private final VagaService vagaService

    Menu(CandidatoService candidatoService, EmpresaService empresaService, VagaService vagaService) {
        this.candidatoService = candidatoService
        this.empresaService = empresaService
        this.vagaService = vagaService
    }

    void iniciar () {

        def scanner = new Scanner(System.in)
        while (true) {
            println(
"""
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

==========================================
"""
            )
            print ">>  "
            String opcao = scanner.nextLine().trim()

            try {
                int acao = Integer.parseInt(opcao)

                switch (acao) {
                    case 1:
                        candidatoService.listaCandidatos().each { println(it) }
                        break
                    case 2:
                        empresaService.listaEmpresas().each { println(it) }
                        break
                    case 3:
                        vagaService.listaVagas().each { println(it) }
                        break
                    case 4:
                        VagasForm.formularioVaga(vagaService)
                        break
                    case 5:
                        CandidatoForm.formularioCandidato(candidatoService)
                        break
                    case 6:
                        EmpresaForm.formularioEmpresa(empresaService)
                        break
                    case 7:
                        apagarItens()
                        break
                    case 8:
                        atualizarDados()
                        break
                    case 9:
                        println "Saindo..."
                        return
                    default:
                        println 'Opção inválida. Escolha um número de 1 a 9.'
                }
            } catch (NumberFormatException e) {
                println("Opção inválida! Digite apenas números.")
            } catch (IllegalArgumentException e) {
                println("Não foi possível concluir a operação: ${e.message}")
            } catch (IllegalStateException e) {
                println("Falha ao acessar os dados. ${e.message}")
            }

        }
    }

    void apagarItens() {
        Scanner scan = new Scanner(System.in)
        println "1 - Apagar Candidato \n2 - Apagar Empresa \n3 - Apagar Vaga"
        String entrada = scan.nextLine().trim()

        try {
            int opcao = Integer.parseInt(entrada)
            switch (opcao){
                case 1:
                    print "Digite o ID do candidato: "
                    int idCandidato = Integer.parseInt(scan.nextLine().trim())
                    candidatoService.deletaCandidato(idCandidato)
                    println(
                        """
=======================================================
||               Candidato Excluido                  ||
=======================================================
""")
                    break
                case 2:
                    print "Digite o ID da empresa: "
                    int idEmpresa = Integer.parseInt(scan.nextLine().trim())
                    empresaService.deletaEmpresa(idEmpresa)
                    println(
                        """
=======================================================
||                 Empresa Excluida                  ||
=======================================================
""")
                    break
                case 3:
                    print "Digite o ID da vaga: "
                    int idVaga = Integer.parseInt(scan.nextLine().trim())
                    vagaService.deletaVaga(idVaga)
                    println(
                        """
=======================================================
||                  Vaga Excluida                    ||
=======================================================
""")
                    break
                default :
                    println "Opção inválida"
                    break
            }
        } catch (NumberFormatException e) {
            println 'Opção ou ID inválido. Digite apenas números.'
        } catch (IllegalArgumentException e) {
            println "Não foi possível excluir: ${e.message}"
        }

    }

    void atualizarDados() {
        Scanner scan = new Scanner(System.in)
        println "1 - Atualizar Candidato \n2 - Atualizar Empresa \n3 - Atualizar Vaga"
        String entrada = scan.nextLine().trim()

        try {
            int opcao = Integer.parseInt(entrada)
            switch (opcao) {
                case 1:
                    print "Digite o ID do candidato: "
                    int idCandidato = Integer.parseInt(scan.nextLine().trim())
                    Candidato candidato = candidatoService.buscaCandidatoPorId(idCandidato)
                    CandidatoEditForm.editFormCandidato(candidatoService, candidato)
                    break
                case 2:
                    print "Digite o ID da empresa: "
                    int idEmpresa = Integer.parseInt(scan.nextLine().trim())
                    Empresa empresa = empresaService.buscaEmpresaPorId(idEmpresa)
                    EmpresaEditForm.editFormEmpresa(empresaService, empresa)
                    break
                case 3:
                    print "Digite o ID da vaga: "
                    int idVaga = Integer.parseInt(scan.nextLine().trim())
                    Vaga vaga = vagaService.buscaVagaPorId(idVaga)
                    VagaEditForm.editFormVaga(vagaService, vaga)
                    break
                default:
                    println "Opção inválida"
                    break
            }
        } catch (NumberFormatException e) {
            println 'Opção ou ID inválido. Digite apenas números.'
        } catch (IllegalArgumentException e) {
            println "Não foi possível atualizar: ${e.message}"
        }
    }
}
