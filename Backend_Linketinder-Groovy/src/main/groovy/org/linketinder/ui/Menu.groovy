package org.linketinder.ui

import org.linketinder.dao.CandidatoDAO
import org.linketinder.dao.EmpresaDAO
import org.linketinder.dao.VagaDAO
import org.linketinder.model.Candidato
import org.linketinder.services.CandidatoService
import org.linketinder.services.EmpresaService
import org.linketinder.services.VagaService

class Menu {

    CandidatoService candidatoService = new CandidatoService()
    EmpresaService empresaService = new EmpresaService()
    VagaService vagaService = new VagaService()

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

            try{
                int acao = Integer.parseInt(opcao)

                if(acao == 1) {
                    def dao = new CandidatoDAO()
                    dao.listarCandidato().each {println(it)}
                } else if(acao == 2) {
                    def dao = new EmpresaDAO()
                    dao.listarEmpresas().each {println(it)}
                } else if(acao == 3) {
                    def dao = new VagaDAO()
                    dao.listarVagas().each { println(it)}
                } else if(acao == 4) {
                    VagasForm.formularioVaga(vagaService)
                } else if(acao == 5) {
                    CandidatoForm.formularioCandidato(candidatoService)
                } else if(acao == 6) {
                    EmpresaForm.formularioEmpresa(empresaService)
                } else if(acao == 7) {
                    apagarItens()
                } else if(acao == 8) {
                    atualizarDados()
                } else if(acao == 9) {
                    println "Saindo..."
                    break
                }
            } catch (NumberFormatException e) {
                println("Opção inválida! Digite apenas números.")
            }

        }
    }

    void apagarItens() {
        Scanner scan = new Scanner(System.in)
        println "1 - Apagar Candidato \n2 - Apagar Empresa \n3 - Apagar Vaga"
        String entrada = scan.nextLine().trim()

        int opcao = Integer.parseInt(entrada)
        switch (opcao){
            case 1:
                print "Digite o ID do candidato: "
                int idCandidato = Integer.parseInt(scan.nextLine().trim())
                candidatoService.delCandidato(idCandidato)
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
                empresaService.delEmpresa(idEmpresa)
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
                vagaService.delVaga(idVaga)
                println(
                        """
=======================================================
||                  Vaga Excluida                    ||
=======================================================
""")
                break
            default :
                println "Opçãp inválida"
                break
        }

    }

    void atualizarDados() {
        Scanner scan = new Scanner(System.in)
        println "1 - Atualizar Candidato \n2 - Atualizar Empresa \n3 - Atualizar Vaga"
        String entrada = scan.nextLine().trim()

        int opcao = Integer.parseInt(entrada)
        switch (opcao) {
            case 1:
                print "Digite o ID do candidato: "
                int idCandidato = Integer.parseInt(scan.nextLine().trim())
                Candidato candidato = candidatoService.buscaPorId(idCandidato)
                CandidatoEditForm.editFormCandidato(candidatoService, candidato)
                break
            case 2:
                print "Digite o ID da empresa: "
                int idEmpresa = Integer.parseInt(scan.nextLine().trim())
                empresaService.delEmpresa(idEmpresa)
                break
            case 3:
                print "Digite o ID da vaga: "
                int idVaga = Integer.parseInt(scan.nextLine().trim())
                vagaService.delVaga(idVaga)
                break
            default:
                println "Opçãp inválida"
                break
        }
    }
}
