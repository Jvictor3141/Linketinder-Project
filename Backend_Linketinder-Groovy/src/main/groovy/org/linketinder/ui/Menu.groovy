package org.linketinder.ui

import org.linketinder.data.CandidatoData
import org.linketinder.data.EmpresaData

class Menu {

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
  3 - Sair

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
                } else if(acao == 3) {
                    println "Saindo..."
                    break
                }
            } catch (NumberFormatException e) {
                println("Opção inválida! Digite apenas números.")
            }

        }
    }
}
