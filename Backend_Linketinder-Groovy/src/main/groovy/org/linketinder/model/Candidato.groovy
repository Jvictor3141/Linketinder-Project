package org.linketinder.model

import groovy.transform.Canonical

@Canonical
class Candidato {
    String nome
    String email
    String cpf
    int idade
    String estado
    String cep
    String descricao
    List<String> competencias

    @Override
    String toString() {
        """
        ====================================================
        Nome: $nome
        E-mail: $email
        CPF: $cpf
        Idade: $idade
        Estado: $estado
        CEP: $cep
        Descrição: $descricao
        Competências: ${competencias.join(', ')}
        ====================================================
        """
    }
}
