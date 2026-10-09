package org.linketinder.ui

import org.linketinder.model.Empresa
import org.linketinder.services.EmpresaService

class EmpresaEditForm {
    static void editFormEmpresa (EmpresaService service, Empresa empresa) {
        Scanner scanner = new Scanner(System.in)

        println "=== ATUALIZAÇÂO DE CADASTRO DE EMPRESA ==="

        println "Nome atual: $empresa.nome"
        print "Novo Nome: "
        String nome = scanner.nextLine().trim()

        println "Email Corporativo atual: $empresa.emailCorporativo"
        print "Novo Email Corporativo: "
        String emailCorporativo = scanner.nextLine().trim()

        println "CNPJ atual: $empresa.cnpj"
        print "Novo CNPJ ( xx.xxx.xxx/xxxx-xx ): "
        String cnpj = scanner.nextLine().trim()

        println "Pais atual: $empresa.pais"
        print "Novo País: "
        String pais = scanner.nextLine().trim()

        println "CEP atual: $empresa.cep"
        print "Novo CEP( xxxxx-xxx ): "
        String cep = scanner.nextLine().trim()

        println "Descrição atual: $empresa.descricao"
        print "Nova Descrição: "
        String descricao = scanner.nextLine().trim()


        try {
            if(!nome.isEmpty()) {
                empresa.nome = nome
            }
            if(!emailCorporativo.isEmpty()) {
                empresa.emailCorporativo = emailCorporativo
            }
            if(!cnpj.isEmpty()) {
                empresa.cnpj = cnpj
            }
            if(!pais.isEmpty()) {
                empresa.pais = pais
            }
            if(!cep.isEmpty()) {
                empresa.cep = cep
            }
            if(!descricao.isEmpty()) {
                empresa.descricao = descricao
            }

            Empresa empresaAtualizada = service.attEmpresa(empresa)

            println("========================================")
            println("Empresa $empresaAtualizada.nome atualizada com sucesso!")
            println("========================================")

        } catch (IllegalArgumentException e) {

            println("======================================================================")
            println("Erro ao atualizar empresa")
            println(e.message)
            println("======================================================================")

        }

    }
}
