package org.linketinder

import org.linketinder.dao.CandidatoDAO
import org.linketinder.dao.CompetenciaDAO
import org.linketinder.dao.VagaDAO
import org.linketinder.database.ConexaoDB
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.model.Vagas
import org.linketinder.ui.Menu
import org.postgresql.util.PSQLException

import java.time.LocalDate

static void main(String[] args) {
    Menu menu = new Menu()

    //def c = new Vagas(nome: "Teste2", descricao: "teste", endereco: "Teste", competencias: [new Competencia(competencia:  "Java"), new Competencia(competencia:  "Python"), new Competencia(competencia:  "C++")], empresa: 3)
    println new VagaDAO().listarVagas()

    menu.iniciar()
}