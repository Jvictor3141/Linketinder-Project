package org.linketinder.services

import org.linketinder.model.Empresa
import org.linketinder.repository.EmpresaRepository

class EmpresaService {
    final EmpresaRepository repository

    EmpresaService (EmpresaRepository repository) {
        this.repository = repository
    }

    Empresa createEmpresa (
            String nome,
            String emailCorporativo,
            String cnpj,
            String pais,
            String estado,
            String cep,
            String descricao,
            List<String> competencias
    ) {
        if(!nome || !emailCorporativo || !cnpj || !estado || !competencias) {
            throw new IllegalArgumentException("Nenhum desses campos deve estar vazio: nome, email, cpf, idade, competências. Tente novamente!")
        }

        Empresa empresa = new Empresa(nome, emailCorporativo, cnpj, pais, estado, cep, descricao, competencias)

        repository.adicionar(empresa)
        return empresa
    }
}
