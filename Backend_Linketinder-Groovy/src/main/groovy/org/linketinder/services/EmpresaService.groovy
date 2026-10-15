package org.linketinder.services

import org.linketinder.dao.jdbc.EmpresaDAOjdbc
import org.linketinder.model.Empresa

class EmpresaService {
    private final EmpresaDAOjdbc dao

    EmpresaService(EmpresaDAOjdbc dao = new EmpresaDAOjdbc()) {
        this.dao = dao
    }

    Empresa createEmpresa ( Empresa empresa) {
        if(!empresa.nome || !empresa.emailCorporativo || !empresa.cnpj || !empresa.pais) {
            throw new IllegalArgumentException("Nenhum desses campos deve estar vazio: nome, email corporativo, cnpj, pais, competências. Tente novamente!")
        }

        dao.inserirEmpresa(empresa)

        return empresa
    }

    void delEmpresa(int idEmpresa) {
        def empresa = dao.buscaPorIdEmpresa(idEmpresa)
        if(!empresa) {
            throw new IllegalArgumentException("Empresa nâo encontrada, verifique e tente novamente!")
        }
        dao.deletaEmpresa(idEmpresa)
    }

    Empresa attEmpresa(Empresa empresa) {
        dao.atualizaEmpresa(empresa)
        return empresa
    }

    Empresa buscaPorId(int idEmpresa) {
        Empresa empresa = dao.buscaPorIdEmpresa(idEmpresa)
        return empresa
    }
}
