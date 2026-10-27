package org.linketinder.model

import groovy.transform.Canonical

@Canonical
class Vaga {
    String nome
    String descricao
    String endereco
    Integer idEmpresa
    String nomeEmpresa
    List<Competencia> competencias = []
    Integer id
}
