package org.linketinder.dao

import org.linketinder.model.Empresa

interface IEmpresaDAO {
    List<Empresa> listarEmpresas()
    Empresa inserirEmpresa(Empresa empresa)
    void deletaEmpresa(Integer idEmpresa)
    Empresa buscaPorIdEmpresa(Integer idEmpresa)
    Empresa atualizaEmpresa(Empresa empresa)
}