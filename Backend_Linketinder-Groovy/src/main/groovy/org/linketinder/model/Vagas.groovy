package org.linketinder.model

import groovy.transform.Canonical

@Canonical
class Vagas {
    String nome
    String descricao
    String endereco
    Integer empresa
    String nomeEmpresa
    List<Competencia> competencias = []
    Integer id

    @Override
    String toString() {
        """
    ====================================================
        Nome: $nome
        Descrição: $descricao
        Competencias: $endereco
        Empresa: $nomeEmpresa
        Requisitos: $competencias
    ====================================================
        """
    }
}
