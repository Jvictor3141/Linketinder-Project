package org.linketinder.ui

import org.linketinder.dao.CandidatoDAO
import org.linketinder.dao.EmpresaDAO
import org.linketinder.dao.VagaDAO
import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.services.CandidatoService
import org.linketinder.services.EmpresaService

class Menu {

    CandidatoService candidatoService = new CandidatoService()

    def repositorioEmpresa = new EmpresaRepository()
    def empresaService = new EmpresaService(repositorioEmpresa)

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
  7 - Sair

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

                } else if(acao == 5) {
                    CandidatoForm.formularioCandidato(candidatoService)
                } else if(acao == 6) {
                    EmpresaForm.formularioEmpresa(empresaService)
                } else if(acao == 7) {
                    println "Saindo..."
                    break
                }
            } catch (NumberFormatException e) {
                println("Opção inválida! Digite apenas números.")
            }

        }
    }
}
