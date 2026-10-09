package org.linketinder.ui

import org.linketinder.model.Empresa
import org.linketinder.services.EmpresaService

class EmpresaForm {
    static void formularioEmpresa (EmpresaService service) {
        Scanner scanner = new Scanner(System.in)

        println "=== CADASTRO DE EMPRESA ==="

        print "Nome: "
        String nome = scanner.nextLine().trim()

        print "Email Corporativo: "
        String emailCorporativo = scanner.nextLine().trim()

        print "CNPJ ( xx.xxx.xxx/xxxx-xx ): "
        String cnpj = scanner.nextLine().trim()

        print "País: "
        String pais = scanner.nextLine().trim()

        print "CEP( xxxxx-xxx ): "
        String cep = scanner.nextLine().trim()

        print "Descrição: "
        String descricao = scanner.nextLine().trim()

        println "\n=== DADOS DA EMPRESA PREENCHIDO ==="
        println "Nome: $nome"
        println "Email Corporativo: $emailCorporativo"
        println "CNPJ: $cnpj"
        println "País: $pais"
        println "Cep: $cep"
        println "Descrição: $descricao"
        println()
        println()

        try {
            Empresa empresa = new Empresa(nome: nome, emailCorporativo: emailCorporativo, cnpj: cnpj, pais: pais, cep: cep, descricao: descricao)
            service.createEmpresa(empresa)

            println("========================================")
            println("Empresa $nome criada com sucesso!")
            println("========================================")

        } catch (IllegalArgumentException e) {

            println("======================================================================")
            println("Erro ao criar empresa")
            println(e.message)
            println("======================================================================")

        }

    }
}
