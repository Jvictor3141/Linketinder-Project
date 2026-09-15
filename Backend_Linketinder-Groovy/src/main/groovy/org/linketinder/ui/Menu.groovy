package org.linketinder.ui

import org.linketinder.data.CandidatoData
import org.linketinder.data.EmpresaData
import org.linketinder.model.Candidato
import org.linketinder.model.Empresa
import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.services.CandidatoService
import org.linketinder.services.EmpresaService

class Menu {

    def repositorioCandidato = new CandidatoRepository()
    def candidatoService = new CandidatoService(repositorioCandidato)

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
  3 - Criar Candidato
  4 - Criar Empresa
  5 - Sair

==========================================
"""
            )
            print ">>  "
            def opcao = scanner.nextLine().trim()

            try{
                int acao = Integer.parseInt(opcao)

                if(acao == 1) {
                    if(repositorioCandidato.listarCandidatos().size() > 0) {
                        for (Candidato candidato : repositorioCandidato.listarCandidatos()) {
                            println(candidato)
                        }
                    } else {
                        println CandidatoData.candidatos
                    }
                } else if(acao == 2) {
                    if(repositorioEmpresa.listarEmpresas().size() > 0) {
                        for (Empresa empresa : repositorioEmpresa.listarEmpresas()) {
                            println(empresa)
                        }
                    } else {
                        println EmpresaData.empresas
                    }
                }else if(acao == 3) {
                    CandidatoForm.formularioCandidato(candidatoService)
                } else if(acao == 4) {
                    EmpresaForm.formularioEmpresa(empresaService)
                } else if(acao == 5) {
                    println "Saindo..."
                    break
                }
            } catch (NumberFormatException e) {
                println("Opção inválida! Digite apenas números.")
            }

        }
    }
}
