package org.linketinder.services

import org.linketinder.dao.IEmpresaDAO
import org.linketinder.model.Empresa

class EmpresaService {
    private final IEmpresaDAO dao

    EmpresaService(IEmpresaDAO dao) {
        this.dao = dao
    }

    Empresa criaEmpresa ( Empresa empresa) {
        if(!empresa.nome || !empresa.emailCorporativo || !empresa.cnpj || !empresa.pais) {
            throw new IllegalArgumentException("Nenhum desses campos deve estar vazio: nome, email corporativo, cnpj, pais, competências. Tente novamente!")
        }

        dao.inserirEmpresa(empresa)

        return empresa
    }

    void deletaEmpresa(int idEmpresa) {
        def empresa = dao.buscaPorIdEmpresa(idEmpresa)
        if(!empresa) {
            throw new IllegalArgumentException("Empresa nâo encontrada, verifique e tente novamente!")
        }
        dao.deletaEmpresa(idEmpresa)
    }

    Empresa atualizaEmpresa(Empresa empresa) {
        dao.atualizaEmpresa(empresa)
        return empresa
    }

    Empresa buscaEmpresaPorId(int idEmpresa) {
        Empresa empresa = dao.buscaPorIdEmpresa(idEmpresa)
        return empresa
    }
}
