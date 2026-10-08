package org.linketinder.model

import groovy.transform.Canonical

@Canonical
class Vagas {
    String nome
    String descricao
    String endereco
    String empresa
    Integer id

    @Override
    String toString() {
        """
    ====================================================
        Nome: $nome
        Descrição: $descricao
        Competencias: $endereco
        Empresa: $empresa
    ====================================================
        """
    }
}
