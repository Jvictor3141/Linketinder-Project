package org.linketinder.ui

import org.linketinder.data.CandidatoData
import org.linketinder.data.EmpresaData
import org.linketinder.repository.CandidatoRepository
import org.linketinder.services.CandidatoService

class Menu {

    def repositorio = new CandidatoRepository()
    def candidatoService = new CandidatoService(repositorio)

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
                    println CandidatoData.candidatos
                } else if(acao == 2) {
                    println EmpresaData.empresas
                }else if(acao == 3) {
                    CandidatoForm.formulario(candidatoService)
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
