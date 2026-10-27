package org.linketinder.model

import groovy.transform.Canonical

@Canonical
class Empresa {
    String nome
    String emailCorporativo
    String cnpj
    String pais
    String cep
    String descricao
    Integer id
}
