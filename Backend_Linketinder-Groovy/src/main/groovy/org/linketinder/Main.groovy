package org.linketinder

import org.linketinder.dao.CandidatoDAO
import org.linketinder.dao.CompetenciaDAO
import org.linketinder.dao.VagaDAO
import org.linketinder.database.ConexaoDB
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.ui.Menu
import org.postgresql.util.PSQLException

import java.time.LocalDate

static void main(String[] args) {
    Menu menu = new Menu()

    menu.iniciar()
}