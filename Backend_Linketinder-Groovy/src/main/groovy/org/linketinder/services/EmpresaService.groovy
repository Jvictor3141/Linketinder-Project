package org.linketinder.services

import org.linketinder.dao.IEmpresaDAO
import org.linketinder.model.Empresa

class EmpresaService {
    private final IEmpresaDAO dao

    EmpresaService(IEmpresaDAO dao) {
        this.dao = dao
    }

    Empresa criaEmpresa ( Empresa empresa) {
        validarDados(empresa, false)

        dao.inserirEmpresa(empresa)

        return empresa
    }

    List<Empresa> listaEmpresas() {
        return dao.listarEmpresas()
    }

    void deletaEmpresa(Integer idEmpresa) {
        ValidacaoService.validarIdPositivo('empresa', idEmpresa)
        Empresa empresa = dao.buscaPorIdEmpresa(idEmpresa)
        if(!empresa) {
            throw new IllegalArgumentException('Empresa não encontrada.')
        }
        dao.deletaEmpresa(idEmpresa)
    }

    Empresa atualizaEmpresa(Empresa empresa) {
        validarDados(empresa, true)
        dao.atualizaEmpresa(empresa)
        return empresa
    }

    Empresa buscaEmpresaPorId(Integer idEmpresa) {
        ValidacaoService.validarIdPositivo('empresa', idEmpresa)
        Empresa empresa = dao.buscaPorIdEmpresa(idEmpresa)
        if (!empresa) {
            throw new IllegalArgumentException('Empresa não encontrada.')
        }
        return empresa
    }

    private static void validarDados(Empresa empresa, boolean exigeId) {
        if (empresa == null) {
            throw new IllegalArgumentException('Os dados da empresa são obrigatórios.')
        }
        if (exigeId) {
            ValidacaoService.validarIdPositivo('empresa', empresa.id)
        }

        ValidacaoService.validarCamposObrigatorios('empresa', [
                'nome'                : empresa.nome,
                'e-mail corporativo'  : empresa.emailCorporativo,
                'CNPJ'                : empresa.cnpj,
                'país'                : empresa.pais,
                'CEP'                 : empresa.cep,
                'descrição'           : empresa.descricao
        ])
    }
}
