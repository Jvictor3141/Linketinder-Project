package org.linketinder

import org.linketinder.dao.ICompetenciaDAO
import org.linketinder.dao.jdbc.CandidatoDAOjdbc
import org.linketinder.dao.jdbc.CompetenciaDAOjdbc
import org.linketinder.dao.jdbc.EmpresaDAOjdbc
import org.linketinder.dao.jdbc.VagaDAOjdbc
import org.linketinder.database.ConexaoDB
import org.linketinder.database.IConexaoFactory
import org.linketinder.database.IDatabaseErrorTranslator
import org.linketinder.database.PostgresErrorTranslator
import org.linketinder.services.CandidatoService
import org.linketinder.services.EmpresaService
import org.linketinder.services.VagaService
import org.linketinder.ui.Menu

static void main(String[] args) {
    IConexaoFactory conexaoFactory = new ConexaoDB()
    IDatabaseErrorTranslator tradutorErros = new PostgresErrorTranslator()
    ICompetenciaDAO competenciaDAO = new CompetenciaDAOjdbc(conexaoFactory, tradutorErros)

    CandidatoService candidatoService = new CandidatoService(new CandidatoDAOjdbc(conexaoFactory, competenciaDAO, tradutorErros))
    EmpresaService empresaService = new EmpresaService(new EmpresaDAOjdbc(conexaoFactory, tradutorErros))
    VagaService vagaService = new VagaService(new VagaDAOjdbc(conexaoFactory, competenciaDAO, tradutorErros))

    Menu menu = new Menu(candidatoService, empresaService, vagaService)

    menu.iniciar()
}
