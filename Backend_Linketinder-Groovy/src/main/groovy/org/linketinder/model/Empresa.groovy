package org.linketinder.model

import groovy.transform.Canonical

@Canonical
class Empresa {
    String nome
    String emailCorporativo
    String cnpj
    String pais
    String estado
    String cep
    String descricao
    List<String> competencias

    @Override
    String toString() {
        """
        ====================================================
        Nome: $nome
        E-mail: $emailCorporativo
        CPF: $cnpj
        Idade: $pais
        Estado: $estado
        CEP: $cep
        Descrição: $descricao
        Competências: ${competencias.join(', ')}
        ====================================================
        """
    }
}
