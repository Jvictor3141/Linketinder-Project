package org.linketinder.repository

import org.linketinder.model.Empresa

class EmpresaRepository {
    final List<Empresa> listaCandidatos = new ArrayList<>()

    List<Empresa> listarEmpresas () {
        return listaCandidatos
    }

    void adicionar(Empresa empresa) {
        listaCandidatos.add(empresa)
    }
}
