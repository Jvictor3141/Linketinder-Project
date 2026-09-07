package org.linketinder.services

import org.linketinder.model.Empresa
import org.linketinder.repository.EmpresaRepository

class EmpresaService {
    final EmpresaRepository repository

    EmpresaService (EmpresaRepository repository) {
        this.repository = repository
    }

    void createEmpresa (
            String nome,
            String emailCorporativo,
            String cnpj,
            String pais,
            String estado,
            String cep,
            String descricao,
            List<String> competencias
    ) {
        Empresa empresa = new Empresa(nome, emailCorporativo, cnpj, pais, estado, cep, descricao, competencias)

        repository.adicionar(empresa)
    }
}
