package org.linketinder.services

import org.linketinder.dao.EmpresaDAO
import org.linketinder.model.Empresa

class EmpresaService {
    private final EmpresaDAO dao

    EmpresaService(EmpresaDAO dao = new EmpresaDAO()) {
        this.dao = dao
    }

    Empresa createEmpresa ( Empresa empresa) {
        if(!empresa.nome || !empresa.emailCorporativo || !empresa.cnpj || !empresa.pais) {
            throw new IllegalArgumentException("Nenhum desses campos deve estar vazio: nome, email corporativo, cnpj, pais, competências. Tente novamente!")
        }

        dao.inserir(empresa)

        return empresa
    }
}
