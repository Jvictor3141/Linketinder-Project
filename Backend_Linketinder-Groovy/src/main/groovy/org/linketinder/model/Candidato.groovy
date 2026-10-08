package org.linketinder.model

import groovy.transform.Canonical

import java.time.LocalDate

@Canonical
class Candidato {
    String nome
    String sobrenome
    String email
    String cpf
    LocalDate dataNascimento
    String estado
    String cep
    String descricao
    List<Competencia> competencias = []
    Integer id

    @Override
    String toString() {
        """
    ====================================================
        Nome: $nome
        Sobrenome: $sobrenome
        E-mail: $email
        CPF: $cpf
        Idade: $dataNascimento
        Estado: $estado
        CEP: $cep
        Descrição: $descricao
        Competencias: $competencias
    ====================================================
        """
    }
}
